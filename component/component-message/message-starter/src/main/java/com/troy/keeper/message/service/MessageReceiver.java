package com.troy.keeper.message.service;

import com.troy.keeper.message.config.Const;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * Created by yg on 2017/6/19.
 */
@EnableBinding(Barista.class)
public class MessageReceiver {
    private static final Log logger = LogFactory.getLog(MessageReceiver.class);

    @StreamListener(Const.INPUT_CHANNEL)
    public void receiver(Message<?> message){
        Object msg = message.getPayload();
        logger.info("接收消息:" + msg);
    }
}
