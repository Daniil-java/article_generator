package com.education.articlegenerator.integration;

import com.education.articlegenerator.configurations.FeignConfig;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dto.openai.OpenAiChatCompletionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "feignIntegrationClient",
        url = "https://api.openai.com/v1/",
        configuration = FeignConfig.class
)
public interface FeignIntegrationClient {
    @PostMapping("chat/completions")
    OpenAiChatCompletionResponse generate(@RequestBody OpenAiChatCompletionRequest request);

}
