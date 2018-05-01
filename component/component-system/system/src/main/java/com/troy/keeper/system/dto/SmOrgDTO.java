package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * 组织机构表DTO
 * Created by SimonChu on 2017/5/25.
 */
public class SmOrgDTO extends BaseDTO {
    // 组织标识
    private Long id;
    // 父组织机构ID
    private Long pId;
    // 层级关系标识
    private String relationship;
    // 名称
    private String orgName;
    // 顺序
    private Long orderCode;
    // 状态
    private Integer status;
    // 页数
    private int page;
    // 条数
    private int size;

    //机构级别
    private Integer orgLevel;

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Long orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
