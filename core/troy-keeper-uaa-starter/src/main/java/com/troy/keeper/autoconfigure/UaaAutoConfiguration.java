package com.troy.keeper.autoconfigure;

import com.troy.keeper.core.config.BeanConfiguration;
import com.troy.keeper.core.config.JwtConfiguration;
import com.troy.keeper.core.config.UaaConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-3-28
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@Import({BeanConfiguration.class, JwtConfiguration.class, UaaConfiguration.class})
//@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
@EnableDiscoveryClient
public class UaaAutoConfiguration {


}
