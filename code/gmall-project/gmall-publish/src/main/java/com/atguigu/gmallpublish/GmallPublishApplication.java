package com.atguigu.gmallpublish;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.gmallpublish.mapper")
public class GmallPublishApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPublishApplication.class, args);
    }

}
