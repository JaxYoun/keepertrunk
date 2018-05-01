package com.troy.uaa.domain;


import com.troy.keeper.core.base.entity.CapabilityAuditEntity;

import javax.persistence.Column;

/**
 * Created by yjm on 2017/4/7.
 */
public class IdapUser  extends CapabilityAuditEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 登陆名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 手机
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 用户类型
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 状态值
     * 0停用 ，1启用
     */
    @Column(name = "status_value")
    private String statusValue;

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
