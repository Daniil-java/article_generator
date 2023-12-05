package com.education.articlegenerator.services;

import com.education.articlegenerator.dto.openai.Message;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionResponse;
import com.education.articlegenerator.entities.OpenAiKey;
import com.education.articlegenerator.properties.IntegrationServiceProperties;
import com.education.articlegenerator.repositories.OpenAiApiRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Arrays;
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


    public List<String> generateTopics(String tags) {
        String filter = "Придумай темы для статей, используя следующие теги.";
        OpenAiChatCompletionResponse topics = makeRequest("ArticleTopicKey", filter + tags);
        String result = topics.getChoices().get(0).getMessage().getContent();
        return Arrays.asList(result.split("\n"));
    }

    public List<String> generateArticle(String topicTitle) {
        String filter = "Придумай несколько разных статей, примерно на 1000 символов каждая , по теме: ";
        OpenAiChatCompletionResponse topics = makeRequest("ArticleTopicKey", filter + topicTitle);
        String result = topics.getChoices().get(0).getMessage().getContent();
        return Arrays.asList(result.split("\n"));
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
                .setModel("gpt-3.5-turbo")
                .setMessages(messages);

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

