package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by SimonChu on 2017/6/5.
 */
@Entity
@Table(name = "sm_role_menu")
public class SmRoleMenu extends BaseEntity {

    //     菜单ID
    @Column(name = "menu_id")
    private Long menuId;

    // 角色ID
    @Column(name = "role_id")
    private Long roleId;

    // 权限级别
    @Column(name = "limit_level")
    private Integer level;


    @JoinColumn(name = "role_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
    @ManyToOne(fetch = FetchType.EAGER)
    private SmRole smRole;

    @JoinColumn(name = "menu_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
    @ManyToOne(fetch = FetchType.EAGER)
    private SmMenu smMenu;


    public SmMenu getSmMenu() {
        return smMenu;
    }

    public void setSmMenu(SmMenu smMenu) {
        this.smMenu = smMenu;
    }

    public SmRole getSmRole() {
        return smRole;
    }

    public void setSmRole(SmRole smRole) {
        this.smRole = smRole;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
