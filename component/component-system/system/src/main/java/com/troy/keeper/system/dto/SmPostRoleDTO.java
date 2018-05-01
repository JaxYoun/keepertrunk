package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/5.
 */
public class SmPostRoleDTO extends BaseDTO {

    // ID
    private Long id;

    // 角色ID
    private Long roleId;

    // 岗位ID
    private Long postId;

    // 角色组
    private List<String> roleIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
