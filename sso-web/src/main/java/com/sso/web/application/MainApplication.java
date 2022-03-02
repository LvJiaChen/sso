package com.sso.web.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.sso.common.mapper")
@ComponentScan(basePackages = {
        "com.sso.web.application.filter",
        "com.sso.service",
        "com.sso.web.application.Controller",
        "com.sso.common.handler",
        "com.sso.common.mapper",
        "com.sso.web.application.config"
})
@EnableTransactionManagement
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context= SpringApplication.run(MainApplication.class, args);
        String[] names=context.getBeanDefinitionNames();
        System.out.println("********************************************WEB启动完成***************************************");
    }

}
