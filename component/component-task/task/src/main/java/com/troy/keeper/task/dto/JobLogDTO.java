package com.troy.keeper.task.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Created by yg on 2017/6/27.
 */
public class JobLogDTO extends BaseDTO {
    private Long id;
    private Long jobId;
    private String startFlag;
    private String startFlagValue;
    private String  finishFlag;
    private String  finishFlagValue;
    private byte[] errorMsg;
    private Long startDate;
    private Long endDate;
    private int page;
    private int size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}