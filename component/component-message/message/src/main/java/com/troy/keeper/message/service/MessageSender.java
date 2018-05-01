package com.troy.keeper.message.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by yg on 2017/6/19.
 */
@Service
@Component
public class MessageSender {
    private static final Log logger = LogFactory.getLog(MessageSender.class);
    @Autowired
    private Barista source;

    // 发送消息
    public void sendMessage(String msg) {
        source.outPut().send(MessageBuilder.withPayload(msg).build());
        logger.info("发送消息:" + msg);
    }
}
