package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/7/19.
 */
public class UserInfoDTO extends BaseDTO {

    // 用户ID
    private Long id;

    // 组织机构ID
    private Long orgId;

    // 名字
    private String userName;

    // 类型
    private Integer type;

    // 账号
    private String loginName;

    // 邮箱号
    private String email;

    // 手机号
    private String mobilePhone;

    // 菜单列表
    private List<Map<String, Object>> menuIds;

    // 当前的岗位ID
    private Long currentPostId;

    // 岗位列表
    private List<Map<String, Object>> posts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public List<Map<String, Object>> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Map<String, Object>> menuIds) {
        this.menuIds = menuIds;
    }

    public Long getCurrentPostId() {
        return currentPostId;
    }

    public void setCurrentPostId(Long currentPostId) {
        this.currentPostId = currentPostId;
    }

    public List<Map<String, Object>> getPosts() {
        return posts;
    }

    public void setPosts(List<Map<String, Object>> posts) {
        this.posts = posts;
    }
}
