package com.troy.keeper.activiti.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;

/**
 * Date:     2017/6/7 15:42<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
public class ProcessTackingDTO extends BaseDTO {

    private String flowPic;

    private String procInstId;

    private String businessId;

    private String businessType;

    private String businessNo;

    private List<HistroyFlowDTO> history;

    public String getFlowPic() {
        return flowPic;
    }

    public void setFlowPic(String flowPic) {
        this.flowPic = flowPic;
    }

    public List<HistroyFlowDTO> getHistory() {
        return history;
    }

    public void setHistory(List<HistroyFlowDTO> history) {
        this.history = history;
    }

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

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }
}
