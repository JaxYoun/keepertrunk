package com.troy.keeper.activiti.dto.param;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Discription: 执行任务参数设置
 * Date:     2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class ActProcessDoTaskParams implements Serializable {

    /**
     * 流程实例ID
     */
    private String procInstId = null;

    /**
     * 任务ID
     */
    private String taskId = null;

    /**
     * 任务处理用户
     */
    private String taskOprateUser = null;

    /**
     * 当前任务处理备注
     */
    private String taskComment = null;

    /**
     * 下一步任务自动签收人，一般不设置
     */
    private String autoClaimUser = null;

    /**
     *  指定下一步环节key
     */
    private List<String> targetTaskKeys = null;

    /**
     * 流程处理需要的变量
     */
    private HashMap<String, Object> taskValues = null;

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskOprateUser() {
        return taskOprateUser;
    }

    public void setTaskOprateUser(String taskOprateUser) {
        this.taskOprateUser = taskOprateUser;
    }

    public String getTaskComment() {
        return taskComment;
    }

    public String getAutoClaimUser() {
        return autoClaimUser;
    }

    public void setAutoClaimUser(String autoClaimUser) {
        this.autoClaimUser = autoClaimUser;
    }

    public void setTaskComment(String taskComment) {
        this.taskComment = taskComment;
    }

    public List<String> getTargetTaskKeys() {
        return targetTaskKeys;
    }

    public void setTargetTaskKeys(List<String> targetTaskKeys) {
        this.targetTaskKeys = targetTaskKeys;
    }

    public HashMap<String, Object> getTaskValues() {
        return taskValues;
    }

    public void setTaskValues(HashMap<String, Object> taskValues) {
        this.taskValues = taskValues;
    }
}

