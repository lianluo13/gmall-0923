package com.atguigu.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/***
 *description:
 *created by 10136 on 2021/2/22
 *
 */
public interface OrderMapper {
    //当日交易额总数
    public Double selectOrderAmountTotal(String date);

    //当日校验分时明细
    public List<Map> selectOrderAmountHourMap(String date);
}
