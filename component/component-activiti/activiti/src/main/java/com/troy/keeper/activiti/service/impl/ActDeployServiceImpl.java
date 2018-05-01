package com.troy.keeper.activiti.service.impl;

import com.troy.keeper.activiti.domain.ActReDeployment;
import com.troy.keeper.activiti.dto.QueryDeployResourceDTO;
import com.troy.keeper.activiti.dto.QueryDeploymentDTO;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.repository.ActDeployRepository;
import com.troy.keeper.activiti.service.ActDeployService;
import com.troy.keeper.activiti.service.mapper.ActReDeploymentMapper;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date:     2017/6/13 10:36<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Service
@Transactional
public class ActDeployServiceImpl implements ActDeployService {
    private Logger log = LoggerFactory.getLogger(ActDeployServiceImpl.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActDeployRepository actDeployRepository;

    @Autowired
    private ActReDeploymentMapper mapper;

    @Override
    public String deploy(MultipartFile file, String fileName, String name, String category) throws Exception {
        String message = "";
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;
            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream, Charset.forName("UTF-8"));
                deployment = repositoryService.createDeployment().addZipInputStream(zip).name(name).deploy();
            } else if (extension.equals("png")) {
                deployment =
                        repositoryService.createDeployment()
                                .addInputStream(fileName, fileInputStream).name(name).deploy();
            } else if (fileName.indexOf("bpmn20.xml") != -1) {
                deployment =
                        repositoryService.createDeployment()
                                .addInputStream(fileName, fileInputStream).name(name).deploy();
            } else if (extension.equals("bpmn")) { // bpmn扩展名特殊处理，转换为bpmn20.xml
                String baseName = FilenameUtils.getBaseName(fileName);
                deployment =
                        repositoryService.createDeployment()
                                .addInputStream(baseName + ".bpmn20.xml", fileInputStream).name(name).deploy();
            } else {
                message = "不支持的文件类型：" + extension;
            }
            if(deployment == null) {
                return "部署失败";
            }
            List<ProcessDefinition> list =
                    repositoryService.createProcessDefinitionQuery()
                            .deploymentId(deployment.getId()).list();
            if(!CollectionUtils.isEmpty(list)) {
                // 设置流程分类
                for (ProcessDefinition processDefinition : list) {
                    repositoryService.setProcessDefinitionCategory(processDefinition.getId(), category);
                }
            } else {
                message = "部署失败";
            }

        } catch (Exception e) {
            message = "部署失败";
        }
        return message;
    }

    @Override
    public void delete(ActProcessParams param) throws Exception {
        repositoryService.deleteDeployment(param.getDeployId(), true);
    }

    @Override
    public List<QueryDeploymentDTO> queryDeployments(ActProcessParams param) throws Exception {
        List<ActReDeployment> deployments = actDeployRepository.findAll(getDeploymentSpec(param));
        return mapper.objsToDTOs(deployments);

    }

    private Specification<ActReDeployment> getDeploymentSpec(final ActProcessParams param) {
        return new Specification<ActReDeployment>() {
                @Override
                public Predicate toPredicate(Root<ActReDeployment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (!StringUtils.isEmpty(param.getName())) {
                        predicates.add(criteriaBuilder.like(root.<String>get("name"), "%" + param.getName() + "%"));
                    }
                    Date startDate;
                    Date endDate;
                    Calendar calendar = Calendar.getInstance();
                    if (!StringUtils.isEmpty(param.getStartTime())) {
                        try {
                            startDate = format.parse(param.getStartTime());
                            calendar.setTime(startDate);
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Instant>get("deployTime"), calendar.getTime().toInstant()));
                        } catch (ParseException e) {
                            log.error("日期格式错误");
                        }
                    }
                    if (!StringUtils.isEmpty(param.getEndTime())) {
                        try {
                            endDate = format.parse(param.getEndTime());
                            calendar.setTime(endDate);
                            calendar.set(Calendar.HOUR_OF_DAY, 23);
                            calendar.set(Calendar.MINUTE, 59);
                            calendar.set(Calendar.SECOND, 59);
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("deployTime"), calendar.getTime().toInstant()));
                        } catch (ParseException e) {
                            log.error("日期格式错误");
                        }
                    }
                    Order o = criteriaBuilder.desc(root.<Instant>get("deployTime"));
                    return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(o).getRestriction();
                }
            };
    }

    @Override
    public Page<QueryDeploymentDTO> queryDeploymentsPage(ActProcessParams param, Pageable pageable) throws Exception {
        Page<ActReDeployment> deployments = actDeployRepository.findAll(getDeploymentSpec(param), pageable);
        return deployments.map(new Converter<ActReDeployment, QueryDeploymentDTO>() {
            @Override
            public QueryDeploymentDTO convert(ActReDeployment actReDeployment) {
                return mapper.objToDTO(actReDeployment);
            }
        });
    }

    @Override
    public List<QueryDeployResourceDTO> queryDeployResource(ActProcessParams param) throws Exception {
        return repositoryService.getDeploymentResourceNames(param.getDeployId()).stream().map((name) -> {
            QueryDeployResourceDTO deployResourceDTO = new QueryDeployResourceDTO();
            deployResourceDTO.setName(name);
            return deployResourceDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void downloadResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String deployId = request.getParameter("deployId");
        String name = request.getParameter("name");
        if(!StringUtils.isEmpty(deployId) && !StringUtils.isEmpty(name)) {
            InputStream resourceAsStream = repositoryService.getResourceAsStream(deployId, name);
            response.setContentType("application/octet- stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(name, "UTF-8"));

            ServletOutputStream out = response.getOutputStream();
            IOUtils.copy(resourceAsStream, out);
            resourceAsStream.close();
            out.close();
            response.flushBuffer();
        }
    }
}
