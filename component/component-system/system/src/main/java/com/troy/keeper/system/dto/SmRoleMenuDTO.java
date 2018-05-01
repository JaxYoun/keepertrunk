package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/5.
 */
public class SmRoleMenuDTO extends BaseDTO {

    // 菜单ID
    private Long menuId;

    // 菜单组
    private List<Map<String, String>> menus;

    // 角色ID
    private Long roleId;

    // 权限级别
    private Integer level;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public List<Map<String, String>> getMenus() {
        return menus;
    }

    public void setMenus(List<Map<String, String>> menus) {
        this.menus = menus;
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
