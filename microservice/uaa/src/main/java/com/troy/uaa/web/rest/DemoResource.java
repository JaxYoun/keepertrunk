package com.troy.uaa.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: xiongzhan
 * Date: 17-4-21
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/demo")
public class DemoResource {


    @RequestMapping(value = "/hi")
    @ResponseBody
    public String login(){
        return "hi";
    }


}
