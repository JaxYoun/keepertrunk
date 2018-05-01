package com.troy.keeper.activiti.domain;


import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Date: 2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Entity
@Table(name = "act_ru_task")
public class ActRuTask implements Serializable {

    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "name_")
    private String name;

    @Column(name = "task_def_key_")
    private String taskDefKey;

    @Column(name = "proc_inst_id_")
    private String procInstId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proc_def_id_", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private ActReProcdef procDef;

    @Column(name = "assignee_")
    private String assignee;

    @Column(name = "create_time_")
    private Instant createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public ActReProcdef getProcDef() {
        return procDef;
    }

    public void setProcDef(ActReProcdef procDef) {
        this.procDef = procDef;
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
}
