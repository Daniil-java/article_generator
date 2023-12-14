package com.education.articlegenerator.services;

import com.education.articlegenerator.dto.openai.Message;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionResponse;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.OpenAiKey;
import com.education.articlegenerator.properties.IntegrationServiceProperties;
import com.education.articlegenerator.repositories.OpenAiApiRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(
        {IntegrationServiceProperties.class}
)
public class OpenAiApiService {
    private WebClient webClient;
    private final IntegrationServiceProperties isp;
    private final OpenAiApiRepository openAiApiRepository;
    @Value("https://api.openai.com/v1/")
    private String openAiUrl;

    public List<ArticleTopic> generateTopics(String tags) {

        String filter = String.format(
                "Это поле/тема или список тегов: %s. Необходимо создать 10 заголовков. " +
                        "Тема должна быть меньше 255 символов. Предоставьте ответ с " +
                        "помощью этой схемы JSON: '[{\"topicTitle\": \"Вот название статьи\"}]'. " +
                        "Я хочу, чтобы вы генерировали заголовок только в формате " +
                        "JSON без каких-либо других объяснений.", tags);

        OpenAiChatCompletionResponse topics = makeRequest("ArticleTopicKey", filter);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ArticleTopic> result = null;
        try {
            result = objectMapper.readValue(topics.getChoices().get(0).getMessage().getContent(), new TypeReference<List<ArticleTopic>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Article> generateArticle(String topicTitle) {
        String filter = String.format(
                "Пожалуйста, сгенерируйте статью на тему: '%s' с длиной от 400 слов. " +
                        "В статье должны быть следующие разделы: " +
                        "название, аннотация, ключевые слова, " +
                        "введение, обзор литературы, основная часть " +
                        "(методология, результаты), выводы и " +
                        "дальнейшие перспективы исследования, " +
                        "а также список литературы. Предоставьте ответ с " +
                        "помощью этой схемы JSON: [{\"articleBody\": \"Текст ответа\"}]. " +
                        "Я хочу, чтобы вы генерировали статью только в формате JSON " +
                        "без каких-либо других объяснений." , topicTitle);

        OpenAiChatCompletionResponse topics = makeRequest("ArticleTopicKey", filter);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Article> result = null;
        try {
            result = objectMapper.readValue(topics.getChoices().get(0).getMessage().getContent(), new TypeReference<List<Article>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private OpenAiChatCompletionResponse makeRequest(String keyName, String request) {
        OpenAiKey openAiKey = openAiApiRepository.findByName(keyName)
                .orElseThrow(() -> new RuntimeException(
                        "Key is not exist"));

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message()
                .setRole("user")
                .setContent(request)
        );

        OpenAiChatCompletionRequest chatRequest = new OpenAiChatCompletionRequest()
                .setModel("gpt-3.5-turbo-1106")
                .setMessages(messages)
                .setTemperature(0.7f);

        Mono<OpenAiChatCompletionResponse> response = getWebClient().post()
                .uri("chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + openAiKey.getKey())
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(OpenAiChatCompletionResponse.class);

        return response.block();
    }

    private WebClient getWebClient() {
        if (webClient == null) {
            HttpClient httpClient = HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, isp.getConnectTimeout())
                    .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(isp.getReadTimeout(), TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(isp.getWriteTimeout(), TimeUnit.MILLISECONDS)));
            webClient = WebClient.builder()
                    .baseUrl(openAiUrl)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        }
        return webClient;
    }
}

