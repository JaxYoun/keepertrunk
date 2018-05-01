package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

import java.util.List;

/**
 * Created by SimonChu on 2017/6/6.
 */
public class SmPostUserDTO extends BaseDTO {

    // 岗位ID
    private Long postId;

    // 角色ID
    private Long userId;

    // 角色ID组
    private List<Long> userIds;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
