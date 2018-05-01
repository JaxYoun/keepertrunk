package com.troy.keeper.system.web.rest;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.core.base.rest.BaseResource;
import com.troy.keeper.system.dto.UserInfoDTO;
import com.troy.keeper.system.service.SmLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by SimonChu on 2017/7/20.
 */
public abstract class BaseSystemResource<T extends BaseDTO> extends BaseResource {

    private final Logger log = LoggerFactory.getLogger(BaseSystemResource.class);

    @Autowired
    private SmLoginService smLoginService;

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfoDTO getLoginUserInfo() {
        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Object object = securityContext.getAuthentication().getPrincipal();
            return smLoginService.getUserByUserIdToCache(((Account) object).getAccountId());
        } catch (Exception e) {
            log.error("【BaseSystemResource】getLoginUserInfo：获取用户信息失败", e);
            // 如果出现异常则返回一个空对象
            return new UserInfoDTO();
        }
    }
}
