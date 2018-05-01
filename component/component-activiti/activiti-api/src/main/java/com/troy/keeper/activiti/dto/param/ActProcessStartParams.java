package com.troy.keeper.activiti.dto.param;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Discription: 流程启动参数设置
 * Date:     2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class ActProcessStartParams implements Serializable {
    /**
     * 流程启动用户
     */
    private String startUser = null;

    /**
     * 流程启动方式
     */
    private String processStartType = null;

    /**
     * 流程启动key
     */
    private String processKey = null;

    /**
     * 流程启动消息
     */
    private String startMessage = null;

    /**
     * 是否自动完成首个任务
     */
    private String autoDoFirstTask = null;

    /**
     * 流程启动需要的变量
     */
    private HashMap<String, Object> startValues = null;

    /**
     * 流程启动备注
     */
    private String startProcessComment = null;

    /**
     * 流程启动需要业务数据
     */
    private BussRelatedProcess bussRelatedProcess = null;

    public String getStartUser() {
        return startUser;
    }

    public void setStartUser(String startUser) {
        this.startUser = startUser;
    }

    public String getProcessStartType() {
        return processStartType;
    }

    public void setProcessStartType(String processStartType) {
        this.processStartType = processStartType;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getStartMessage() {
        return startMessage;
    }

    public void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    public String getAutoDoFirstTask() {
        return autoDoFirstTask;
    }

    public void setAutoDoFirstTask(String autoDoFirstTask) {
        this.autoDoFirstTask = autoDoFirstTask;
    }

    public HashMap<String, Object> getStartValues() {
        return startValues;
    }

    public void setStartValues(HashMap<String, Object> startValues) {
        this.startValues = startValues;
    }

    public String getStartProcessComment() {
        return startProcessComment;
    }

    public void setStartProcessComment(String startProcessComment) {
        this.startProcessComment = startProcessComment;
    }

    public BussRelatedProcess getBussRelatedProcess() {
        return bussRelatedProcess;
    }

    public void setBussRelatedProcess(BussRelatedProcess bussRelatedProcess) {
        this.bussRelatedProcess = bussRelatedProcess;
    }
}
