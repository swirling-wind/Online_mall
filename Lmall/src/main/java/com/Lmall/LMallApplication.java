package com.Lmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 13
 * @qq交流群 658365129
 * @email 2449207463@qq.com
 *
 */
@MapperScan("com.Lmall.dao")
@SpringBootApplication
public class LMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(LMallApplication.class, args);
    }
}
