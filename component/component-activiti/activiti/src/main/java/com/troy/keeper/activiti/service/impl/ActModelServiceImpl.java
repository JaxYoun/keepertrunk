package com.troy.keeper.activiti.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.troy.keeper.activiti.domain.ActReModel;
import com.troy.keeper.activiti.dto.NewModelDTO;
import com.troy.keeper.activiti.dto.QueryModelDTO;
import com.troy.keeper.activiti.dto.param.ActModelParams;
import com.troy.keeper.activiti.repository.ActModelRepository;
import com.troy.keeper.activiti.service.ActModelService;
import com.troy.keeper.activiti.service.mapper.ActModelMapper;
import com.troy.keeper.activiti.utils.CustomBpmnJsonConverter;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.explorer.util.XmlUtil;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

/**
 * Date:     2017/6/7 14:34<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Service
@Transactional
public class ActModelServiceImpl implements ActModelService {
    private Logger log = LoggerFactory.getLogger(ActModelServiceImpl.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActModelRepository actModelRepository;

    @Autowired
    private ActModelMapper mapper;

    @Override
    public NewModelDTO newModel(ActModelParams param) throws Exception {
        JSONObject editorNode = new JSONObject();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        JSONObject stencilSetNode = new JSONObject();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        JSONObject modelObj = new JSONObject();
        String name = param.getName();
        String description = param.getDescription();
        modelObj.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelObj.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObj.put(ModelDataJsonConstants.MODEL_DESCRIPTION, StringUtils.isEmpty(description) ? "" : description);

        // 新建模型
        Model model = repositoryService.newModel();
        model.setName(name);
        model.setMetaInfo(modelObj.toString());
        // 保存模型
        repositoryService.saveModel(model);
        // 添加附件二进制数据到数据库
        repositoryService.addModelEditorSource(model.getId(),
                editorNode.toString().getBytes("utf-8"));


        String modelId = model.getId();
        NewModelDTO dto = new NewModelDTO();
        dto.setId(modelId);
        return dto;
    }

    @Override
    public void delModel(ActModelParams param) throws Exception {
        repositoryService.deleteModel(param.getId());
    }

    private Specification<ActReModel> getModelSpec(final ActModelParams param) {
        return new Specification<ActReModel>() {
            @Override
            public Predicate toPredicate(Root<ActReModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(param.getId())) {
                    predicates.add(criteriaBuilder.equal(root.<String>get("id"), param.getId()));
                }
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
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Instant>get("createTime"), calendar.getTime().toInstant()));
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
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("createTime"), calendar.getTime().toInstant()));
                    } catch (ParseException e) {
                        log.error("日期格式错误");
                    }
                }

                Order o = criteriaBuilder.desc(root.<Instant>get("createTime"));
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(o).getRestriction();
            }
        };
    }

    @Override
    public List<QueryModelDTO> queryModel(ActModelParams param) throws Exception {
        List<ActReModel> models = actModelRepository.findAll(getModelSpec(param));
        return mapper.objsToDTOs(models);
    }

    @Override
    public Page<QueryModelDTO> queryModelPage(ActModelParams param, Pageable pageable) throws Exception {
        Page<ActReModel> models = actModelRepository.findAll(getModelSpec(param), pageable);
        return models.map(new Converter<ActReModel, QueryModelDTO>() {
            @Override
            public QueryModelDTO convert(ActReModel actReModel) {
                return mapper.objToDTO(actReModel);
            }
        });
    }

    @Override
    public String importModel(MultipartFile file, String fileName) throws Exception {
        String errorMsg = "";
        try {
            if (file != null
                    && (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn"))) {
                XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
                InputStreamReader in =
                        new InputStreamReader(new BufferedInputStream(file.getInputStream()), "UTF-8");
                XMLStreamReader xtr = xif.createXMLStreamReader(in);
                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(xtr);

                if (bpmnModel.getMainProcess() == null
                        || bpmnModel.getMainProcess().getId() == null) {
                    errorMsg = "导入模型失败！";
                } else {

                    if (bpmnModel.getLocationMap().size() == 0) {
                        errorMsg = "模型文件错误：没有位置信息！";
                    } else {

                        String processName = null; // 取名字 优先name 没有则取id
                        if (org.apache.commons.lang3.StringUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
                            processName = bpmnModel.getMainProcess().getName();
                        } else {
                            processName = bpmnModel.getMainProcess().getId();
                        }

                        // 新建模型
                        Model modelData = repositoryService.newModel();
                        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
                        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processName);
                        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
                        modelData.setMetaInfo(modelObjectNode.toString());
                        modelData.setName(processName);
                        // 保存模型
                        repositoryService.saveModel(modelData);

                        CustomBpmnJsonConverter jsonConverter = new CustomBpmnJsonConverter();
                        ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);
                        // 添加附件二进制数据到数据库
                        repositoryService.addModelEditorSource(modelData.getId(), editorNode
                                .toString().getBytes("utf-8"));
                    }
                }
            } else {
                errorMsg = "文件类型或内容错误！";
            }
        } catch (Exception e) {
            String msg = e.getMessage().replace(System.getProperty("line.separator"), "<br/>");
            errorMsg = "导入模型失败！：" + msg;
        }
        return errorMsg;
    }

    @Override
    public void exportModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String modelId = request.getParameter("id");
        if(!StringUtils.isEmpty(modelId)) {
            Model modelData = repositoryService.getModel(modelId);
            CustomBpmnJsonConverter jsonConverter = new CustomBpmnJsonConverter();
            JsonNode editorNode =
                    new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData
                            .getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bytes = xmlConverter.convertToXML(bpmnModel);

            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setContentType("application/octet- stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);

            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ServletOutputStream out = response.getOutputStream();
            IOUtils.copy(in, out);
            in.close();
            out.close();
            response.flushBuffer();
        }
    }

    @Override
    public String deployModel(ActModelParams param) throws Exception {
        String errorMsg = "";
        try {
            Model modelData = repositoryService.getModel(param.getId());
            ObjectNode modelNode =
                    (ObjectNode) new ObjectMapper().readTree(repositoryService
                            .getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;

            BpmnModel model = new CustomBpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model, "UTF-8");

            String processName = modelData.getName() + ".bpmn20.xml";
            repositoryService
                            .createDeployment()
                            .name(modelData.getName())
                            .addString(processName, new String(bpmnBytes, Charset.forName("UTF-8")))
                            .deploy();
        } catch (Exception e) {
            errorMsg = "根据模型部署流程失败：modelId=" + param.getId();
        }
        return errorMsg;
    }
}
