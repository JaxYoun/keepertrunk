package com.troy.keeper.system.start;

import com.troy.keeper.core.base.service.RedisService;
import com.troy.keeper.system.domain.SmUser;
import com.troy.keeper.system.service.SmInitService;
import com.troy.keeper.system.service.SmUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    private SmUserService smUserService;

    @Autowired
    private SmInitService smInitService;

    @Autowired
    private RedisService redisService;

    private static final String KEY_NAME = "user_";

    private static final String UNDERLINE = "_";

    /**
     * 应用启动完成后执行
     *
     * @param applicationReadyEvent
     */
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("----------系统管理 启动加载Start----------");
        if (!smInitService.checkSystemDataNotNull()){
            smInitService.initSystemData();
        }
        getUserIdAndUserNameToRedis();
        log.info("----------系统管理 启动加载End  ----------");
    }

    /**
     * 获取用户ID和名称到Redis
     */
    private void getUserIdAndUserNameToRedis() {
        List<SmUser> list = smUserService.getUserIdAndUserName();
        log.info("----------获取用户信息完成 正在缓存Redis----------");
        putRedisUserIdAndUserName(list);
        log.info("----------完成用户信息缓存Redis----------");
    }

    /**
     * 更新用户信息到Redis中
     *
     * @param userList
     */
    private void putRedisUserIdAndUserName(List<SmUser> userList) {
        StringBuilder userId = new StringBuilder(KEY_NAME);
        for (int i = 0; i < userList.size(); i++) {
            if (i == 0) {
                userId.append(userList.get(i).getId());
                redisService.set(userId.toString(), userList.get(i).getUserName());
            } else {
                userId.replace(userId.indexOf(UNDERLINE) + 1, userId.length(), userList.get(i).getId().toString());
                redisService.set(userId.toString(), userList.get(i).getUserName());
            }
        }
    }
}
