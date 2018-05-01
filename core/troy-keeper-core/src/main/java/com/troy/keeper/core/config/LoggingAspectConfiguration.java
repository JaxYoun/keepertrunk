package com.troy.keeper.core.config;

import com.troy.keeper.core.logging.LoggingAspect;
import com.troy.keeper.core.logging.PerformanceAspect;
import io.github.jhipster.config.JHipsterConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    /**
	 @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }*/

    @Bean
    @Profile({JHipsterConstants.SPRING_PROFILE_DEVELOPMENT,JHipsterConstants.SPRING_PROFILE_TEST})
    public PerformanceAspect performanceAspect(CloudProperties cloudProperties) {
        return new PerformanceAspect(cloudProperties);
    }
}
