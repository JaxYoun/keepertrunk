package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sm_login_log")
public class SmLoginLog extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ip", length = 100)
    private String ip;

    @Column(name = "login_type")
    private Integer loginType;

    @Column(name = "create_dt")
    private Long createDt;

    @Column(name = "logout_dt")
    private Long logoutDt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Long getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Long createDt) {
        this.createDt = createDt;
    }

    public Long getLogoutDt() {
        return logoutDt;
    }

    public void setLogoutDt(Long logoutDt) {
        this.logoutDt = logoutDt;
    }
}
