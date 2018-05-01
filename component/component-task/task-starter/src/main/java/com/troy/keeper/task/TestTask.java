package com.troy.keeper.task;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by yg on 2017/6/23.
 */
@Component("TestTask")
public class TestTask {
    public static final Logger log = Logger.getLogger(TestTask.class);

    /**
     * 测试任务hello
     */
    public void hello(){
        log.info("定时任务1开始............");
        log.info("hello!");
        log.info("定时任务1结束............");
    }

    /**
     * 测试任务world
     */
    public void world(){
        log.info("定时任务2开始............");
        Integer a = new Integer("aa");
        log.info("world!");
        log.info("定时任务2结束............");
    }
}
