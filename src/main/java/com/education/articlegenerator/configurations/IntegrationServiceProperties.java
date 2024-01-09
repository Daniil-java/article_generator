package com.education.articlegenerator.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class IntegrationServiceProperties {
    @Value("${integrations.connect-timeout}")
    private Integer connectTimeout;
    @Value("${integrations.read-timeout}")
    private Integer readTimeout;
    @Value("${integrations.write-timeout}")
    private Integer writeTimeout;
}
