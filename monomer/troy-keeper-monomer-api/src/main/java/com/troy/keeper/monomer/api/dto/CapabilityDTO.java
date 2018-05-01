package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by yg on 2017/4/5.
 */
public class CapabilityDTO extends BaseDTO{
    private Long id;
    private String capabilityName;
    private String capabilityDesc;
    private String icon;
    private String type;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCapabilityName() {
        return capabilityName;
    }

    public void setCapabilityName(String capabilityName) {
        this.capabilityName = capabilityName;
    }

    public String getCapabilityDesc() {
        return capabilityDesc;
    }

    public void setCapabilityDesc(String capabilityDesc) {
        this.capabilityDesc = capabilityDesc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
