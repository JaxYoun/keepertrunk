package com.troy.keeper.activiti.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * activiti 字体配置 防止中文流程图乱码
 * Date:     2017/6/20 14:01<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Component
@ConfigurationProperties(prefix = "spring.activitiEx", ignoreUnknownFields = false)
public class CustomActivitiCfgConfigurer implements ProcessEngineConfigurationConfigurer {
    private String activityFontName;

    private String labelFontName;

    public String getActivityFontName() {
        return activityFontName;
    }

    public void setActivityFontName(String activityFontName) {
        this.activityFontName = activityFontName;
    }

    public String getLabelFontName() {
        return labelFontName;
    }

    public void setLabelFontName(String labelFontName) {
        this.labelFontName = labelFontName;
    }

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setActivityFontName(getActivityFontName());
        processEngineConfiguration.setLabelFontName(getLabelFontName());
    }
}
