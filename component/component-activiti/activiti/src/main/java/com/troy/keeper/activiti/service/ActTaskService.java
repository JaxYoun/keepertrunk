package com.troy.keeper.activiti.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * discription: 任务管理接口 <br/>
 * Date:     2017/6/2 15:50<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public interface ActTaskService {
    boolean claim(ActTaskParams param) throws Exception;

    StartProcessDTO startProcess(ActProcessStartParams param) throws Exception;

    void doTask(ActProcessDoTaskParams param) throws Exception;

    List<ToDoTaskDTO> todoList(ActTaskParams param) throws Exception;

    Page<ToDoTaskDTO> todoListPage(ActTaskParams param, Pageable pageable) throws Exception;

    List<HistoryTaskDTO> historyTaskList(ActTaskParams param) throws Exception;

    Page<HistoryTaskDTO> historyTaskListPage(ActTaskParams param, Pageable pageable) throws Exception;

    ProcessTackingDTO getProcessTracking(ActTaskParams param) throws Exception;

    void delProcessInst(ActProcessParams param) throws Exception;

    String getLastOperateUser(ActTaskParams param) throws Exception;

    List<ActivityDTO> getActivities(ActTaskParams param) throws Exception;

    void callBackProcess(ActProcessDoTaskParams param) throws Exception;

    void sendBackProcess(ActProcessDoTaskParams param) throws Exception;

    List<ProcessTackingDTO> getProcessTrackingList(ActTaskParams param) throws Exception;

    List<UserDTO> getTaskCandidates(ActTaskParams param) throws Exception;
}
