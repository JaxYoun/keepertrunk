package com.troy.keeper.core.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 配置工具类 <br/>
 * Date:     2017/4/6 15:29<br/>
 *
 * @author work_tl
 * @see
 * @since JDK 1.8
 */
@Component
@ConfigurationProperties(prefix="cdapProps")
public class PropUtils {
  private Map<String, String> mapProps = new HashMap<>();

  public String getValue(String keyName) {
    String s = mapProps.get(keyName);
    return s!=null?s:"";
  }

  public String getValue(String keyName, String defaultValue) {
    String value = defaultValue;
    if (!StringUtils.isEmpty(keyName)) {
      value = mapProps.get(keyName);
    }
    return value!=null?value:"";
  }

  public Map<String, String> getMapProps() {
    return mapProps;
  }

  public void setMapProps(Map<String, String> mapProps) {
    this.mapProps = mapProps;
  }
}
