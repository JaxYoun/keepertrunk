package com.troy.keeper.core.security;

import com.troy.keeper.core.base.entity.Account;
import com.troy.keeper.core.context.UserDetailContextHolder;
import com.troy.keeper.core.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public class AgentUserDetailServiceImpl implements UserDetailsService {

    private static Map<String,UserDetailsService> map = null;

    private static final String sec = "application.userdetail.type";
    @Autowired
    private Environment env;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String detailType = UserDetailContextHolder.getType();
        if (StringUtils.isNotEmpty(detailType)){
            detailType = env.getProperty(sec+"."+detailType);
        }else{
            detailType = "defultDetailService";
        }

        //获取所有的UserDetailsService；
        UserDetailsService userDetailsService = null;
        if (map == null) {
            map = SpringUtils.getBeansOfType(UserDetailsService.class);
        }

        if (map != null){
            userDetailsService = map.get(detailType);
            if (userDetailsService == null){
                userDetailsService = SpringUtils.getBean(detailType);
            }
        }

        if ( userDetailsService != null){
            Account account = (Account) userDetailsService.loadUserByUsername(username);
            return account;
        }else{
            return null;
        }
    }
}
