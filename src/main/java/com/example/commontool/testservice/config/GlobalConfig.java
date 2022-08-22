package com.example.commontool.testservice.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName GlobalConfig
 * @Description TODO 类描述
 * @Author tianhuan
 * @Date 2022/8/4
 **/
//@Configuration
public class GlobalConfig {

    @Bean
   public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setBasePackage("com.example.commontool.**.mapper");
        return scannerConfigurer;
    }
}
