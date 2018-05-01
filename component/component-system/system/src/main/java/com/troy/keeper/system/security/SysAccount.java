package com.troy.keeper.system.security;

import com.troy.keeper.core.base.entity.Account;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SysAccount extends Account {

    private Long postId;

    public SysAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Long accountId,Long postId) {
        super(username, password, authorities, accountId);
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
