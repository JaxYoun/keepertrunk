package com.troy.keeper.activiti.service.impl;

import com.troy.keeper.activiti.domain.BussRelatedProcess;
import com.troy.keeper.activiti.domain.TaskClaimRecord;
import com.troy.keeper.activiti.domain.ToDoTask;
import com.troy.keeper.activiti.dto.ActivityDTO;
import com.troy.keeper.activiti.dto.HistoryTaskDTO;
import com.troy.keeper.activiti.dto.HistroyFlowDTO;
import com.troy.keeper.activiti.dto.ProcessTackingDTO;
import com.troy.keeper.activiti.dto.StartProcessDTO;
import com.troy.keeper.activiti.dto.ToDoTaskDTO;
import com.troy.keeper.activiti.dto.UserDTO;
import com.troy.keeper.activiti.dto.param.ActProcessDoTaskParams;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.dto.param.ActProcessStartParams;
import com.troy.keeper.activiti.dto.param.ActTaskParams;
import com.troy.keeper.activiti.repository.ActRuIdentitylinkRepository;
import com.troy.keeper.activiti.repository.BussRelatedRepository;
import com.troy.keeper.activiti.repository.HistoryTaskRepository;
import com.troy.keeper.activiti.repository.TaskClaimRecordRepository;
import com.troy.keeper.activiti.repository.ToDoTaskRepository;
import com.troy.keeper.activiti.service.ActTaskService;
import com.troy.keeper.activiti.service.mapper.HistoryTaskMapper;
import com.troy.keeper.activiti.service.mapper.ToDoTaskMapper;
import com.troy.keeper.activiti.utils.DateTiimeUtil;
import com.troy.keeper.activiti.utils.bpm.cmd.HistoryProcessInstanceDiagramCmd;
import com.troy.keeper.activiti.utils.jump.cmd.CustomJumpCommand;
import com.troy.keeper.activiti.web.rest.errors.TroyActivitiException;
import com.troy.keeper.core.base.repository.CommonRepository;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.utils.RedisUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Date:     2017/6/7 14:52<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Service
@Transactional
public class ActTaskServiceImpl extends BaseServiceImpl implements ActTaskService {
    private Logger log = LoggerFactory.getLogger(ActTaskServiceImpl.class);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private BussRelatedRepository bussRelatedRepository;

    @Autowired
    private TaskClaimRecordRepository taskClaimRecordRepository;

    @Autowired
    private ActRuIdentitylinkRepository actRuIdentitylinkRepository;

    @Autowired
    private ToDoTaskRepository toDoTaskRepository;

    @Autowired
    private ToDoTaskMapper toDoTaskMapper;

    @Autowired
    private HistoryTaskRepository historyTaskRepository;

    @Autowired
    private HistoryTaskMapper historyTaskMapper;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 流程资源服务
     */
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程运行时服务 用于查询运行时流程信息
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程任务服务
     */
    @Autowired
    private TaskService taskService;

    /**
     * 流程历史服务
     */
    @Autowired
    private HistoryService historyService;

    /**
     * 流程用户服务
     */
    @Autowired
    private IdentityService identityService;

    @Override
    public boolean claim(ActTaskParams param) throws Exception {
        return claim(param.getProcInstId(), param.getTaskId(), param.getUserId());
    }

    @Override
    public StartProcessDTO startProcess(ActProcessStartParams param) throws Exception {
        identityService.setAuthenticatedUserId(param.getStartUser());
        ProcessInstance procIns = null;
        Map<String, Object> startValues = param.getStartValues();
        if (startValues == null) {
            startValues = new HashMap<>();
        }
        // 流程启动
        // 通过流程定义key启动 从空开始节点启动
        if ("0".equals(param.getProcessStartType())) {
            procIns = runtimeService.startProcessInstanceByKey(param.getProcessKey(), startValues);
            // 通过流程开始message启动 从指定的开始节点启动
        } else if ("1".equals(param.getProcessStartType())) {
            procIns = runtimeService.startProcessInstanceByMessage(param.getStartMessage(), startValues);
        }

        if (procIns != null) {
            com.troy.keeper.activiti.dto.param.BussRelatedProcess bussRelatedProcessParam = param.getBussRelatedProcess();
            BussRelatedProcess bussRelatedProcess = new BussRelatedProcess();
            bussRelatedProcess.setBusinessId(bussRelatedProcessParam.getBusinessId());
            bussRelatedProcess.setBusinessNo(bussRelatedProcessParam.getBusinessNo());
            bussRelatedProcess.setBusinessType(bussRelatedProcessParam.getBusinessType());
            bussRelatedProcess.setTitle(bussRelatedProcessParam.getTitle());
            bussRelatedProcess.setProcInstId(procIns.getId());
            bussRelatedProcess.setAuthor(param.getStartUser());
            try {
                bussRelatedRepository.save(bussRelatedProcess);
            } catch (Exception e) {
                throw new TroyActivitiException("业务ID重复：" + bussRelatedProcess.getBusinessId());
            }

        } else {
            throw new TroyActivitiException("process start error");
        }

        // 自动完成第一个任务
        if ("1".equals(param.getAutoDoFirstTask())) {
            // 首个任务，如果为单个候选人则直接认领
            singleUserClaim(procIns.getId(), param.getStartUser());
            completeTask(procIns.getId(), null, param.getStartUser(), param.getStartProcessComment(), startValues, null);
        }

        // 下一个任务，如果为单个候选人则直接认领
        singleUserClaim(procIns.getId(), "");

        StartProcessDTO dto = new StartProcessDTO();
        dto.setProcInstId(procIns.getId());
        return dto;
    }

    @Override
    public void doTask(ActProcessDoTaskParams param) throws Exception {
        // 完成任务
        completeTask(param.getProcInstId(), param.getTaskId(), param.getTaskOprateUser(), param.getTaskComment(), param.getTaskValues(), param.getTargetTaskKeys());
        // 下一个任务，如果为单个候选人则直接认领
        singleUserClaim(param.getProcInstId(), param.getAutoClaimUser());
        // 如果完成流程则删除TaskClaimRecord表余留数据
        deleteTaskClaimRecord(param.getProcInstId());
    }

    private void deleteTaskClaimRecord(String procInstId) {
        List<HistoricProcessInstance> histProIns =
                historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(procInstId)
                        .orderByProcessInstanceStartTime().asc().list();
        if (histProIns.size() > 0) {
            if (!StringUtils.isEmpty(histProIns.get(0).getEndTime())) {
                taskClaimRecordRepository.deleteByProcInstId(procInstId);
            }
        }
    }

    private Specification<ToDoTask> getTodoListSpec(ActTaskParams param) throws Exception {
        return new Specification<ToDoTask>() {
            @Override
            public Predicate toPredicate(Root<ToDoTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(param.getBusinessType())) {
                    predicates.add(cb.equal(root.<String>get("businessType"), param.getBusinessType()));
                }
                if (null != param.getBusinessId() && param.getBusinessId().size() > 0) {
                    predicates.add(root.<String>get("businessId").in(param.getBusinessId()));
                }
                if (!StringUtils.isEmpty(param.getBusinessNo())) {
                    predicates.add(cb.like(root.<String>get("businessNo"), "%" + param.getBusinessNo() + "%"));
                }
                if (!StringUtils.isEmpty(param.getTitle())) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + param.getTitle() + "%"));
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
                        predicates.add(cb.greaterThanOrEqualTo(root.<Instant>get("createTime"), calendar.getTime().toInstant()));
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
                        predicates.add(cb.lessThanOrEqualTo(root.<Instant>get("createTime"), calendar.getTime().toInstant()));
                    } catch (ParseException e) {
                        log.error("日期格式错误");
                    }
                }
                if(param.getUserId().indexOf(",") > 0 && param.getPostId().indexOf(",") > 0){
                    predicates.add(cb.or(root.<String>get("assignee").in(Arrays.asList(param.getUserId().split(","))), root.<String>get("candidateUserId").in(Arrays.asList(param.getUserId().split(","))), root.<String>get("candidateGroupId").in(Arrays.asList(param.getPostId().split(",")))));
                } else if(param.getUserId().indexOf(",") > 0 && param.getPostId().indexOf(",") < 0){
                    predicates.add(cb.or(root.<String>get("assignee").in(Arrays.asList(param.getUserId().split(","))), root.<String>get("candidateUserId").in(Arrays.asList(param.getUserId().split(","))), cb.equal(root.<String>get("candidateGroupId"), param.getPostId())));
                } else if(param.getUserId().indexOf(",") < 0 && param.getPostId().indexOf(",") > 0){
                    predicates.add(cb.or(cb.equal(root.<String>get("assignee"), param.getUserId()), cb.equal(root.<String>get("candidateUserId"), param.getUserId()), root.<String>get("candidateGroupId").in(Arrays.asList(param.getPostId().split(",")))));
                } else {
                    predicates.add(cb.or(cb.equal(root.<String>get("assignee"), param.getUserId()), cb.equal(root.<String>get("candidateUserId"), param.getUserId()), cb.equal(root.<String>get("candidateGroupId"), param.getPostId())));
                }

                Order o = cb.desc(root.<Instant>get("createTime"));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(o).getRestriction();
            }
        };
    }

    @Override
    public List<ToDoTaskDTO> todoList(ActTaskParams param) throws Exception {
        List<ToDoTask> toDoTasks = toDoTaskRepository.findAll(getTodoListSpec(param));
        List<ToDoTaskDTO> toDoTaskDTOS = toDoTaskMapper.objsToDTOs(toDoTasks);

        String[] userSourceColumns = new String[]{"userId", "author"};
        String[] userDistColumns = new String[]{"userName", "authorName"};

        redisUtils.getUserNameList(toDoTaskDTOS, userSourceColumns, userDistColumns);
        return toDoTaskDTOS;
    }

    @Override
    public Page<ToDoTaskDTO> todoListPage(ActTaskParams param, Pageable pageable) throws Exception {
        Page<ToDoTask> toDoTasks = toDoTaskRepository.findAll(getTodoListSpec(param), pageable);
        Page<ToDoTaskDTO> toDoTaskDTOS = toDoTasks.map(new Converter<ToDoTask, ToDoTaskDTO>() {
            @Override
            public ToDoTaskDTO convert(ToDoTask toDoTask) {
                return toDoTaskMapper.objToDTO(toDoTask);
            }
        });
        String[] userSourceColumns = new String[]{"userId", "author"};
        String[] userDistColumns = new String[]{"userName", "authorName"};

        redisUtils.getUserNamePage(toDoTaskDTOS, userSourceColumns, userDistColumns);
        return toDoTaskDTOS;
    }

    private Query getHistoryTaskCountQuery(ActTaskParams param) throws Exception {
        StringBuilder sb = new StringBuilder();
        LinkedHashMap<Integer, Object> params = new LinkedHashMap<>();
        sb.append("SELECT COUNT(DISTINCT h.procInstId)\n" +
                "  FROM HistoryTask h \n" +
                " WHERE 1=1");
        if (!StringUtils.isEmpty(param.getBusinessType())) {
            sb.append(" AND h.businessType = ?1 ");
            params.put(1, param.getBusinessType());
        }
        if (null != param.getBusinessId() && param.getBusinessId().size() > 0) {
            sb.append(" AND h.businessId IN (?2) ");
            params.put(2, param.getBusinessId());
        }
        if (!StringUtils.isEmpty(param.getBusinessNo())) {
            sb.append(" AND h.businessNo like ?3 ");
            params.put(3, "%" + param.getBusinessNo() + "%");
        }
        if (!StringUtils.isEmpty(param.getTitle())) {
            sb.append(" AND h.title like ?4 ");
            params.put(4, "%" + param.getTitle() + "%");
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
                sb.append(" AND h.startTime >= ?5 ");
                params.put(5, calendar.getTime().toInstant());
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
                sb.append(" AND h.startTime <= ?6 ");
                params.put(6, calendar.getTime().toInstant());
            } catch (ParseException e) {
                log.error("日期格式错误");
            }
        }

        if (!StringUtils.isEmpty(param.getUserId())) {
            if(param.getUserId().indexOf(",") > 0){
                sb.append(" AND h.participantUserId in (?7) ");
                params.put(7, Arrays.asList(param.getUserId().split(",")));
            } else {
                sb.append(" AND h.participantUserId = ?7 ");
                params.put(7, param.getUserId());
            }
        }
        Query query = entityManager.createQuery(sb.toString());
        for (Map.Entry<Integer, Object> paramInfo :params.entrySet()) {
            query.setParameter(paramInfo.getKey(), paramInfo.getValue());
        }
        return query;
    }

    private Query getHistoryTaskListQuery(ActTaskParams param) throws Exception {
        StringBuilder sb = new StringBuilder();
        LinkedHashMap<Integer, Object> params = new LinkedHashMap<>();
        sb.append("SELECT DISTINCT h.procInstId ,\n" +
                "                h.procDefId , \n" +
                "                h.startTime , \n" +
                "                h.endTime , \n" +
                "                h.duration , \n" +
                "                h.procDefName , \n" +
                "                h.version , \n" +
                "                h.taskId , \n" +
                "                h.taskDefKey , \n" +
                "                h.taskDefName , \n" +
                "                h.assignee , \n" +
                "                h.businessId , \n" +
                "                h.businessNo , \n" +
                "                h.businessType , \n" +
                "                h.title , \n" +
                "                h.author  \n" +
                "  FROM HistoryTask h \n" +
                " WHERE 1=1");
                if (!StringUtils.isEmpty(param.getBusinessType())) {
                    sb.append(" AND h.businessType = ?1 ");
                    params.put(1, param.getBusinessType());
                }
                if (null != param.getBusinessId() && param.getBusinessId().size() > 0) {
                    sb.append(" AND h.businessId IN (?2) ");
                    params.put(2, param.getBusinessId());
                }
                if (!StringUtils.isEmpty(param.getBusinessNo())) {
                    sb.append(" AND h.businessNo like ?3 ");
                    params.put(3, "%" + param.getBusinessNo() + "%");
                }
                if (!StringUtils.isEmpty(param.getTitle())) {
                    sb.append(" AND h.title like ?4 ");
                    params.put(4, "%" + param.getTitle() + "%");
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
                        sb.append(" AND h.startTime >= ?5 ");
                        params.put(5, calendar.getTime().toInstant());
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
                        sb.append(" AND h.startTime <= ?6 ");
                        params.put(6, calendar.getTime().toInstant());
                    } catch (ParseException e) {
                        log.error("日期格式错误");
                    }
                }
                if (!StringUtils.isEmpty(param.getUserId())) {
                    if(param.getUserId().indexOf(",") > 0){
                        sb.append(" AND h.participantUserId in (?7) ");
                        params.put(7, Arrays.asList(param.getUserId().split(",")));
                    } else {
                        sb.append(" AND h.participantUserId = ?7 ");
                        params.put(7, param.getUserId());
                    }
                }

                sb.append("ORDER BY h.startTime DESC");
        Query query = entityManager.createQuery(sb.toString());
        for (Map.Entry<Integer, Object> paramInfo :params.entrySet()) {
            query.setParameter(paramInfo.getKey(), paramInfo.getValue());
        }
        return query;
    }

    @Override
    public List<HistoryTaskDTO> historyTaskList(ActTaskParams param) throws Exception {
        List<Object[]> resultList = getHistoryTaskListQuery(param).getResultList();
        List<HistoryTaskDTO> collect = resultList.stream().map(objects -> {
            HistoryTaskDTO dto = new HistoryTaskDTO();
            dto.setProcInstId(objects[0] != null ? String.valueOf(objects[0]) : null);
            dto.setProcdefId(objects[1] != null ? String.valueOf(objects[1]) : null);
            dto.setCreateTime(objects[2] != null ? (Instant) (objects[2]) : null);
            dto.setEndTime(objects[3] != null ? (Instant) (objects[3]) : null);
            dto.setDuration(DateTiimeUtil.getDateTimeString(objects[4] != null ? Long.valueOf(String.valueOf(objects[4])) : 0L));
            dto.setProcdefName(objects[5] != null ? String.valueOf(objects[5]) : null);
            dto.setVersion(objects[6] != null ? String.valueOf(objects[6]) : null);
            dto.setActId(objects[7] != null ? String.valueOf(objects[7]) : null);
            dto.setActKey(objects[8] != null ? String.valueOf(objects[8]) : null);
            dto.setActName(objects[9] != null ? String.valueOf(objects[9]) : null);
            dto.setUserId(objects[10] != null ? String.valueOf(objects[10]) : null);
            dto.setBusinessId(objects[11] != null ? String.valueOf(objects[11]) : null);
            dto.setBusinessNo(objects[12] != null ? String.valueOf(objects[12]) : null);
            dto.setBusinessType(objects[13] != null ? String.valueOf(objects[13]) : null);
            dto.setTitle(objects[14] != null ? String.valueOf(objects[14]) : null);
            dto.setAuthor(objects[15] != null ? String.valueOf(objects[15]) : null);
            return dto;
        }).collect(Collectors.toList());

        String[] userSourceColumns = new String[]{"userId", "author"};
        String[] userDistColumns = new String[]{"userName", "authorName"};

        redisUtils.getUserNameList(collect, userSourceColumns, userDistColumns);

        return collect;
    }

    @Override
    public Page<HistoryTaskDTO> historyTaskListPage(ActTaskParams param, Pageable pageable) throws Exception {
        long count = (long)getHistoryTaskCountQuery(param).getSingleResult();
        List<Object[]> resultList = getHistoryTaskListQuery(param).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        Page<Object[]> historyTasks = new PageImpl<Object[]>(resultList, pageable, count);

        Page<HistoryTaskDTO> historyTaskDTOS = historyTasks.map(new Converter<Object[], HistoryTaskDTO>() {
            @Override
            public HistoryTaskDTO convert(Object[] objects) {
                HistoryTaskDTO dto = new HistoryTaskDTO();
                dto.setProcInstId(objects[0] != null ? String.valueOf(objects[0]) : null);
                dto.setProcdefId(objects[1] != null ? String.valueOf(objects[1]) : null);
                dto.setCreateTime(objects[2] != null ? (Instant) (objects[2]) : null);
                dto.setEndTime(objects[3] != null ? (Instant) (objects[3]) : null);
                dto.setDuration(DateTiimeUtil.getDateTimeString(objects[4] != null ? Long.valueOf(String.valueOf(objects[4])) : 0L));
                dto.setProcdefName(objects[5] != null ? String.valueOf(objects[5]) : null);
                dto.setVersion(objects[6] != null ? String.valueOf(objects[6]) : null);
                dto.setActId(objects[7] != null ? String.valueOf(objects[7]) : null);
                dto.setActKey(objects[8] != null ? String.valueOf(objects[8]) : null);
                dto.setActName(objects[9] != null ? String.valueOf(objects[9]) : null);
                dto.setUserId(objects[10] != null ? String.valueOf(objects[10]) : null);
                dto.setBusinessId(objects[11] != null ? String.valueOf(objects[11]) : null);
                dto.setBusinessNo(objects[12] != null ? String.valueOf(objects[12]) : null);
                dto.setBusinessType(objects[13] != null ? String.valueOf(objects[13]) : null);
                dto.setTitle(objects[14] != null ? String.valueOf(objects[14]) : null);
                dto.setAuthor(objects[15] != null ? String.valueOf(objects[15]) : null);
                return dto;
            }
        });

        String[] userSourceColumns = new String[]{"userId", "author"};
        String[] userDistColumns = new String[]{"userName", "authorName"};

        redisUtils.getUserNamePage(historyTaskDTOS, userSourceColumns, userDistColumns);
        return historyTaskDTOS;
    }

    @Override
    public ProcessTackingDTO getProcessTracking(ActTaskParams param) throws Exception {
        ProcessTackingDTO dto = new ProcessTackingDTO();
        // 流程图
        Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
                param.getProcInstId());
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
        int rc = 0;
        while ((rc = is.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        dto.setFlowPic(Base64Utils.encodeToString(swapStream.toByteArray()));

        List<HistroyFlowDTO> histroyFlows = getHistroyFlowDTO(param.getProcInstId());
        dto.setHistory(histroyFlows);

        String[] userSourceColumns = new String[]{"userId"};
        String[] userDistColumns = new String[]{"userName"};

        redisUtils.getUserNameList(histroyFlows, userSourceColumns, userDistColumns);
        return dto;
    }

    private  List<HistroyFlowDTO> getHistroyFlowDTO(String procInstId) {
        // 流程跟踪
        List<HistroyFlowDTO> histroyFlows = new ArrayList<>();
        List<HistoricActivityInstance> list =
                historyService.createHistoricActivityInstanceQuery().processInstanceId(procInstId)
                        .orderByHistoricActivityInstanceStartTime().asc()
                        .orderByHistoricActivityInstanceEndTime().asc().list();
        for (HistoricActivityInstance histIns : list) {
            if (!StringUtils.isEmpty(histIns.getTaskId()) || "startEvent".equals(histIns.getActivityType()) || "endEvent".equals(histIns.getActivityType())) {
                HistroyFlowDTO histroyFlow = new HistroyFlowDTO();
                histroyFlow.setActId(histIns.getTaskId());
                histroyFlow.setActKey(histIns.getActivityId());
                histroyFlow.setActName(histIns.getActivityName());
                histroyFlow.setUserId(histIns.getAssignee());
                histroyFlow.setStartTime(histIns.getStartTime() != null ? histIns.getStartTime().toInstant() : null);
                histroyFlow.setEndTime(histIns.getEndTime() != null ? histIns.getEndTime().toInstant() : null);
                histroyFlow.setDuration(DateTiimeUtil.getDateTimeString(histIns.getDurationInMillis() != null ? histIns.getDurationInMillis() : 0L));
                // 获取意见评论内容
                if (!StringUtils.isEmpty(histIns.getTaskId())) {
                    List<Comment> commentList = taskService.getTaskComments(histIns.getTaskId());
                    if (commentList.size() > 0) {
                        histroyFlow.setComment(commentList.get(0).getFullMessage());
                    }
                }
                // 流程开始人
                if ("startEvent".equals(histIns.getActivityType()) && StringUtils.isEmpty(histIns.getAssignee())) {
                    List<HistoricProcessInstance> histProIns =
                            historyService.createHistoricProcessInstanceQuery()
                                    .processInstanceId(procInstId)
                                    .orderByProcessInstanceStartTime().asc().list();
                    if (histProIns.size() > 0) {
                        if (!StringUtils.isEmpty(histProIns.get(0).getStartUserId())) {
                            histroyFlow.setUserId(histProIns.get(0).getStartUserId());
                        }
                    }
                }
                histroyFlows.add(histroyFlow);
            }
        }
            return histroyFlows;
    }


    @Override
    public List<ProcessTackingDTO> getProcessTrackingList(ActTaskParams param) throws Exception {
        List<ProcessTackingDTO> resultList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<Object> params = new ArrayList<>();
        int i = 0;
        sb.append("select b from BussRelatedProcess b where 1=1");
        if(!StringUtils.isEmpty(param.getProcInstId())){
            sb.append(" and b.procInstId = ?").append(i++);
            params.add(param.getProcInstId());
        }
        if(!CollectionUtils.isEmpty(param.getBusinessId())){
            sb.append(" and b.businessId in (?").append(i++).append(")");
            params.add(param.getBusinessId());
        }
        if(!StringUtils.isEmpty(param.getBusinessType())){
            sb.append(" and b.businessType = ?").append(i++);
            params.add(param.getBusinessType());
        }
        if(!StringUtils.isEmpty(param.getBusinessNo())){
            sb.append(" and b.businessNo = ?").append(i++);
            params.add(param.getBusinessNo());
        }
        if(!StringUtils.isEmpty(param.getTitle())){
            sb.append(" and b.title like ?").append(i++);
            params.add("%" + param.getTitle() + "%");
        }
        sb.append(" order by b.createdDate desc");
        List<BussRelatedProcess> result = commonRepository.findListByHql(sb.toString(), params.toArray());
        if(!CollectionUtils.isEmpty(result)){
            String[] userSourceColumns = new String[]{"userId"};
            String[] userDistColumns = new String[]{"userName"};
            List<ProcessTackingDTO> collect = result.stream().map(bussRelatedProcess ->
                    {
                        ProcessTackingDTO processTackingDTO = new ProcessTackingDTO();
                        processTackingDTO.setBusinessId(bussRelatedProcess.getBusinessId());
                        processTackingDTO.setBusinessNo(bussRelatedProcess.getBusinessNo());
                        processTackingDTO.setBusinessType(bussRelatedProcess.getBusinessType());
                        processTackingDTO.setProcInstId(bussRelatedProcess.getProcInstId());

                        List<HistroyFlowDTO> histroyFlows = getHistroyFlowDTO(bussRelatedProcess.getProcInstId());
                        processTackingDTO.setHistory(histroyFlows);

                        try {
                            redisUtils.getUserNameList(histroyFlows, userSourceColumns, userDistColumns);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        return processTackingDTO;
                    }
            ).collect(Collectors.toList());

            return collect;
        }

        return null;
    }

    @Override
    public List<UserDTO> getTaskCandidates(ActTaskParams param) throws Exception{
        List<String> userIds = actRuIdentitylinkRepository.queryTaskSingleTodoUser(param.getTaskId());
        if(!CollectionUtils.isEmpty(userIds)){
            List<UserDTO> collect = userIds.stream().map(userId -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(userId);
                return userDTO;
            }).collect(Collectors.toList());

            String[] userSourceColumns = new String[]{"userId"};
            String[] userDistColumns = new String[]{"userName"};

            redisUtils.getUserNameList(collect, userSourceColumns, userDistColumns);
            return collect;
        }
        return null;
    }


    @Override
    public void delProcessInst(ActProcessParams param) throws Exception {
        BussRelatedProcess bussRelatedProcess = bussRelatedRepository.findByIdAndType(param.getBusinessId(), param.getBusinessType());
        if (bussRelatedProcess != null) {
            bussRelatedRepository.delete(bussRelatedProcess);
            if (!StringUtils.isEmpty(bussRelatedProcess.getProcInstId())) {
                taskClaimRecordRepository.deleteByProcInstId(bussRelatedProcess.getProcInstId());

                // 先删除运行实例 再删除历史数据
                if (!CollectionUtils.isEmpty(runtimeService.createProcessInstanceQuery().processInstanceId(bussRelatedProcess.getProcInstId()).list())) {
                    runtimeService.deleteProcessInstance(bussRelatedProcess.getProcInstId(), "deleteProcess by user");
                }
                if (!CollectionUtils.isEmpty(historyService.createHistoricProcessInstanceQuery().processInstanceId(bussRelatedProcess.getProcInstId()).list())) {
                    historyService.deleteHistoricProcessInstance(bussRelatedProcess.getProcInstId());
                }
            }
        }
    }

    @Override
    public String getLastOperateUser(ActTaskParams param) throws Exception {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();

        historicTaskInstanceQuery.processInstanceId(param.getProcInstId());
        if(!StringUtils.isEmpty(param.getTaskDefId())){
            historicTaskInstanceQuery.taskDefinitionKey(param.getTaskDefId());
        }
        List<HistoricTaskInstance> list = historicTaskInstanceQuery .finished()
                        .orderByHistoricTaskInstanceEndTime().desc().list();
        if(!CollectionUtils.isEmpty(list)) {
            return list.get(0).getAssignee();
        }
        return null;
    }

    @Override
    public List<ActivityDTO> getActivities(ActTaskParams param) throws Exception {
        List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(param.getProcInstId()).list();
        List<String> activityIdList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(executionList)){
            for(Execution execution:executionList){
                activityIdList.add(execution.getActivityId());
            }
        }
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(param.getProcDefId());
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
        Set<ActivityDTO> taskDefinitionSet = new HashSet<>();
        for(ActivityImpl activityImpl:activitiList){
            if(activityIdList.contains(activityImpl.getId())) {
                if ("userTask".equals(activityImpl.getProperty("type"))) {
                    List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
                    if (!CollectionUtils.isEmpty(outTransitions)) {
                        for (PvmTransition pvmTransition : outTransitions) {
                            PvmActivity destination = pvmTransition.getDestination();
                            Object type = destination.getProperty("type");
                            Object name = destination.getProperty("name");
                            if ("userTask".equals(type)){
                                ActivityDTO activityDTO = new ActivityDTO();
                                activityDTO.setId(destination.getId());
                                activityDTO.setName(name.toString());
                                taskDefinitionSet.add(activityDTO);
                            } else if(type != null && type.toString().endsWith("Gateway")){
                                List<PvmTransition> sOut = destination.getOutgoingTransitions();
                                if (!CollectionUtils.isEmpty(sOut)) {
                                    for (PvmTransition sPvmTransition : sOut) {
                                        PvmActivity sDestination = sPvmTransition.getDestination();
                                        Object sType = sDestination.getProperty("type");
                                        Object sName = sDestination.getProperty("name");
                                        if ("userTask".equals(sType)) {
                                            ActivityDTO activityDTO = new ActivityDTO();
                                            activityDTO.setId(sDestination.getId());
                                            activityDTO.setName(sName.toString());
                                            taskDefinitionSet.add(activityDTO);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(taskDefinitionSet);
    }

    /**
     * 撤销当前环节，恢复到上一环节
     *
     * @param param
     */
    @Override
    public void callBackProcess(ActProcessDoTaskParams param) throws Exception {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(param.getProcInstId()).finished().orderByHistoricTaskInstanceEndTime().desc().list();
        String preTaskDefId = "";
        String preTaskId = "";
        String executionId = "";

        if(!CollectionUtils.isEmpty(list)){
            preTaskDefId = list.get(0).getTaskDefinitionKey();
            preTaskId = list.get(0).getId();
            executionId = list.get(0).getExecutionId();
        }

        Query query = entityManager.createNativeQuery("SELECT T.ID_ FROM ACT_RU_TASK T WHERE T.ID_ = ?1");
        query.setParameter("1", param.getTaskId());
        List result = query.getResultList();
        if(CollectionUtils.isEmpty(result)){
           throw new TroyActivitiException("流程撤回失败");
        }

        // 当前运行环节数据清除
        query = entityManager.createNativeQuery("DELETE FROM ACT_RU_IDENTITYLINK T WHERE T.TASK_ID_ = ?1");
        query.setParameter("1", param.getTaskId());
        query.executeUpdate();
        query = entityManager.createNativeQuery("DELETE FROM ACT_RU_VARIABLE T WHERE T.TASK_ID_ = ?1");
        query.setParameter("1", param.getTaskId());
        query.executeUpdate();
        query = entityManager.createNativeQuery("DELETE FROM ACT_RU_TASK T WHERE T.ID_ = ?1");
        query.setParameter("1", param.getTaskId());
        query.executeUpdate();
        query = entityManager.createNativeQuery("UPDATE ACT_RU_EXECUTION T SET T.REV_ = T.REV_ - 1, T.ACT_ID_ = ?1  WHERE T.ID_ = ?2");
        query.setParameter("1", preTaskDefId);
        query.setParameter("2", executionId);
        query.executeUpdate();

        // 当前运行环节历史数据清除
        query = entityManager.createNativeQuery("DELETE FROM ACT_HI_IDENTITYLINK T WHERE T.TASK_ID_ = ?1");
        query.setParameter("1", param.getTaskId());
        query.executeUpdate();
        query = entityManager.createNativeQuery("DELETE FROM ACT_HI_VARINST T WHERE T.TASK_ID_ = ?1");
        query.setParameter("1", param.getTaskId());
        query.executeUpdate();
        query = entityManager.createNativeQuery("DELETE FROM ACT_HI_TASKINST T WHERE T.ID_ = ?1");
        query.setParameter("1", param.getTaskId());
        query.executeUpdate();

        // 前一环节历史数据还原
        query = entityManager.createNativeQuery("UPDATE ACT_HI_TASKINST T SET T.END_TIME_ = NULL, T.DURATION_ = NULL, T.DELETE_REASON_ = NULL WHERE T.ID_ = ?1");
        query.setParameter("1", preTaskId);
        query.executeUpdate();
        query = entityManager.createNativeQuery("DELETE FROM ACT_HI_COMMENT T WHERE T.TASK_ID_ = ?1");
        query.setParameter("1", preTaskId);
        query.executeUpdate();

        // 当前环节历史流转数据删除
        query = entityManager.createNativeQuery("DELETE FROM ACT_HI_ACTINST T WHERE T.START_TIME_ >= (SELECT START_TIME_ FROM ACT_HI_ACTINST  WHERE TASK_ID_ = ?1) AND T.PROC_INST_ID_ = ?2 AND T.ACT_ID_ <> ?3");
        query.setParameter("1", preTaskId);
        query.setParameter("2", param.getProcInstId());
        query.setParameter("3", preTaskDefId);
        query.executeUpdate();

        // 前一环节历史流转数据还原
        query = entityManager.createNativeQuery("UPDATE ACT_HI_ACTINST T SET T.END_TIME_ = NULL, T.DURATION_ = NULL WHERE T.TASK_ID_ = ?1");
        query.setParameter("1", preTaskId);
        query.executeUpdate();

        // 前一环节运行时任务数据还原
        query = entityManager.createNativeQuery("INSERT INTO ACT_RU_TASK T( T.ID_, T.REV_, T.EXECUTION_ID_, T.PROC_INST_ID_, T.PROC_DEF_ID_, T.NAME_, T.DESCRIPTION_, T.TASK_DEF_KEY_, T.ASSIGNEE_, T.PRIORITY_ , T.CREATE_TIME_, T.SUSPENSION_STATE_ ) SELECT T1.ID_, 2, T1.EXECUTION_ID_, T1.PROC_INST_ID_, T1.PROC_DEF_ID_, T1.NAME_, T1.DESCRIPTION_, T1.TASK_DEF_KEY_, T1.ASSIGNEE_, T1.PRIORITY_ , T1.START_TIME_, 1 FROM ACT_HI_TASKINST T1 WHERE T1.ID_ = ?1");
        query.setParameter("1", preTaskId);
        query.executeUpdate();

    }

    /**
     * 退回上一环节
     *
     * @param param
     */
    @Override
    public void sendBackProcess(ActProcessDoTaskParams param) throws Exception {
        ActTaskParams actTaskParam = new ActTaskParams();
        actTaskParam.setProcInstId(param.getProcInstId());
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(param.getProcInstId()).finished().orderByHistoricTaskInstanceEndTime().desc().list();
        String taskDefId = "";
        if(!CollectionUtils.isEmpty(list)){
            taskDefId = list.get(0).getTaskDefinitionKey();
        }
        // 上一环节名称
        actTaskParam.setTaskDefId(taskDefId);
        // 获取上一步处理人
        String lastOperateUser = getLastOperateUser(actTaskParam);

        // 强制跳转到上一环节
        param.setTargetTaskKeys(Arrays.asList(taskDefId));
        // 自动签收人
        param.setAutoClaimUser(lastOperateUser);
        doTask(param);
    }


    /**
     * 完成任务
     *
     * @param procInsId  流程实例
     * @param userId     任务用户
     * @param comment    任务备注
     * @param taskValues 任务所需变量
     * @return task 任务
     */
    private Task completeTask(String procInsId, String taskId, String userId, String comment, Map<String, Object> taskValues, List<String> targetTaskKeys) throws TroyActivitiException {
        Task task = getTask(procInsId, taskId, userId);
        if (task != null) {
            //备注
            if (!StringUtils.isEmpty(comment)) {
                taskService.addComment(task.getId(), procInsId, comment);
            }
            if (taskValues == null) {
                taskValues = new HashMap<>();
            }
            //完成任务
            if(!CollectionUtils.isEmpty(targetTaskKeys)){
                customJump(procInsId, task.getExecutionId(), targetTaskKeys, taskValues);
            } else {
                taskService.complete(task.getId(), taskValues);
            }
            return task;
        } else {
            throw new TroyActivitiException("没有找到代办任务");
        }
    }

    private void customJump(String procInsId, String executionId, List<String> targetTaskKeys, Map<String, Object> taskValues) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(procInsId).executionId(executionId).singleResult();
        CustomJumpCommand command = new CustomJumpCommand(execution.getId(), targetTaskKeys, taskValues);
        //包装一个Command对象
        ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(command);
    }

    /**
     * 获取当前任务
     *
     * @param procInstId 流程实例
     * @return task 任务实例ID
     */
    public List<Task> getTodoTasks(String procInstId) {
        if (StringUtils.isEmpty(procInstId)) {
            throw new TroyActivitiException("processInstId must be set");
        }
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.processInstanceId(procInstId);
        List<Task> taskList = taskQuery.list();
        if (!CollectionUtils.isEmpty(taskList)) {
            return taskList;
        }
        return null;
    }

    /**
     * 获取当前任务
     *
     * @param procInstId 流程实例
     * @param userId     用户ID
     * @return task 任务实例ID
     */
    public Task getTask(String procInstId, String taskId, String userId) {
        if (StringUtils.isEmpty(procInstId)) {
            throw new TroyActivitiException("processInstId must be set");
        }
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.processInstanceId(procInstId);
        if (!StringUtils.isEmpty(taskId)) {
            taskQuery.taskId(taskId);
        }
        if (!StringUtils.isEmpty(userId)) {
            taskQuery.taskAssignee(userId);
        }
        List<Task> taskList = taskQuery.list();
        if (!CollectionUtils.isEmpty(taskList)) {
            return taskList.get(0);
        }
        return null;
    }


    /**
     * 获取流程历史变量
     *
     * @param procInstId   流程实例
     * @param variableName 变量名称
     * @return 历史变量值
     */
    public <T> T getHistoricVariable(String procInstId, String variableName, Class<T> clazz) {
        if (StringUtils.isEmpty(procInstId) || StringUtils.isEmpty(variableName)) {
            return null;
        }
        HistoricVariableInstanceQuery historyQuery = historyService.createHistoricVariableInstanceQuery();
        historyQuery.processInstanceId(procInstId);
        historyQuery.variableName(variableName);
        List<HistoricVariableInstance> historicList = historyQuery.list();
        if (!CollectionUtils.isEmpty(historicList)) {
            return (T) (historicList.get(0).getValue());
        }
        return null;
    }

    /**
     * 下一个任务，如果为单个候选人则直接认领
     *
     * @param procInstId 流程实例ID
     */
    private void singleUserClaim(String procInstId, String defaultUser) throws Exception {
        // 添加多任务支持
        // 下一个任务，如果为单个候选人则直接认领
        List<Task> tasks = getTodoTasks(procInstId);
        if (tasks != null && !CollectionUtils.isEmpty(tasks)) {
            for (Task task : tasks) {
                if (task != null && StringUtils.isEmpty(task.getAssignee())) {
                    String userId = StringUtils.isEmpty(defaultUser)?getTaskSingleTodoUser(task):defaultUser;
                    if (!StringUtils.isEmpty(userId)) {
                        // 认领任务
                        claim(procInstId, task.getId(), userId);
                    }
                }
            }
        }
    }

    private String getTaskSingleTodoUser(Task task) {
        List<String> users = actRuIdentitylinkRepository.queryTaskSingleTodoUser(task.getId());
        if (users != null && users.size() == 1) {
            return users.get(0);
        }
        return "";
    }

    /**
     * 任务签收
     *
     * @param procInstId 流程实例ID
     * @param taskId     任务ID
     * @param userId     用户ID
     * @return 任务签收成功则返回true，失败则返回false
     */
    public boolean claim(String procInstId, String taskId, String userId) throws Exception {
        if (!isClaim(procInstId, taskId, userId)) {
            taskService.claim(taskId, userId);
            return true;
        }
        return false;
    }

    /**
     * 获取任务签收状态
     *
     * @param procInstId 流程实例ID
     * @param taskId     任务ID
     * @param userId     用户ID
     * @return 判断任务是否可签收，已签收则返回true，未签收则返回false
     */
    public boolean isClaim(String procInstId, String taskId, String userId) throws Exception {
        if (StringUtils.isEmpty(procInstId)) {
            throw new TroyActivitiException("processInstId must be set");
        }
        if (StringUtils.isEmpty(taskId)) {
            throw new TroyActivitiException("taskId must be set");
        }
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.processInstanceId(procInstId);
        taskQuery.taskId(taskId);
        List<Task> taskList = taskQuery.list();
        String assignee = "";
        //默认未签收
        boolean flag = false;
        if (!CollectionUtils.isEmpty(taskList)) {
            Task task = taskList.get(0);
            assignee = task.getAssignee();
            //签收人为空,未签收,否则已经签收
            if (null == assignee || "".equals(assignee)) {
                TaskClaimRecord taskClaimRecord = new TaskClaimRecord();
                taskClaimRecord.setProcInstId(procInstId);
                taskClaimRecord.setTaskId(taskId);
                taskClaimRecord.setUserId(userId);
                try {
                    taskClaimRecordRepository.save(taskClaimRecord);
                } catch (Exception e) {
                    flag = true;
                }
            } else {
                flag = true;
            }
        }
        return flag;
    }
}
