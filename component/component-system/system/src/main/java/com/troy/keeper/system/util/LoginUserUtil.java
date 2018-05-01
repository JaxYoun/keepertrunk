package com.troy.keeper.system.util;

import com.troy.keeper.core.utils.JsonUtils;
import com.troy.keeper.system.security.SysAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/13.
 */
public class LoginUserUtil {

    /**
     * 将用户数据转换为MAP对象
     *
     * @param redisData
     * @return
     */
    public static Map<String, String> getUserRedisData(String redisData) {
        return StringToMap(redisData);
    }

    /**
     * 获取缓存用户数据的RedisKey
     *
     * @param userId
     * @return
     */
    public static String getUserRedisKey(Long userId) {
        StringBuffer key = new StringBuffer("loginUser-");
        key.append(userId.toString());
        return key.toString();
    }

//    private static Map<String, String> StringToMap(String mapText) {
//        JSONObject data = JSONObject.fromObject(mapText);
//        Map<String, String> userData = (Map<String, String>) data;
//        return userData;
//    }

    private static Map<String, String> StringToMap(String mapText) {
//        JSONObject data = JsonUtils.toObject(mapText,JSONObject.class);
        Map < String,String > map = JsonUtils.toObject(mapText,Map.class);
        return map;
    }


}
