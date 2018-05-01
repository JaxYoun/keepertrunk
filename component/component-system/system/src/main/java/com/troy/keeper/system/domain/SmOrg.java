package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by SimonChu on 2017/5/25.
 */
@Entity
@Table(name = "sm_org")
public class SmOrg extends BaseAuditingEntity {


    // 父组织机构ID
    @Column(name = "p_id")
    private Long pId;
    // 层级关系标识
    @Column(name = "relationship",length = 200)
    private String relationship;
    // 名称
    @Column(name = "org_name",length = 50)
    private String orgName;
    // 顺序
    @Column(name = "order_code",length = 11)
    private Long orderCode;
    // 状态
//    @Column(name = "status")
//    private Integer status;
    //机构级别
    @Column(name = "org_level")
    private Integer orgLevel;

    //岗位
    @OneToMany(mappedBy = "smOrg",fetch = FetchType.LAZY)
    private List<SmPost> smPostList;

    public List<SmPost> getSmPostList() {
        return smPostList;
    }

    public void setSmPostList(List<SmPost> smPostList) {
        this.smPostList = smPostList;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
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

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

}
