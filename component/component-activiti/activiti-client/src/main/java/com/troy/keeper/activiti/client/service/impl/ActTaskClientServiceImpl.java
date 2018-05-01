package com.troy.keeper.activiti.client.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.troy.keeper.activiti.client.config.ActivitiConfig;
import com.troy.keeper.activiti.client.config.ActivitiConstants;
import com.troy.keeper.activiti.client.util.HttpUtil;
import com.troy.keeper.activiti.dto.param.ActProcessDoTaskParams;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.dto.param.ActProcessStartParams;
import com.troy.keeper.activiti.dto.param.ActTaskParams;
import com.troy.keeper.activiti.client.service.ActTaskClientService;
import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Date:     2017/6/7 14:52<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Service
@Transactional
public class ActTaskClientServiceImpl extends BaseServiceImpl implements ActTaskClientService {
    private Logger log = LoggerFactory.getLogger(ActTaskClientServiceImpl.class);

    @Autowired
    HttpUtil httpUtil;
    @Autowired
    ActivitiConfig activitiConfig;
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public boolean claim(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_CLAIM, null, objectMapper.writeValueAsString(param));
        ResponseDTO<String> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, Object> startProcess(ActProcessStartParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_START_PROCESS, null, objectMapper.writeValueAsString(param));
        ResponseDTO<Map<String, Object>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("启动失败");
        }
    }

    @Override
    public void doTask(ActProcessDoTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_DO_TASK, null, objectMapper.writeValueAsString(param));
        ResponseDTO<String> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
        } else {
            throw new RuntimeException("办理失败");
        }
    }

    @Override
    public List<Map<String, Object>> todoList(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_TODO_LIST, null, objectMapper.writeValueAsString(param));
        ResponseDTO<List<Map<String, Object>>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public Map<String, Object> todoListPage(ActTaskParams param, Pageable pageable) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_TODO_LIST_PAGE + String.format("?page=%d&size=%d", pageable.getPageNumber(), pageable.getPageSize() ), null, objectMapper.writeValueAsString(param));
        ResponseDTO<Map<String, Object>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public List<Map<String, Object>> historyTaskList(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_HISTORY_TASK_LIST, null, objectMapper.writeValueAsString(param));
        ResponseDTO<List<Map<String, Object>>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public Map<String, Object> historyTaskListPage(ActTaskParams param, Pageable pageable) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_HISTORY_TASK_LIST_PAGE + String.format("?page=%d&size=%d", pageable.getPageNumber(), pageable.getPageSize() ), null, objectMapper.writeValueAsString(param));
        ResponseDTO<Map<String, Object>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public Map<String, Object> getProcessTracking(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_GET_PROCESS_TRACKING, null, objectMapper.writeValueAsString(param));
        ResponseDTO<Map<String, Object>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public void delProcessInst(ActProcessParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_DEL_PROCESS, null, objectMapper.writeValueAsString(param));
        ResponseDTO<String> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
        } else {
            throw new RuntimeException("删除失败");
        }
    }

    @Override
    public String getLastOperateUser(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_GET_LAST_OPERATE_USER, null, objectMapper.writeValueAsString(param));
        ResponseDTO<String> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public List<Map<String, Object>> getActivities(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_GET_ACTIVITIES, null, objectMapper.writeValueAsString(param));
        ResponseDTO<List<Map<String, Object>>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public void callBackProcess(ActProcessDoTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_CALL_BACK_PROCESS, null, objectMapper.writeValueAsString(param));
        ResponseDTO<String> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
        } else {
            throw new RuntimeException("撤销失败");
        }
    }

    @Override
    public void sendBackProcess(ActProcessDoTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_SEND_BACK_PROCESS, null, objectMapper.writeValueAsString(param));
        ResponseDTO<String> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
        } else {
            throw new RuntimeException("退回失败");
        }
    }

    @Override
    public List<Map<String, Object>> getProcessTrackingList(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_GET_PROCESS_TRACKING_LIST, null, objectMapper.writeValueAsString(param));
        ResponseDTO<List<Map<String, Object>>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }

    @Override
    public List<Map<String, Object>> getTaskCandidates(ActTaskParams param) throws Exception {
        String s = httpUtil.doPostByHeadWithBody(activitiConfig.getBaseUrl() + ActivitiConstants.URL_GET_TASK_CANDIDATES, null, objectMapper.writeValueAsString(param));
        ResponseDTO<List<Map<String, Object>>> dto = objectMapper.readValue(s, ResponseDTO.class);
        if(dto!=null && "200".equals(dto.getCode())){
            return dto.getData();
        } else {
            throw new RuntimeException("查询失败");
        }
    }
}
