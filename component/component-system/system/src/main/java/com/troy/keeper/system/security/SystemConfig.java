package com.troy.keeper.system.security;

import com.troy.keeper.system.dto.SmUserDTO;
import com.troy.keeper.system.intercept.account.AccountAdviceService;
import com.troy.keeper.system.intercept.account.impl.AbstractAccountAdviceServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
* Created by yg on 2017/4/28.
* 1. please access /login to understand how to get the crsf token.
* 2. and also the crsf token will get on the cookie. go to see.
*/
@Configuration
public class SystemConfig {

//    @Bean
//    @ConditionalOnMissingBean(UserDetailsService.class)
//    public UserDetailsService userDetailsService() {
//        return new SmLoginSecurity();
//    }

    @Bean("defultDetailService")
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }




//    @Bean
//         public AccountAdviceService accountAdviceService2() {
//        return new AbstractAccountAdviceServiceImpl("portalAccountService22"){};
//    }
//
//    @Bean
//    public AccountAdviceService accountAdviceService() {
//        return new AbstractAccountAdviceServiceImpl("portalAccountService11"){};
//    }
}
