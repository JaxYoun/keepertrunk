package com.troy.keeper.message.service;

import com.troy.keeper.message.config.Const;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by yg on 2017/6/19.
 */
public interface Barista {
    @Input(Const.INPUT_CHANNEL)
    SubscribableChannel input();

    @Output(Const.OUTPUT_CHANNEL)
    MessageChannel outPut();
}
