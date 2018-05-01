package com.troy.keeper.monomer.api.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.time.ZonedDateTime;

/**
 * Created by yg on 2017/4/7.
 */
public class LoginLogDTO extends BaseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String ipAddress;
    private String sessionId;
    private ZonedDateTime loginTime;
    private ZonedDateTime logoutTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public ZonedDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(ZonedDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public ZonedDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(ZonedDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
}
