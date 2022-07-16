package com.wangxiaoqi.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkbenchMainController {
    @RequestMapping("/workbench/main/mainIndex.do")
    public String main(){
        return "workbench/main/index";
    }
}
