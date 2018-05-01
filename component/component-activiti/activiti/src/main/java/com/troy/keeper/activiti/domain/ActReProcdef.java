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
@Table(name = "act_re_procdef")
public class ActReProcdef implements Serializable {

    @Id
    @Column(name = "id_")
    private String id;

    @Column(name = "name_")
    private String name;

    @Column(name = "key_")
    private String key;

    @Column(name = "version_")
    private Long version;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deployment_id_", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private ActReDeployment deployment;

    @Column(name = "resource_name_")
    private String resourceName;

    @Column(name = "dgrm_resource_name_")
    private String dgrmResourceName;

    @Column(name = "suspension_state_")
    private Long suspensionState;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public ActReDeployment getDeployment() {
        return deployment;
    }

    public void setDeployment(ActReDeployment deployment) {
        this.deployment = deployment;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDgrmResourceName() {
        return dgrmResourceName;
    }

    public void setDgrmResourceName(String dgrmResourceName) {
        this.dgrmResourceName = dgrmResourceName;
    }

    public Long getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(Long suspensionState) {
        this.suspensionState = suspensionState;
    }
}
