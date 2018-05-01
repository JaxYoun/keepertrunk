package com.troy.keeper.activiti.dto.param;

import java.io.Serializable;

/**
 * Discription: 流程删除参数设置
 * Date:     2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class ActProcessDelParams implements Serializable {

    /**
     * 流程实例ID
     */
    private String procInstId = null;

    /**
     * 业务ID
     */
    private String businessId = null;

    /**
     * 业务类型
     */
    private String businessType = null;

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
