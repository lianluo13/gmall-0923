package com.atguigu.gmallpublisher.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmallpublisher.service.PublisherService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 *description:
 *created by 10136 on 2021/2/22
 *
 */

@RestController
public class PublisherController {
    @Autowired
    private PublisherService publisherService;

    @GetMapping("realtime-total")
    public String realtimeDate(@RequestParam("date")String date){
        //创建一个List用来接收数据,这个list里面的数据是用来web展示的
        ArrayList<Map> list = new ArrayList<>();

        //从service中获取日活总数
        int dauTotal = publisherService.getDauTotal(date);
        //创建一个map用来存储日活总数及相关信息
        Map<String,Object> dauMap = new LinkedHashMap();
        //向daumap添加数据,描述性数据
        dauMap.put("id","dau");
        dauMap.put("name","当日日活");
        dauMap.put("value",dauTotal);
        list.add(dauMap);

        //新增用户
        Map newMidMap = new LinkedHashMap<String,Object>();
        newMidMap.put("id","new_mid");
        newMidMap.put("name","新增用户");
        newMidMap.put("value",233);
        list.add(newMidMap);

        //新增交易总额
        Map orderAmountMap= new LinkedHashMap<>();
        //添加信息
        orderAmountMap.put("id","order_amount");
        orderAmountMap.put("name","新增交易额");
        Double orderAmount = publisherService.getOrderAmount(date);
        orderAmountMap.put("value",orderAmount);
        //把新增交易额信息添加的list中
        list.add(orderAmountMap);

        return JSON.toJSONString(list);
    }

    @RequestMapping("realtime-hours")
    public  String getDauHourTotal(@RequestParam("id")String id,
                                   @RequestParam("date")String date){
        //创建Map集合用来存放结果数据
        //根据结果要求,{"yesterday":{"11":383,"12":123,"17":88,"19":200 },
        //"today":{"12":38,"13":1233,"17":123,"19":688 }}
        LinkedHashMap<String,Map> result = new LinkedHashMap<>();


        //3.求昨天日期
        String yesterday = LocalDate.parse(date).plusDays(-1).toString();

        Map todayMap =null;
        Map yesterdayMap =null;
        if (id.equals("dau")){
            //把昨天今天的日活数据放入list
            todayMap = publisherService.getDauHours(date);
            yesterdayMap = publisherService.getDauHours(yesterday);
        }else if (id.equals("order_amount")){
            //把今天昨天的交易额数据放入list中
            todayMap=publisherService.getOrderAmountHour(date);
            yesterdayMap=publisherService.getOrderAmountHour(yesterday);
        }
        //将数据保存到map集合中
        result.put("yesterday",yesterdayMap);
        result.put("today", todayMap);
        //返回json格式结果
        return JSON.toJSONString(result);
    }
}
