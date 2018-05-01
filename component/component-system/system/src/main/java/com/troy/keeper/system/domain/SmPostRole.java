package com.troy.keeper.system.domain;

import com.troy.keeper.core.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by SimonChu on 2017/6/5.
 */
@Entity
@Table(name = "sm_post_role")
public class SmPostRole extends BaseEntity {

    // 角色ID
    @Column(name = "role_id")
    private Long roleId;

    // 岗位ID
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",insertable = false,updatable = false)
    private SmRole smRole;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id",insertable = false,updatable = false)
    private SmPost smPost;

    //是否批量添加
    @Column(name = "source_batch")
    private Boolean sourceBatch;

    public Boolean getSourceBatch() {
        return sourceBatch;
    }

    public void setSourceBatch(Boolean sourceBatch) {
        this.sourceBatch = sourceBatch;
    }

    public SmRole getSmRole() {
        return smRole;
    }

    public void setSmRole(SmRole smRole) {
        this.smRole = smRole;
    }

    public SmPost getSmPost() {
        return smPost;
    }

    public void setSmPost(SmPost smPost) {
        this.smPost = smPost;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
