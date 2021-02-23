package com.atguigu.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/***
 *description:
 *created by 10136 on 2021/2/22
 *
 */
public interface DauMapper {
    //1.获取当天数据的方法
    public Integer selectDauTotal(String date);
    //2.获取小时数据的方法
    public List<Map> selectDauTotalHourMap(String date);
    //3.获取当天新增用户的方法
//    Integer selectNewMidTotal(String date);


}
