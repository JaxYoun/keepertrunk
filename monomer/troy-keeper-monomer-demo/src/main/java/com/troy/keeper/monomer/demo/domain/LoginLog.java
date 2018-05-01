package com.troy.keeper.monomer.demo.domain;



import com.troy.keeper.core.base.entity.BaseAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by yg on 2017/4/7.
 */
@Entity
@Table(name = "login_log")
public class LoginLog extends BaseAuditingEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "login_time")
    private Date loginTime;

    @Column(name = "logout_time")
    private Date logoutTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }
}
