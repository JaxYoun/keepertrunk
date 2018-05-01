package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.Map;

/**
 * Description: 租户参数信息 <br/>
 * Date:     2017/4/24 11:34<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class TenantPreferencesDTO extends BaseDTO {
    /**
     * 租户参数
     */
    private Map tenant = null;
    /**
     * 平台参数
     */
    private Map platform = null;

    public Map getTenant() {
        return tenant;
    }

    public void setTenant(Map tenant) {
        this.tenant = tenant;
    }

    public Map getPlatform() {
        return platform;
    }

    public void setPlatform(Map platform) {
        this.platform = platform;
    }
}
