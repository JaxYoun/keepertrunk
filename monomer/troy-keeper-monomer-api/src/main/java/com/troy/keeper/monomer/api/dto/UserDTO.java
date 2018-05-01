package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;
import java.util.Set;

/**
 * Created by yjm on 2017/4/7.
 */
public class UserDTO extends BaseDTO{
    //用户ID
    private Long id;

    //用户名
    private String userName;

    //登陆名
    private String loginName;

    //租户
    private Long tenantId;

    //手机
    private String mobile;

    //email
    private String email;

    //角色
    private List<Long> roles;

    //备注
    private String memo;

    //默认不更新角色
    private boolean updateRole = false;

    //用户类型
    private String type;

    //用户类型值
    private String value;

    //角色名（分页查询展示使用)
    private String roleName;

    //租户名
    private String tenantName;

    private TenantDTO tenantDTO;

    public TenantDTO getTenantDTO() {
        return tenantDTO;
    }

    private RoleDTO roleDTO;

    private Set<String> authorities;

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public RoleDTO getRoleDTO() {
        return roleDTO;
    }

    public void setRoleDTO(RoleDTO roleDTO) {
        this.roleDTO = roleDTO;
    }

    public void setTenantDTO(TenantDTO tenantDTO) {
        this.tenantDTO = tenantDTO;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUpdateRole() {
        return updateRole;
    }

    public void setUpdateRole(boolean updateRole) {
        this.updateRole = updateRole;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
}
