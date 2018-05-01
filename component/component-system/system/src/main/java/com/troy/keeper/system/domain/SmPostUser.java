package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by SimonChu on 2017/6/6.
 */
@Entity
@Table(name = "sm_post_user")
public class SmPostUser extends BaseEntity {

    // 用户ID
    @Column(name = "user_id")
    private Long userId;

    // 岗位ID
    @Column(name = "post_id")
    private Long postId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
    private SmUser smUser;

    // 关联岗位表
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
    private SmPost smPost;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public SmUser getSmUser() {
        return smUser;
    }

    public void setSmUser(SmUser smUser) {
        this.smUser = smUser;
    }

    public SmPost getSmPost() {
        return smPost;
    }

    public void setSmPost(SmPost smPost) {
        this.smPost = smPost;
    }
}
