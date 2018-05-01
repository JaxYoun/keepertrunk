package com.troy.uaa.domain;

import com.troy.keeper.core.base.entity.CapabilityAuditEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A user.
 */
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends CapabilityAuditEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 租组
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /*
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 登陆名
     */
    @Column(name = "login_name")
    private String login;

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
     * 类型值
     */
    @Column(name = "user_type_value")
    private String userTypeValue;

    /**
     * 角色中间实体
     */
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<UserRole> userRoleList;

    public String getUserTypeValue() {
        return userTypeValue;
    }

    public void setUserTypeValue(String userTypeValue) {
        this.userTypeValue = userTypeValue;
    }

    public List<UserRole> getUserRoleList() {
        return userRoleList;
    }

    public void setUserRoleList(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
}
