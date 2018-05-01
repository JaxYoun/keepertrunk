package com.troy.keeper.monomer.demo.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yg on 2018/1/31.
 */
@Controller
public class FreeMarkerTestController {
    @RequestMapping("/freeTest/test")
    public String testFtl(ModelMap model){
        model.addAttribute("name","FreeMarker 模版引擎 ");
        return "test";
    }
}
