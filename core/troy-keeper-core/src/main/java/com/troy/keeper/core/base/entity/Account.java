package com.troy.keeper.core.base.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by yjm on 2017/4/26.
 */
public class Account extends org.springframework.security.core.userdetails.User{

    private Long accountId;

    public Account(String username, String password,
                   Collection<? extends GrantedAuthority> authorities, Long accountId){
        super(username, password, true, true, true, true, authorities);
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }
}
