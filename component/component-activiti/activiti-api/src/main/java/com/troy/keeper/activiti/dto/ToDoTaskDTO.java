package com.troy.keeper.activiti.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.troy.keeper.core.base.dto.BaseDTO;

import java.time.Instant;

/**
 * Date:     2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class ToDoTaskDTO extends BaseDTO {

    private String businessId;

    private String businessNo;

    private String businessType;

    private String title;

    private String actId;

    private String actKey;

    private String actName;

    private String userId;

    private String userName;

    private String procInstId;

    private String procdefId;

    private String procdefName;

    private String version;

    private String author;

    private String authorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant createTime;


    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getActKey() {
        return actKey;
    }

    public void setActKey(String actKey) {
        this.actKey = actKey;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProcdefId() {
        return procdefId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public void setProcdefId(String procdefId) {
        this.procdefId = procdefId;
    }

    public String getProcdefName() {
        return procdefName;
    }

    public void setProcdefName(String procdefName) {
        this.procdefName = procdefName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }
}
