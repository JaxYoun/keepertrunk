package com.troy.keeper.core.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-18
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
public interface HttpSecurityConfigure {

    void configure(HttpSecurity http) throws Exception;
}
