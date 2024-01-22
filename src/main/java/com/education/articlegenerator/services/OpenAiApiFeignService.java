package com.education.articlegenerator.services;

import com.education.articlegenerator.configurations.OpenAiApiProperties;
import com.education.articlegenerator.dtos.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dtos.openai.OpenAiChatCompletionResponse;
import com.education.articlegenerator.dtos.ArticleDto;
import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.entities.OpenAiRequestAttributes;
import com.education.articlegenerator.integration.OpenAiFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiApiFeignService {
    private final OpenAiFeignClient openAiFeignClient;
    private final OpenAiApiProperties openAiApiProperties;
    private final OpenAiRequestAttributesService openAiRequestAttributesService;

    public List<ArticleTopicDto> generateTopics(String tags) throws JsonProcessingException {
        OpenAiRequestAttributes openAiRequestAttributes = openAiRequestAttributesService.getByNameCachable(openAiApiProperties.getArticleTopicKey());
        OpenAiChatCompletionRequest request = OpenAiChatCompletionRequest.makeRequest(
                String.format(openAiRequestAttributes.getRequestMessage(), tags)
        );
        OpenAiChatCompletionResponse response =
                openAiFeignClient.generate(
                        openAiRequestAttributes.getKey(), request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ArticleTopicDto> result = objectMapper.readValue(
                response.getChoices().get(0).getMessage().getContent(),
                new TypeReference<List<ArticleTopicDto>>()
                {});

        return result;
    }

    public ArticleDto generateArticle(String topicTitle) throws JsonProcessingException {
        OpenAiRequestAttributes openAiRequestAttributes = openAiRequestAttributesService.getByNameCachable(openAiApiProperties.getArticleKey());
        OpenAiChatCompletionRequest request = OpenAiChatCompletionRequest.makeRequest(
                String.format(openAiRequestAttributes.getRequestMessage(), topicTitle));
        OpenAiChatCompletionResponse response =
                openAiFeignClient.generate(
                        openAiRequestAttributes.getKey(), request);

        ObjectMapper objectMapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .build();
        ArticleDto result = objectMapper.readerFor(ArticleDto.class).readValue(
                response.getChoices().get(0).getMessage().getContent());
        return result;
    }

}
