package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Created by SimonChu on 2017/6/1.
 */
public class SmMenuDTO extends BaseDTO {

    // 父菜单ID
    private Long pId;

    // 层级关系标识
    private String relationship;

    // 菜单名称
    private String menuName;

    // 菜单URL
    private String menuUrl;

    // 菜单图标
    private String menuIcon;

    // 排序编号
    private Integer orderCode;

    // 状态
    private Integer status;

    // 菜单id
    private Long id;

    // 菜单类型
    private Integer menuType;

    // 菜单编码
    private String menuCode;

    // 页数
    private int page;

    // 条数
    private int size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

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

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }
}
