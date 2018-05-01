package com.troy.uaa;

import com.troy.uaa.web.rest.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by yg on 2017/4/18.
 */
@Component
public class MyStartupRunner implements CommandLineRunner {
    @Autowired
    private UserResource userResource;
    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
        userResource.refreshUser();
        System.out.println(">>>>>>>>>>>>>>>执行加载数据等操作完毕<<<<<<<<<<<<<");
    }
}
