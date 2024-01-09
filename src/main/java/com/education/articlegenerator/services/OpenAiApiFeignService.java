package com.education.articlegenerator.services;

import com.education.articlegenerator.configurations.OpenAiApiProperties;
import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dtos.openai.OpenAiChatCompletionResponse;
import com.education.articlegenerator.dtos.ArticleDto;
import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.OpenAiKey;
import com.education.articlegenerator.integration.OpenAiFeignClient;
import com.education.articlegenerator.repositories.OpenAiApiRepository;
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
    private final OpenAiApiRepository openAiApiRepository;
    private final OpenAiApiProperties openAiApiProperties;

    public List<ArticleTopicDto> generateTopics(String tags) {
        OpenAiKey openAiKey = openAiApiRepository.findByName(openAiApiProperties.getArticleTopicKey())
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.KEY_DOESNT_EXIST));
        OpenAiChatCompletionRequest request = OpenAiChatCompletionRequest.makeRequest(
                String.format(openAiKey.getResponseMessage(), tags)
        );
        OpenAiChatCompletionResponse response =
                openAiFeignClient.generate(
                        openAiKey.getKey(), request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ArticleTopicDto> result = null;
        try {
            result = objectMapper.readValue(
                    response.getChoices().get(0).getMessage().getContent(),
                    new TypeReference<List<ArticleTopicDto>>()
                    {});
        } catch (JsonProcessingException e) {
            throw new ErrorResponseException(ErrorStatus.OPENAI_INCORRECT_ANSWER, e);
        }
        return result;
    }

    public ArticleDto generateArticle(String topicTitle) {
        OpenAiKey openAiKey = openAiApiRepository.findByName(openAiApiProperties.getArticleKey())
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.KEY_DOESNT_EXIST));
        OpenAiChatCompletionRequest request = OpenAiChatCompletionRequest.makeRequest(
                String.format(openAiKey.getResponseMessage(), topicTitle));
        OpenAiChatCompletionResponse response =
                openAiFeignClient.generate(
                        openAiKey.getKey(), request);

        ObjectMapper objectMapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                .build();
        ArticleDto result = null;
        try {
            result = objectMapper.readerFor(ArticleDto.class).readValue(
                    response.getChoices().get(0).getMessage().getContent());
        } catch (JsonProcessingException e) {
            throw new ErrorResponseException(ErrorStatus.OPENAI_INCORRECT_ANSWER, e);
        }
        return result;
    }


}
