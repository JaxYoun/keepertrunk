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
public class QueryDeploymentDTO extends BaseDTO {
    private String id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Instant deployTime;

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

    public Instant getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Instant deployTime) {
        this.deployTime = deployTime;
    }
}
