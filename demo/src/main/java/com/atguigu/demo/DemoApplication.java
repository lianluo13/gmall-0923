package com.atguigu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.Controller 接收请求
 *
 * 2.Service  处理请求
 *
 * 3.Dao  + JDBC(My B) =  mapper 操作数据
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
