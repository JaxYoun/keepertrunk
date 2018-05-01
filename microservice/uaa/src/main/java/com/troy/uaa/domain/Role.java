package com.troy.uaa.domain;


import com.troy.keeper.core.base.entity.CapabilityAuditEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yjm on 2017/4/7.
 */
@Entity
@Table(name = "role")
@Cacheable
public class Role extends CapabilityAuditEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_code")
    private String roleCode;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
