package com.education.articlegenerator.services;

import com.education.articlegenerator.dto.openai.Message;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionResponse;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.OpenAiKey;
import com.education.articlegenerator.integration.FeignIntegrationClient;
import com.education.articlegenerator.repositories.OpenAiApiRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiApiFeignService {
    private final FeignIntegrationClient feignIntegrationClient;

    public List<ArticleTopic> generateTopics(String tags) {
        String filter = String.format(
                "Это поле/тема или список тегов: %s. Необходимо создать 10 заголовков. " +
                        "Тема должна быть меньше 255 символов. Предоставьте ответ с " +
                        "помощью этой схемы JSON: '[{\"topicTitle\": \"Вот название статьи\"}]'. " +
                        "Я хочу, чтобы вы генерировали заголовок только в формате " +
                        "JSON без каких-либо других объяснений.", tags
        );

        OpenAiChatCompletionRequest request = makeRequest("ArticleTopicKey", filter);
        OpenAiChatCompletionResponse response = feignIntegrationClient.generate(request);


        ObjectMapper objectMapper = new ObjectMapper();
        List<ArticleTopic> result = null;
        try {
            result = objectMapper.readValue(response.getChoices().get(0).getMessage().getContent(), new TypeReference<List<ArticleTopic>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private OpenAiChatCompletionRequest makeRequest(String keyName, String request) {
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message()
                .setRole("user")
                .setContent(request)
        );

        OpenAiChatCompletionRequest chatRequest = new OpenAiChatCompletionRequest()
                .setModel("gpt-3.5-turbo-1106")
                .setMessages(messages)
                .setTemperature(0.7f);

        return chatRequest;
    }

//    public List<Article> generateArticle(String topicTitle) {
//
//    }
}
