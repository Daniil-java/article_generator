package com.education.articlegenerator.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties
public class OpenAiApiProperties {
    @Value("${integrations.openai-api.article-key:ArticleKey}")
    private final String articleKey;
    @Value("${integrations.openai-api.articletopic-key:ArticleTopicKey}")
    private final String articleTopicKey;

}
