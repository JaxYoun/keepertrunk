package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sm_login_error_log")
public class SmLoginErrorLog extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_ip", length = 100)
    private String loginIp;

    @Column(name = "login_type")
    private Integer loginType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "log_dt")
    private Long date;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
