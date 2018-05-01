package com.troy.keeper.autoconfigure;

import com.troy.keeper.core.config.BeanConfiguration;
import com.troy.keeper.core.config.CloudProperties;
import com.troy.keeper.core.config.JwtConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
//@ComponentScan(
//        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OAuth2InterceptedFeignConfiguration.class)
//)
@Import({BeanConfiguration.class, JwtConfiguration.class})
@EnableConfigurationProperties({LiquibaseProperties.class, CloudProperties.class})
@EnableDiscoveryClient
public class AppAutoConfiguration {


}
