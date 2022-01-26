package com.sso.web.application.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author lvxiaozuo
 * @date 2022/1/7 18:00
 */
@Configuration
public class CorsConfig {

    private CorsConfiguration corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");  //允许所有域名访问
        corsConfiguration.addAllowedHeader("*");  //允许所有请求头
        corsConfiguration.addAllowedMethod("*");  //允许所有的请求类型
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true); //允许请求携带验证信息（cookie）
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        //存储request与跨域配置信息的容器，基于url的映射
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig());
        return new CorsFilter(source);
    }
}
