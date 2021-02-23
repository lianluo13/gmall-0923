package com.atguigu.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/***
 *description:
 *created by 10136 on 2021/2/22
 *
 */

//@Controller
@RestController
public class ControllerTest {
    @RequestMapping("test01")
   //@ResponseBody
    public String test01(){
        System.out.println("123");
        return "success";
    }

    @RequestMapping("test02")
    //@ResponseBody
    public String test02(@RequestParam("name")String name,
                         @RequestParam("age")String age){
        System.out.println("123");
        return name+": "+age;
    }
}
















