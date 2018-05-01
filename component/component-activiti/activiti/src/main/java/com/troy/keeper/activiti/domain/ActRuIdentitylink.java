package com.troy.keeper.activiti.domain;


import java.io.Serializable;
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
@Table(name = "act_ru_identitylink")
public class ActRuIdentitylink implements Serializable {

    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "user_id_")
    private String userId;

    @Column(name = "group_id_")
    private String groupId;

    @Column(name = "type_")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id_", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private ActRuTask task;

    @Column(name = "proc_inst_id_")
    private String procInstId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proc_def_id_", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private ActReProcdef procDef;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActRuTask getTask() {
        return task;
    }

    public void setTask(ActRuTask task) {
        this.task = task;
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
}
