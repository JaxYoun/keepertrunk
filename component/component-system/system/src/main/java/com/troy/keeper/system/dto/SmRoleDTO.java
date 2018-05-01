package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Created by SimonChu on 2017/6/1.
 */
public class SmRoleDTO extends BaseDTO {

    // 角色ID
    private Long id;

    // 角色名称
    private String roleName;

    // 角色描述
    private String roleDesc;

    // 状态
    private Integer status;

    // 页数
    private int page;

    // 条数
    private int size;

    //角色编码
    private String roleCode;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
