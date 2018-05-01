package com.troy.keeper.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: Harry
 * Date: 17-7-6
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@Profile("!cc")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 600)
public class SessionConfiguration {

}
