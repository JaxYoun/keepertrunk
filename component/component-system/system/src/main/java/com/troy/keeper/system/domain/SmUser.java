package com.troy.keeper.system.domain;

import com.troy.keeper.monomer.security.domain.User;

import javax.persistence.*;
import java.util.List;

/**
 * Created by SimonChu on 2017/5/27.
 */
@Entity
@Table(name = "sm_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("SmUser")
public class SmUser extends User {

    // 组织机构ID
    @Column(name = "org_id")
    private Long orgId;

    // 名字
    @Column(name = "user_name", length = 50)
    private String userName;

    // 类型
    @Column(name = "type")
    private Integer type;

    // 账号
    @Column(name = "login_name", length = 30)
    private String loginName;

    // 手机号
    @Column(name = "mobile_phone", length = 15)
    private String mobilePhone;

    @OneToMany(mappedBy = "smUser", fetch = FetchType.LAZY)
    private List<SmPostUser> smPostUserList;

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

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public List<SmPostUser> getSmPostUserList() {
        return smPostUserList;
    }

    public void setSmPostUserList(List<SmPostUser> smPostUserList) {
        this.smPostUserList = smPostUserList;
    }
}
