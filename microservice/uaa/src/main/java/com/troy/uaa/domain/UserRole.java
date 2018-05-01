package com.troy.uaa.domain;


import com.troy.keeper.core.base.entity.CapabilityAuditEntity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by yjm on 2017/4/7.
 */
@Entity
@Table(name = "user_role")
@Cacheable
public class UserRole extends CapabilityAuditEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    /**
     * 角色
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
