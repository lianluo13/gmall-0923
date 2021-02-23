package com.atguigu.app

import com.alibaba.fastjson.JSON
import com.atguigu.bean.OrderInfo
import com.atguigu.constant.GmallConstants
import com.atguigu.utils.MyKafkaUtil
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}



/** *
 * description:
 * created by 10136 on 2021/2/22
 *
 */
object OrderApp {
  def main(args: Array[String]): Unit = {
    //1.获取配置SparkConf
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("order_app")
    //2.获取StreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    //3.获取kafka数据
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(GmallConstants.KAFKA_TOPIC_ORDER, ssc)

    //4.将数据转换为样例类，并补全数据
    val orderInfoDStream: DStream[OrderInfo] = kafkaDStream.mapPartitions(
      partition => {
        partition.map(record => {
          //将数据转换为样例类
          val orderInfo: OrderInfo = JSON.parseObject(record.value(), classOf[OrderInfo])

          //补全create_date
          orderInfo.create_date = orderInfo.create_time.split(" ")(0)
          orderInfo.create_hour = orderInfo.create_time.split(" ")(1).split(":")(0)

          //将手机号脱敏
          orderInfo.consignee_tel = orderInfo.consignee_tel.substring(0, 3) + "********"
          orderInfo
        })
      }
    )
    import  org.apache.phoenix.spark._
    //5.将数据写入Phoenix
    orderInfoDStream.foreachRDD(rdd=>{
      rdd.saveToPhoenix("GMALL0923_ORDER_INFO",
        Seq("ID", "PROVINCE_ID", "CONSIGNEE", "ORDER_COMMENT", "CONSIGNEE_TEL", "ORDER_STATUS", "PAYMENT_WAY", "USER_ID", "IMG_URL", "TOTAL_AMOUNT", "EXPIRE_TIME", "DELIVERY_ADDRESS", "CREATE_TIME", "OPERATE_TIME", "TRACKING_NO", "PARENT_ORDER_ID", "OUT_TRADE_NO", "TRADE_BODY", "CREATE_DATE", "CREATE_HOUR"),
        HBaseConfiguration.create(),
        Some("hadoop102,hadoop103,hadoop104:2181"))
    })

    //6.开启任务
    ssc.start()
    ssc.awaitTermination()
  }
}
