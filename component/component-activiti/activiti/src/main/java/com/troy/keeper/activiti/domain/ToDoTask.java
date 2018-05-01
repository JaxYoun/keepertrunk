package com.troy.keeper.activiti.domain;


import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Date: 2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Entity
@Table(name = "v_todo_task")
public class ToDoTask implements Serializable {

    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "proc_inst_id_")
    private String procInstId;

    @Column(name = "proc_def_id_")
    private String procDefId;

    @Column(name = "proc_def_name_")
    private String procDefName;

    @Column(name = "version_")
    private Long version;

    @Column(name = "task_def_key_")
    private String taskDefKey;

    @Column(name = "task_def_name_")
    private String taskDefName;

    @Column(name = "assignee_")
    private String assignee;

    @Column(name = "create_time_")
    private Instant createTime;

    @Column(name = "business_id")
    private String businessId;

    @Column(name = "business_no")
    private String businessNo;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "candidate_user_id")
    private String candidateUserId;

    @Column(name = "candidate_group_id")
    private String candidateGroupId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcDefName() {
        return procDefName;
    }

    public void setProcDefName(String procDefName) {
        this.procDefName = procDefName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskDefName() {
        return taskDefName;
    }

    public void setTaskDefName(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCandidateUserId() {
        return candidateUserId;
    }

    public void setCandidateUserId(String candidateUserId) {
        this.candidateUserId = candidateUserId;
    }

    public String getCandidateGroupId() {
        return candidateGroupId;
    }

    public void setCandidateGroupId(String candidateGroupId) {
        this.candidateGroupId = candidateGroupId;
    }
}
