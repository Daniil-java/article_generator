package com.education.articlegenerator.configurations;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor interceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer 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");
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}
