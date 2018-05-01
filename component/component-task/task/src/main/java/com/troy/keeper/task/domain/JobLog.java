package com.troy.keeper.task.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.*;

/**
 * Created by yg on 2017/6/27.
 */
@Entity
@Table(name = "job_log", indexes = @Index(name="idx_joblog_jobid", columnList = "job_id"))
public class JobLog extends BaseAuditingEntity {
    //启动成功
    public static final String START_SUCCESS = "1";
    //启动失败
    public static final String START_FAIL = "0";
    //完成成功
    public static final String FINISH_SUCCESS = "1";
    //完成失败
    public static final String FINISH_FAIL = "0";
    /**
     * 任务名称
     */
    @Column(name = "job_id")
    private Long jobId;
    /**
     * 启动标志
     */
    @Column(name = "start_flag")
    private String startFlag;
    /**
     * 启动标志描述(冗余字段，就2个值 为了查询不走码表增加)
     */
    @Column(name = "start_flag_value")
    private String startFlagValue;
    /**
     * 完成标志
     */
    @Column(name = "finish_flag")
    private String  finishFlag;
    /**
     * 完成标志描述(冗余字段，就2个值 为了查询不走码表增加)
     */
    @Column(name = "finish_flag_value")
    private String  finishFlagValue;
    /**
     * 异常信息
     */
    @Lob
    @Column(name = "error_msg", columnDefinition="BLOB")
    private byte[] errorMsg;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getStartFlag() {
        return startFlag;
    }

    public void setStartFlag(String startFlag) {
        this.startFlag = startFlag;
    }

    public String getStartFlagValue() {
        return startFlagValue;
    }

    public void setStartFlagValue(String startFlagValue) {
        this.startFlagValue = startFlagValue;
    }

    public String getFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(String finishFlag) {
        this.finishFlag = finishFlag;
    }

    public String getFinishFlagValue() {
        return finishFlagValue;
    }

    public void setFinishFlagValue(String finishFlagValue) {
        this.finishFlagValue = finishFlagValue;
    }

    public byte[] getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(byte[] errorMsg) {
        this.errorMsg = errorMsg;
    }
}