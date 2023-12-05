package com.education.articlegenerator.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class IntegrationServiceProperties {
    @Value("5000")
    private Integer connectTimeout;
    @Value("1000000")
    private Integer readTimeout;
    @Value("5000")
    private Integer writeTimeout;
}
