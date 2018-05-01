package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by SimonChu on 2017/6/1.
 */
@Entity
@Table(name = "sm_role")
public class SmRole extends BaseAuditingEntity {

    // 角色名称
    @Column(name = "role_name", length = 50)
    private String roleName;

    // 角色描述
    @Column(name = "role_desc", length = 100)
    private String roleDesc;

//    // 状态
//    @Column(name = "status")
//    private Integer status;
    //角色编码
    @Column(name = "role_code")
    private String roleCode;

    @OneToMany(mappedBy = "smRole",fetch = FetchType.LAZY)
    private List<SmRoleMenu> smRoleMenuList;

    @OneToMany(mappedBy = "smRole",fetch = FetchType.LAZY)
    private List<SmPostRole> smPostRoleList;

    public List<SmPostRole> getSmPostRoleList() {
        return smPostRoleList;
    }

    public void setSmPostRoleList(List<SmPostRole> smPostRoleList) {
        this.smPostRoleList = smPostRoleList;
    }

    public List<SmRoleMenu> getSmRoleMenuList() {
        return smRoleMenuList;
    }

    public void setSmRoleMenuList(List<SmRoleMenu> smRoleMenuList) {
        this.smRoleMenuList = smRoleMenuList;
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

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
