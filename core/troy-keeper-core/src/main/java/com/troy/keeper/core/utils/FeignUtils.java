package com.troy.keeper.core.utils;

import com.troy.keeper.core.config.CloudProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-5-11
 * Time: 下午1:28
 * To change this template use File | Settings | File Templates.
 */
public class FeignUtils {

    private static final Log LOGGER = LogFactory.getLog(FeignUtils.class);

    public static String url(String serviceName, String url) {
        if (StringUtils.isEmpty(url) && isCombine(serviceName)) {
            return CloudProperties.App.Combine.address();
        }
        return url;
    }

    private static boolean isCombine(String serviceName) {
        return CloudProperties.App.Combine.isCombine(serviceName);
    }


}
