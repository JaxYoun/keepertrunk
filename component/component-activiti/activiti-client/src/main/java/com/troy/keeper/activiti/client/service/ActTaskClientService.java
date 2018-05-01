package com.troy.keeper.activiti.client.service;

import com.troy.keeper.activiti.dto.param.ActProcessDoTaskParams;
import com.troy.keeper.activiti.dto.param.ActProcessParams;
import com.troy.keeper.activiti.dto.param.ActProcessStartParams;
import com.troy.keeper.activiti.dto.param.ActTaskParams;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * discription: 任务管理接口 <br/>
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface ActTaskClientService {
    boolean claim(ActTaskParams param) throws Exception;

    Map<String, Object> startProcess(ActProcessStartParams param) throws Exception;

    void doTask(ActProcessDoTaskParams param) throws Exception;

    List<Map<String, Object>> todoList(ActTaskParams param) throws Exception;

    Map<String, Object> todoListPage(ActTaskParams param, Pageable pageable) throws Exception;

    List<Map<String, Object>> historyTaskList(ActTaskParams param) throws Exception;

    Map<String, Object> historyTaskListPage(ActTaskParams param, Pageable pageable) throws Exception;

    Map<String, Object> getProcessTracking(ActTaskParams param) throws Exception;

    void delProcessInst(ActProcessParams param) throws Exception;

    String getLastOperateUser(ActTaskParams param) throws Exception;

    List<Map<String, Object>> getActivities(ActTaskParams param) throws Exception;

    void callBackProcess(ActProcessDoTaskParams param) throws Exception;

    void sendBackProcess(ActProcessDoTaskParams param) throws Exception;

    List<Map<String, Object>> getProcessTrackingList(ActTaskParams param) throws Exception;

    List<Map<String, Object>> getTaskCandidates(ActTaskParams param) throws Exception;
}
