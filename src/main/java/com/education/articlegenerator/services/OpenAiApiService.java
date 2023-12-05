package com.education.articlegenerator.services;

import com.education.articlegenerator.entities.OpenAiKey;
import com.education.articlegenerator.properties.IntegrationServiceProperties;
import com.education.articlegenerator.repositories.OpenAiApiRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

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


    public List<String> generateTopics(String request) {
        String filter = "Придумай темы для статей, используя следующие теги. Раздели их знаком ; .";
        String topics = makeRequest("ArticleTopicKey", filter + request);
        return Arrays.asList(topics.split(";"));
    }

    public List<String> generateArticle(String topicTitle) {
        String topics = "article1: " + topicTitle + ";"
                + "\narticle2: " + topicTitle + ";";
        return Arrays.asList(topics.split(";"));
    }

    private String makeRequest(String keyName, String request) {
        OpenAiKey openAiKey = openAiApiRepository.findByName(keyName)
                .orElseThrow(() -> new RuntimeException(
                        "Key is not exist"));

        String postData = String.format("{ \"model\": \"gpt-3.5-turbo\", " +
                "\"messages\": " +
                "[{\"role\": \"user\", " +
                "\"content\": \"%s\"}]}", request);

        Mono<String> response = getWebClient().post()
                .uri("chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + openAiKey.getKey())
                .body(BodyInserters.fromValue(postData))
                .retrieve()
                .bodyToMono(String.class);

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

