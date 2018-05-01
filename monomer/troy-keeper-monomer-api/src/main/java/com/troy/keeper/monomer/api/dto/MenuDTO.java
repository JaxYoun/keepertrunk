package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;

/**
 * Created by yjm on 2017/4/10.
 */
public class MenuDTO extends BaseDTO {
    private Long id;
    /**
     * 菜单名
     */
    private String menuName;

    /**
     * 菜单url
     */
    private String menuUrl;

    /**
     * 排序code
     */
    private Integer orderCode;

    /**
     * 父级
     */
    private Long parentId;

    /**
     * 子级菜单
     */
    private List<MenuDTO> children;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<MenuDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDTO> children) {
        this.children = children;
    }
}
