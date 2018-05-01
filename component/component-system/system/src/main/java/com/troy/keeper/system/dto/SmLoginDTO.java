package com.troy.keeper.system.dto;

import com.troy.keeper.core.base.dto.BaseDTO;

/**
 * Created by SimonChu on 2017/6/14.
 */
public class SmLoginDTO extends BaseDTO {

    // 验证码
    private String code;

    private Long id;

    private Long currentPostId;

    public Long getCurrentPostId() {
        return currentPostId;
    }

    public void setCurrentPostId(Long currentPostId) {
        this.currentPostId = currentPostId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
