package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/5.
 */
public class SmPostDTO extends BaseDTO {

    // 主键ID
    private Long id;

    // 组织标识
    private Long orgId;

    // 岗位ID
    private Long postId;

    // 岗位名称
    private String postName;

    // 岗位描述
    private String postDesc;

    // 状态
    private Integer status;

    // 角色组
    private List<String> roleIds;

    //岗位级别
    private Integer postLevel;

    //岗位ID集合
    private List<Long> postIds;

    //岗位负责人
    private Long smUserId;

    //岗位编码
    private String postCode;

    public Long getSmUserId() {
        return smUserId;
    }

    public void setSmUserId(Long smUserId) {
        this.smUserId = smUserId;
    }

    public Integer getPostLevel() {
        return postLevel;
    }

    public void setPostLevel(Integer postLevel) {
        this.postLevel = postLevel;
    }

    public List<Long> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<Long> postIds) {
        this.postIds = postIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
