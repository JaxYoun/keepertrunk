package com.troy.keeper.system.intercept;

import com.google.common.collect.Maps;
import com.troy.keeper.core.utils.SpringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by Harry on 2017/9/6.
 */
class InterceptServiceInstance {

    private static Map<Class,List<?>> delegateServices = Maps.newHashMapWithExpectedSize(3);

    static List<?> delegateServices(Class self, Class delegate) {
        List<?> result = delegateServices.get(self);
        if (CollectionUtils.isEmpty(result)) {
            Map beansOfType = SpringUtils.getBeansOfType(delegate);
            if (MapUtils.isNotEmpty(beansOfType)) {
                List list= (List) beansOfType.values().stream().filter(b -> !self.isAssignableFrom(b.getClass())).collect(toList());
                if (CollectionUtils.isNotEmpty(list)){
                    delegateServices.put(self, result);
                    result = list;
                }
            }
        }
        return result;
    }
}
