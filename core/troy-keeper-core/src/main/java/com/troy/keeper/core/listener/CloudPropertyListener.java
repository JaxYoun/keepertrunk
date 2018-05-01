package com.troy.keeper.core.listener;

import com.troy.keeper.core.config.CloudProperties;
import com.troy.keeper.core.utils.EnvironmentContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-5-17
 * Time: 下午5:08
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CloudPropertyListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>{

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        List<String> combinations = environment.getProperty("cloud.app.combine.combinations", List.class);
        Boolean enabled = environment.getProperty("cloud.app.combine.enabled", Boolean.class, false);
        if (enabled && CollectionUtils.isNotEmpty(combinations)) {
            CloudProperties.App.Combine.init(environment);
            EnvironmentContext.init(environment);
        }
    }

}
