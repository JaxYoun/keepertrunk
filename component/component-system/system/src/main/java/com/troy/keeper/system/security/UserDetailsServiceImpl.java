package com.troy.keeper.system.security;

import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.repository.SmLoginErrorLogRepository;
import com.troy.keeper.system.service.SmLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by SimonChu on 2017/6/15.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private static final String VALID_DATA_STATUS = "1";

    @Autowired
    private SmLoginService smLoginService;

    @Autowired
    private SmLoginErrorLogRepository smLoginErrorLogRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.info("[系统管理日志：登录系统] ———— 用户请求 登录账户：" + login);
        //通过 Lambda 表达式判断是否生效
        Optional<SmUser> smUserData = smLoginService.getUserInfoByLoginName(login)
                .filter(smUser -> {
                    if (!smUser.getStatus().equals(VALID_DATA_STATUS)) {
                        log.warn("[系统管理日志：登录系统] ———— 用户请求 登录账户：" + login + "已删除！禁止登录");
                        return false;
                    } else {
                        return true;
                    }
                })
                .filter(smUser -> {
                    if (smUser.getActivated()) {
                        return true;
                    } else {
                        log.warn("[系统管理日志：登录系统] ———— 用户请求 登录账户：" + login + "已失效！禁止登录");
                        return false;
                    }
                });
        return smUserData.map(user -> {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("null"));
            // 清空缓存用户信息
            smLoginService.delUserIdCache(smUserData.get().getId());
            return new SysAccount(smUserData.get().getLoginName(), smUserData.get().getPassword(), grantedAuthorities, smUserData.get().getId(),null);
        }).orElseThrow(() -> {
            log.warn("[系统管理日志：登录系统]————登录账号：" + login + " [出现异常]");
            return new UsernameNotFoundException(login);
        });
    }

}
