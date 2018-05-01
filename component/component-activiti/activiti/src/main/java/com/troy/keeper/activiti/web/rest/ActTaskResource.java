package com.troy.keeper.activiti.web.rest;

import com.troy.keeper.activiti.dto.ActivityDTO;
import com.troy.keeper.activiti.dto.HistoryTaskDTO;
import com.troy.keeper.activiti.dto.ProcessTackingDTO;
import com.troy.keeper.activiti.dto.StartProcessDTO;
import com.troy.keeper.activiti.dto.ToDoTaskDTO;
import com.troy.keeper.activiti.dto.UserDTO;
import com.troy.keeper.activiti.dto.param.ActProcessDoTaskParams;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.dto.param.ActProcessStartParams;
import com.troy.keeper.activiti.dto.param.ActTaskParams;
import com.troy.keeper.activiti.service.ActTaskService;
import com.troy.keeper.activiti.web.rest.errors.TroyActivitiException;
import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.rest.BaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * discription: 任务管理 <br/>
 * Date:     2017/6/1 16:09<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@RestController
public class ActTaskResource extends BaseResource<BaseDTO> {
    private Logger log = LoggerFactory.getLogger(ActTaskResource.class);

    @Autowired
    private ActTaskService actTaskService;

    /**
     * 任务签收接口
     */
    @RequestMapping(
            value = "/api/actTask/claim",
            method = RequestMethod.POST
    )
    public ResponseDTO<String> claim(@RequestBody ActTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getUserId())) {
                return new ResponseDTO<>("202", "用户ID为空", null);
            }

            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }

            if (StringUtils.isEmpty(param.getTaskId())) {
                return new ResponseDTO<>("202", "任务ID为空", null);
            }

            if (actTaskService.claim(param)) {
                return new ResponseDTO<>("200", "签收成功", null);
            } else {
                return new ResponseDTO<>("201", "签收失败", null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 删除流程实例接口
     */
    @RequestMapping(
            value = "/api/actProc/delProcess",
            method = RequestMethod.POST
    )
    public ResponseDTO<String> delProcess(@RequestBody ActProcessParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getBusinessId())) {
                return new ResponseDTO<>("202", "业务ID为空", null);
            }

            if (StringUtils.isEmpty(param.getBusinessType())) {
                return new ResponseDTO<>("202", "业务类型为空", null);
            }

            actTaskService.delProcessInst(param);

            return new ResponseDTO<>("200", "删除成功", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 任务办理接口
     */
    @RequestMapping(
            value = "/api/actTask/doTask",
            method = RequestMethod.POST
    )
    public ResponseDTO<String> doTask(@RequestBody ActProcessDoTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }

            if (StringUtils.isEmpty(param.getTaskId())) {
                return new ResponseDTO<>("202", "任务ID为空", null);
            }

            if (StringUtils.isEmpty(param.getTaskOprateUser())) {
                return new ResponseDTO<>("202", "任务处理人为空", null);
            }

            actTaskService.doTask(param);

            return new ResponseDTO<>("200", "办理成功", null);
        } catch (TroyActivitiException e) {
            return getResponseDTO("201", e.getMessage(), null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 已办任务查询接口
     */
    @RequestMapping(
            value = "/api/actTask/historyTaskList",
            method = RequestMethod.POST
    )
    public ResponseDTO<List<HistoryTaskDTO>> historyTaskList(@RequestBody ActTaskParams param) throws Exception {
        try {
            List<HistoryTaskDTO> dtos = actTaskService.historyTaskList(param);

            return new ResponseDTO<>("200", "查詢成功", dtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }


    /**
     * 已办任务查询接口 分页
     */
    @RequestMapping(
            value = "/api/actTask/historyTaskListPage",
            method = RequestMethod.POST
    )
    public ResponseDTO<Page<HistoryTaskDTO>> historyTaskListPage(@RequestBody ActTaskParams param, Pageable pageable) throws Exception {
        try {
            if(pageable == null){
                return getResponseDTO("202", "请求参数分页参数错误", null);
            }
            Page<HistoryTaskDTO> dtos = actTaskService.historyTaskListPage(param, pageable);

            return new ResponseDTO<>("200", "查詢成功", dtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 启动流程接口
     */
    @RequestMapping(
            value = "/api/actTask/startProcess",
            method = RequestMethod.POST
    )
    public ResponseDTO<StartProcessDTO> startProcess(@RequestBody ActProcessStartParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getStartUser())) {
                return new ResponseDTO<>("202", "用户ID为空", null);
            }

            if (StringUtils.isEmpty(param.getProcessStartType())) {
                param.setProcessStartType("0");    // 按流程定义key启动流程(默认)
            }

            if ("0".equals(param.getProcessStartType()) && StringUtils.isEmpty(param.getProcessKey())) {
                return new ResponseDTO<>("202", "流程启动key为空", null);
            }

            if ("1".equals(param.getProcessStartType()) && StringUtils.isEmpty(param.getStartMessage())) {
                return new ResponseDTO<>("202", "流程启动消息为空", null);
            }

            if ((null == param.getBussRelatedProcess())
                    || StringUtils.isEmpty(param.getBussRelatedProcess().getBusinessId())) {
                return new ResponseDTO<>("202", "业务ID为空", null);
            }

            if ((null == param.getBussRelatedProcess())
                    || StringUtils.isEmpty(param.getBussRelatedProcess().getBusinessType())) {
                return new ResponseDTO<>("202", "业务分类为空", null);
            }

            StartProcessDTO dto = actTaskService.startProcess(param);

            return new ResponseDTO<>("200", "启动成功", dto);
        } catch (TroyActivitiException e) {
            return getResponseDTO("201", e.getMessage(), null);
        }  catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 待办任务查询接口
     */
    @RequestMapping(
            value = "/api/actTask/todoList",
            method = RequestMethod.POST
    )
    public ResponseDTO<List<ToDoTaskDTO>> todoList(@RequestBody ActTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getUserId())) {
                return new ResponseDTO<>("202", "用户ID为空", null);
            }

            if (StringUtils.isEmpty(param.getPostId())) {
                return new ResponseDTO<>("202", "用户岗位ID为空", null);
            }

            List<ToDoTaskDTO> dtos = actTaskService.todoList(param);

            return new ResponseDTO<>("200", "查詢成功", dtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 待办任务查询接口 分页
     */
    @RequestMapping(
            value = "/api/actTask/todoListPage",
            method = RequestMethod.POST
    )
    public ResponseDTO<Page<ToDoTaskDTO>> todoListPage(@RequestBody ActTaskParams param, Pageable pageable) throws Exception {
        try {
            if(pageable == null){
                return getResponseDTO("202", "请求参数分页参数错误", null);
            }
            if (StringUtils.isEmpty(param.getUserId())) {
                return new ResponseDTO<>("202", "用户ID为空", null);
            }

            if (StringUtils.isEmpty(param.getPostId())) {
                return new ResponseDTO<>("202", "用户岗位ID为空", null);
            }

            Page<ToDoTaskDTO> dtos = actTaskService.todoListPage(param, pageable);

            return new ResponseDTO<>("200", "查詢成功", dtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 流程追踪接口
     */
    @RequestMapping(
            value = "/api/actTask/getProcessTracking",
            method = RequestMethod.POST
    )
    public ResponseDTO<ProcessTackingDTO> getProcessTracking(@RequestBody ActTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }

            ProcessTackingDTO dto = actTaskService.getProcessTracking(param);

            return new ResponseDTO<>("200", "查詢成功", dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 流程追踪接口
     */
    @RequestMapping(
            value = "/api/actTask/getProcessTrackingList",
            method = RequestMethod.POST
    )
    public ResponseDTO<List<ProcessTackingDTO>> getProcessTrackingList(@RequestBody ActTaskParams param) throws Exception {
        try {
            List<ProcessTackingDTO> dto = actTaskService.getProcessTrackingList(param);

            return new ResponseDTO<>("200", "查詢成功", dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }



    /**
     * 获取任务最近办理人接口
     */
    @RequestMapping(
            value = "/api/actTask/getLastOperateUser",
            method = RequestMethod.POST
    )
    public ResponseDTO<String> getLastOperateUser(@RequestBody ActTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }

            String userId = actTaskService.getLastOperateUser(param);

            return new ResponseDTO<>("200", "查詢成功", userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }



    /**
     * 获取可选任务接口
     */
    @RequestMapping(
            value = "/api/actTask/getActivities",
            method = RequestMethod.POST
    )
    public ResponseDTO<List<ActivityDTO>> getActivities(@RequestBody ActTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }
            if (StringUtils.isEmpty(param.getProcDefId())) {
                return new ResponseDTO<>("202", "流程定义ID为空", null);
            }
            List<ActivityDTO> userId = actTaskService.getActivities(param);

            return new ResponseDTO<>("200", "查詢成功", userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }


    /**
     * 收回流程接口
     */
    @RequestMapping(
            value = "/api/actTask/callBackProcess",
            method = RequestMethod.POST
    )
    public ResponseDTO<String> callBackProcess(@RequestBody ActProcessDoTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }

            if (StringUtils.isEmpty(param.getTaskId())) {
                return new ResponseDTO<>("202", "任务ID为空", null);
            }

            actTaskService.callBackProcess(param);

            return new ResponseDTO<>("200", "收回成功", null);
        } catch (TroyActivitiException e) {
            return getResponseDTO("201", e.getMessage(), null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 退回流程接口
     */
    @RequestMapping(
            value = "/api/actTask/sendBackProcess",
            method = RequestMethod.POST
    )
    public ResponseDTO<String> sendBackProcess(@RequestBody ActProcessDoTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }

            if (StringUtils.isEmpty(param.getTaskId())) {
                return new ResponseDTO<>("202", "任务ID为空", null);
            }

            if (StringUtils.isEmpty(param.getTaskOprateUser())) {
                return new ResponseDTO<>("202", "任务处理人为空", null);
            }

            actTaskService.sendBackProcess(param);

            return new ResponseDTO<>("200", "退回成功", null);
        } catch (TroyActivitiException e) {
            return getResponseDTO("201", e.getMessage(), null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }

    /**
     * 获取任务候选人接口
     */
    @RequestMapping(
            value = "/api/actTask/getTaskCandidates",
            method = RequestMethod.POST
    )
    public ResponseDTO<List<UserDTO>> getTaskCandidates(@RequestBody ActTaskParams param) throws Exception {
        try {
            if (StringUtils.isEmpty(param.getProcInstId())) {
                return new ResponseDTO<>("202", "流程实例ID为空", null);
            }
            if (StringUtils.isEmpty(param.getTaskId())) {
                return new ResponseDTO<>("202", "任务ID为空", null);
            }
            List<UserDTO> userDTOS = actTaskService.getTaskCandidates(param);

            return new ResponseDTO<>("200", "查詢成功", userDTOS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return getResponseDTO("201", "系统内部错误", null);
        }
    }
}
