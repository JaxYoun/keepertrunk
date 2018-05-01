package com.troy.uaa.security;

import com.troy.keeper.core.config.CloudProperties;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-21
 * Time: 下午2:07
 * To change this template use File | Settings | File Templates.
 */
public class AwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private CloudProperties cloudProperties;

    public AwareAuthenticationEntryPoint(CloudProperties cloudProperties) {
        super(cloudProperties.getLogin().getLoginFormUrl());
    }
}
