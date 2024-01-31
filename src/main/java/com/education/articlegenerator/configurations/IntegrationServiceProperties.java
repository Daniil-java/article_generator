package com.education.articlegenerator.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
@Deprecated
public class IntegrationServiceProperties {
    @Value("${integrations.connect-timeout:5000}")
    private Integer connectTimeout;
    @Value("${integrations.read-timeout:1000000}")
    private Integer readTimeout;
    @Value("${integrations.write-timeout:5000}")
    private Integer writeTimeout;
}
