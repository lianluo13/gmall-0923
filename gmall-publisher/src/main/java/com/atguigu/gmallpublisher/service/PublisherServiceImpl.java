package com.atguigu.gmallpublisher.service;

import com.atguigu.gmallpublisher.mapper.DauMapper;
import com.atguigu.gmallpublisher.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/***
 *description:
 *created by 10136 on 2021/2/22
 *
 */
@Service
public class PublisherServiceImpl implements PublisherService{
    //定义daumapper
    @Autowired
    private DauMapper dauMapper;
    @Autowired
    private OrderMapper orderMapper;
    //获取daumapper查询到的总dau
    @Override
    public int getDauTotal(String date) {
        return dauMapper.selectDauTotal(date);
    }

    //获取小时DAU
    @Override
    public Map getDauHours(String date) {
        LinkedHashMap dauHourMap = new LinkedHashMap();
        //从mapper过来的查询结果
        List<Map> dauHourList = dauMapper.selectDauTotalHourMap(date);
        for (Map map : dauHourList) {
            dauHourMap.put(map.get("LH"),map.get("CT"));
        }
        return dauHourMap;
    }

    @Override
    public Double getOrderAmount(String date) {
        return orderMapper.selectOrderAmountTotal(date);
    }

    @Override
    public Map getOrderAmountHour(String date) {
        List<Map> mapList = orderMapper.selectOrderAmountHourMap(date);
        Map orderAmountHourMap = new HashMap();
        for (Map map : mapList) {
            orderAmountHourMap.put(map.get("CREATE_HOUR"),map.get("SUM_AMOUNT"));

        }
        return orderAmountHourMap;
    }

//    @Override
//    public int getNewMidTotal(String date) {
//        return dauMapper.selectNewMidTotal(date);
//    }
}
