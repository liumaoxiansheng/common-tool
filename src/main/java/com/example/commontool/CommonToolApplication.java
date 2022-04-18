package com.example.commontool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.commontool.**.mapper")
public class CommonToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonToolApplication.class, args);
    }

}
