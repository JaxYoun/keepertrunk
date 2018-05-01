package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by SimonChu on 2017/6/5.
 */
@Entity
@Table(name = "sm_post")
public class SmPost extends BaseAuditingEntity {

    // 组织标识
    @Column(name = "org_id")
    private Long orgId;

    // 岗位名称
    @Column(name = "post_name", length = 50)
    private String postName;

    // 岗位描述
    @Column(name = "post_desc", length = 100)
    private String postDesc;

    // 状态
//    @Column(name = "status")
//    private Integer status;

    @OneToMany(mappedBy = "smPost", fetch = FetchType.LAZY)
    private List<SmPostRole> smPostRoleList;

    @OneToMany(mappedBy = "smPost", fetch = FetchType.LAZY)
    private List<SmPostUser> smPostUserList;

    //岗位级别
    @Column(name = "post_level")
    private Integer postLevel;

    //机构
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id",insertable = false,updatable = false)
    private SmOrg smOrg;

    //岗位负责人
    @Column(name = "sm_user_id")
    private Long smUserId;

    //岗位编码
    @Column(name = "post_code")
    private String postCode;

    public SmOrg getSmOrg() {
        return smOrg;
    }

    public void setSmOrg(SmOrg smOrg) {
        this.smOrg = smOrg;
    }

    public Integer getPostLevel() {
        return postLevel;
    }

    public void setPostLevel(Integer postLevel) {
        this.postLevel = postLevel;
    }

    public List<SmPostRole> getSmPostRoleList() {
        return smPostRoleList;
    }

    public void setSmPostRoleList(List<SmPostRole> smPostRoleList) {
        this.smPostRoleList = smPostRoleList;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public List<SmPostUser> getSmPostUserList() {
        return smPostUserList;
    }

    public void setSmPostUserList(List<SmPostUser> smPostUserList) {
        this.smPostUserList = smPostUserList;
    }

    public Long getSmUserId() {
        return smUserId;
    }

    public void setSmUserId(Long smUserId) {
        this.smUserId = smUserId;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
