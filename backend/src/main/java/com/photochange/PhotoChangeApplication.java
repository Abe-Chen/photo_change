package com.photochange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 人物姿势改变应用的主应用类
 * 用于启动Spring Boot应用
 */
@SpringBootApplication
public class PhotoChangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoChangeApplication.class, args);
    }
}