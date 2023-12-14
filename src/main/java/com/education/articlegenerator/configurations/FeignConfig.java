package com.education.articlegenerator.configurations;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor interceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}
