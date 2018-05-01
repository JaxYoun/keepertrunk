package com.troy.keeper.activiti.service.impl;

import com.troy.keeper.activiti.domain.ActReDeployment;
import com.troy.keeper.activiti.domain.ActReProcdef;
import com.troy.keeper.activiti.dto.QueryProcDefineDTO;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.repository.ActProcDefineRepository;
import com.troy.keeper.activiti.service.ActDefineService;
import com.troy.keeper.activiti.service.mapper.ActReProcdefMapper;
import com.troy.keeper.core.utils.RedisUtils;
import org.activiti.engine.RepositoryService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * Date:     2017/6/7 14:53<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Service
@Transactional
public class ActDefineServiceImpl implements ActDefineService {
    private Logger log = LoggerFactory.getLogger(ActDefineServiceImpl.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private static final String OPERATION_TYPE_LIST_KEY = "activiti.act_re_procdef.suspension_state_";

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActProcDefineRepository actProcDefineRepository;

    @Autowired
    private ActReProcdefMapper mapper;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public void activate(ActProcessParams param) throws Exception {
        repositoryService.activateProcessDefinitionById(param.getProcDefId(), true, null);
    }

    @Override
    public void suspend(ActProcessParams param) throws Exception {
        repositoryService.suspendProcessDefinitionById(param.getProcDefId(), true, null);
    }

    @Override
    public List<QueryProcDefineDTO> queryProcess(ActProcessParams param) throws Exception {
        List<ActReProcdef> models = actProcDefineRepository.findAll(getProcdefSpec(param));
        List<QueryProcDefineDTO> queryProcDefineDTOS = mapper.objsToDTOs(models);

        String[] listKeys = new String[]{OPERATION_TYPE_LIST_KEY};
        String[] sourceColumns = new String[]{"suspensionState"};
        String[] distColumns = new String[]{"suspensionStateName"};
        redisUtils.getFullValueList(queryProcDefineDTOS, listKeys, sourceColumns, distColumns);
        return queryProcDefineDTOS;
    }

    private Specification<ActReProcdef> getProcdefSpec(final ActProcessParams param) {
        return new Specification<ActReProcdef>() {
                @Override
                public Predicate toPredicate(Root<ActReProcdef> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Join<ActReProcdef, ActReDeployment> joins = root.join("deployment");
                    List<Predicate> predicates = new ArrayList<>();
                    if (!StringUtils.isEmpty(param.getKey())) {
                        predicates.add(criteriaBuilder.like(root.<String>get("key"), "%" + param.getKey() + "%"));
                    }
                    if (!StringUtils.isEmpty(param.getSuspensionState())) {
                        predicates.add(criteriaBuilder.equal(root.<Long>get("suspensionState"), Long.valueOf(param.getSuspensionState())));
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
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(joins.<Instant>get("deployTime"), calendar.getTime().toInstant()));
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
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(joins.<Instant>get("deployTime"), calendar.getTime().toInstant()));
                        } catch (ParseException e) {
                            log.error("日期格式错误");
                        }
                    }

                    Subquery<ActReProcdef> subquery = criteriaQuery.subquery(ActReProcdef.class);
                    Root<ActReProcdef> rootS = subquery.from(ActReProcdef.class);
                    subquery.where(criteriaBuilder.equal(rootS.get("key"), root.get("key")), criteriaBuilder.greaterThan(rootS.get("version"), root.get("version"))).select(rootS);
                    predicates.add(criteriaBuilder.not(criteriaBuilder.exists(subquery)));

                    Order o = criteriaBuilder.desc(joins.<Instant>get("deployTime"));
                    return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(o).getRestriction();
                }
            };
    }

    @Override
    public Page<QueryProcDefineDTO> queryProcessPage(ActProcessParams param, Pageable pageable) throws Exception {
        Page<ActReProcdef> models = actProcDefineRepository.findAll(getProcdefSpec(param), pageable);
        Page<QueryProcDefineDTO> queryProcDefineDTOS = models.map(new Converter<ActReProcdef, QueryProcDefineDTO>() {
            @Override
            public QueryProcDefineDTO convert(ActReProcdef actReProcdef) {
                return mapper.objToDTO(actReProcdef);
            }
        });

        String[] listKeys = new String[]{OPERATION_TYPE_LIST_KEY};
        String[] sourceColumns = new String[]{"suspensionState"};
        String[] distColumns = new String[]{"suspensionStateName"};
        redisUtils.getFullValuePage(queryProcDefineDTOS, listKeys, sourceColumns, distColumns);
        return queryProcDefineDTOS;
    }

}
