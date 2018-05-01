package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by SimonChu on 2017/6/1.
 */
@Entity
@Table(name = "sm_menu")
public class SmMenu extends BaseAuditingEntity {

    // 父菜单ID
    @Column(name = "p_id")
    private Long pId;

    // 层级关系标识
    @Column(name = "relationship", length = 200)
    private String relationship;

    // 菜单名称
    @Column(name = "menu_name", length = 50)
    private String menuName;

    // 菜单URL
    @Column(name = "menu_url", length = 200)
    private String menuUrl;

    // 菜单图标
    @Column(name = "menu_icon", length = 100)
    private String menuIcon;

    // 排序编号
    @Column(name = "order_code")
    private Integer orderCode;

    @Column(name = "menu_type")
    private Integer menuType;

    // 菜单编码
    @Column(name = "menu_code")
    private String menuCode;

    @OneToMany(mappedBy = "smMenu", fetch = FetchType.LAZY)
    private List<SmRoleMenu> smRoleMenuList;

    public List<SmRoleMenu> getSmRoleMenuList() {
        return smRoleMenuList;
    }

    public void setSmRoleMenuList(List<SmRoleMenu> smRoleMenuList) {
        this.smRoleMenuList = smRoleMenuList;
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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
}
