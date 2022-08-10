package com.chen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chen")
public class SpringbootAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAdvancedApplication.class, args);
    }

}
