package com.atguigu.gmallpublisher.service;

import java.util.Map;

/***
 *description:
 *created by 10136 on 2021/2/22
 *
 */
public interface PublisherService {
    public int getDauTotal(String date);
    public Map getDauHours(String date);

//    public int getNewMidTotal(String date);

    public Double getOrderAmount(String date);

    public Map getOrderAmountHour(String date);

}
