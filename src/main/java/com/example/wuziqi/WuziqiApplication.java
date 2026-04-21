package com.example.wuziqi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.wuziqi.mapper")
public class WuziqiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WuziqiApplication.class, args);
    }
}
