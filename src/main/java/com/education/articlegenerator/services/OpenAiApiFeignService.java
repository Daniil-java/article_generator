package com.education.articlegenerator.services;

import com.education.articlegenerator.dto.openai.Message;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionResponse;
import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ArticleDto;
import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.OpenAiKey;
import com.education.articlegenerator.integration.OpenAiFeignClient;
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
    private final OpenAiFeignClient openAiFeignClient;
    private final OpenAiApiRepository openAiApiRepository;

    public List<ArticleTopicDto> generateTopics(String tags) {
        String filter = String.format(
                "Это поле/тема или список тегов: %s. Необходимо создать 10 заголовков. " +
                        "Тема должна быть меньше 255 символов. Предоставьте ответ с " +
                        "помощью этой схемы JSON: '[{\"topicTitle\": \"Вот название статьи\"}]'. " +
                        "Я хочу, чтобы вы генерировали заголовок только в формате " +
                        "JSON без каких-либо других объяснений.", tags
        );

        OpenAiChatCompletionRequest request = makeRequest(filter);
        OpenAiKey openAiKey = openAiApiRepository.findByName("ArticleTopicKey")
                .orElseThrow(() -> new RuntimeException(
                        "Key is not exist"));
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
        String filter = String.format(
                "Предоставьте ответ с помощью этой схемы JSON. : " +
                        "\"{\"articleBody\": \"Весь текст ответа\"}\". Я хочу, " +
                        "чтобы вы генерировали статью только в формате JSON без " +
                        "каких-либо других объяснений. В теле JSON, за пределами " +
                        "\"Весь текст ответа\".Пожалуйста, сгенерируйте статью " +
                        "  на тему: \"%s\". Длина текса от 200 слов. " , topicTitle);

        OpenAiChatCompletionRequest request = makeRequest(filter);
        OpenAiKey openAiKey = openAiApiRepository.findByName("ArticleKey")
                .orElseThrow(() -> new RuntimeException(
                        "Key is not exist"));
        OpenAiChatCompletionResponse response =
                openAiFeignClient.generate(
                        openAiKey.getKey(), request);
        ObjectMapper objectMapper = new ObjectMapper();
        ArticleDto result = null;
        try {
            result = objectMapper.readValue(
                    response.getChoices().get(0).getMessage().getContent(),
                    new TypeReference<ArticleDto>()
                    {});
        } catch (JsonProcessingException e) {
            throw new ErrorResponseException(ErrorStatus.OPENAI_INCORRECT_ANSWER, e);
        }
        return result;
    }

    private OpenAiChatCompletionRequest makeRequest(String request) {
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

}
