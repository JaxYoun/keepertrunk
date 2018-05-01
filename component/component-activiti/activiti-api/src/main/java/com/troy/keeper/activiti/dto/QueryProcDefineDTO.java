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
public class QueryProcDefineDTO extends BaseDTO {
    private String id;

    private String key;

    private String name;

    private String version;

    private String resourceName;

    private String dgrmResourceName;

    private String deployId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant deployTime;

    private String suspensionState;

    private String suspensionStateName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getDeployId() {
        return deployId;
    }

    public void setDeployId(String deployId) {
        this.deployId = deployId;
    }

    public Instant getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Instant deployTime) {
        this.deployTime = deployTime;
    }

    public String getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(String suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getSuspensionStateName() {
        return suspensionStateName;
    }

    public void setSuspensionStateName(String suspensionStateName) {
        this.suspensionStateName = suspensionStateName;
    }
}
