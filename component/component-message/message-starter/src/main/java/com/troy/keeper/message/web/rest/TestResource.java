package com.troy.keeper.message.web.rest;

import com.troy.keeper.message.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yg on 2017/6/19.
 */
@RestController
public class TestResource {

    @Autowired
    private MessageSender messageSender;

    @RequestMapping(value = "/api/test/{msg}", method = RequestMethod.GET)
    public void test(@PathVariable String msg){
        messageSender.sendMessage(msg);
    }
}
