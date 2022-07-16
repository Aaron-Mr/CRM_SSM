package com.wangxiaoqi.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/toIndex")
    public String toIndex(){
        System.out.println("123");
        return "index";
    }
}
