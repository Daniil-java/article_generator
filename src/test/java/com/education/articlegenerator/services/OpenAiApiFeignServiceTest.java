package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.openai.Choice;
import com.education.articlegenerator.dtos.openai.Message;
import com.education.articlegenerator.dtos.openai.OpenAiChatCompletionRequest;
import com.education.articlegenerator.dtos.openai.OpenAiChatCompletionResponse;
import com.education.articlegenerator.entities.OpenAiRequestAttributes;
import com.education.articlegenerator.integration.OpenAiFeignClient;
import com.education.articlegenerator.repositories.OpenAiRequestAttributesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = OpenAiApiFeignService.class)
@Disabled
public class OpenAiApiFeignServiceTest {
    @Autowired
    private OpenAiApiFeignService openAiApiFeignService;
    @MockBean
    private OpenAiFeignClient openAiFeignClient;
    @MockBean
    private OpenAiRequestAttributesRepository openAiRequestAttributesRepository;

    private final String TRUE_OPENAI_TOPIC_KEY = "ArticleTopicKey";
    private final String TRUE_OPENAI_ARTICLE_KEY = "ArticleKey";
    private final String MESSAGE_TOPIC_CONTENT = "[{\"topicTitle\": \"Вот название статьи\"}]";

    private final String MESSAGE_ARTICLE_CONTENT = "{\"articleBody\": \"Весь текст ответа\"}";
    private final String TAGS = "text";
    private final String FILTER_TOPIC = String.format(
            "Это поле/тема или список тегов: %s. Необходимо создать 10 заголовков. " +
                    "Тема должна быть меньше 255 символов. Предоставьте ответ с " +
                    "помощью этой схемы JSON: '[{\"topicTitle\": \"Вот название статьи\"}]'. " +
                    "Я хочу, чтобы вы генерировали заголовок только в формате " +
                    "JSON без каких-либо других объяснений.", TAGS
    );
    private final String FILTER_ARTICLE = String.format(
            "Предоставьте ответ с помощью этой схемы JSON. : " +
                    "\"{\"articleBody\": \"Весь текст ответа\"}\". Я хочу, " +
                    "чтобы вы генерировали статью только в формате JSON без " +
                    "каких-либо других объяснений. В теле JSON, за пределами " +
                    "\"Весь текст ответа\".Пожалуйста, сгенерируйте статью " +
                    "  на тему: \"%s\". Длина текса от 200 слов. " , TAGS
    );
    private final String TOPICTEST_RESULT_EXPECTED = "Вот название статьи";
    private final String ARTICLETEST_RESULT_EXPECTED = "Весь текст ответа";

    @BeforeEach
    public void setUp() {
        OpenAiRequestAttributes openAiRequestAttributes = new OpenAiRequestAttributes();
        Mockito.doReturn(Optional.of(openAiRequestAttributes)).when(openAiRequestAttributesRepository).findByName(TRUE_OPENAI_TOPIC_KEY);
        Mockito.doReturn(Optional.of(openAiRequestAttributes)).when(openAiRequestAttributesRepository).findByName(TRUE_OPENAI_ARTICLE_KEY);

        OpenAiChatCompletionResponse openAiChatCompletionResponse = makeResponse(MESSAGE_TOPIC_CONTENT);
        OpenAiChatCompletionResponse openAiChatCompletionResponseArticle = makeResponse(MESSAGE_ARTICLE_CONTENT);
        OpenAiChatCompletionRequest request = OpenAiChatCompletionRequest.makeRequest(FILTER_TOPIC);
        OpenAiChatCompletionRequest requestArticle = OpenAiChatCompletionRequest.makeRequest(FILTER_ARTICLE);
        Mockito.doReturn(openAiChatCompletionResponse).when(openAiFeignClient)
                .generate(openAiRequestAttributes.getKey(), request);
        Mockito.doReturn(openAiChatCompletionResponseArticle).when(openAiFeignClient)
                .generate(openAiRequestAttributes.getKey(), requestArticle);

    }

//    @Test
//    public void generateTopicsTest() {
//        Assertions.assertEquals(TOPICTEST_RESULT_EXPECTED,
//                openAiApiFeignService.generateTopics(TAGS).get(0).getTopicTitle());
//    }

    @Test
    public void generateTopicsErrorKeyTest() {
        Mockito.doReturn(Optional.empty()).when(openAiRequestAttributesRepository).findByName(TRUE_OPENAI_TOPIC_KEY);
        ErrorResponseException exception = assertThrows(ErrorResponseException.class, () -> {
            openAiApiFeignService.generateTopics(TAGS);
        });
        String expectedMessage = "Key does not exist! You need a key!";
        String errorStatus = exception.getErrorStatus().getMessage();
        assertTrue(errorStatus.contains(expectedMessage));

    }

    @Test
    public void generateTopicsErrorNotFoundTest() {
        OpenAiRequestAttributes openAiRequestAttributes = new OpenAiRequestAttributes();
        OpenAiChatCompletionResponse openAiChatCompletionResponse = makeResponse("");
        OpenAiChatCompletionRequest request = OpenAiChatCompletionRequest.makeRequest(FILTER_TOPIC);
        Mockito.doReturn(openAiChatCompletionResponse).when(openAiFeignClient)
                .generate(openAiRequestAttributes.getKey(), request);

        ErrorResponseException exception = assertThrows(ErrorResponseException.class, () -> {
            openAiApiFeignService.generateTopics(TAGS);
        });
        String expectedMessage = "OpenAI sent an incorrect response! Try later!";
        String errorStatus = exception.getErrorStatus().getMessage();
        assertTrue(errorStatus.contains(expectedMessage));
    }


//    @Test
//    public void generateArticleTest() {
//        Assertions.assertEquals(ARTICLETEST_RESULT_EXPECTED, openAiApiFeignService
//                .generateArticle(TAGS).getArticleBody());
//    }

    @Test
    public void generateArticleErrorKeyTest() {
        Mockito.doReturn(Optional.empty()).when(openAiRequestAttributesRepository).findByName(TRUE_OPENAI_ARTICLE_KEY);
        ErrorResponseException exception = assertThrows(ErrorResponseException.class, () -> {
            openAiApiFeignService.generateArticle(TAGS);
        });
        String expectedMessage = "Key does not exist! You need a key!";
        String errorStatus = exception.getErrorStatus().getMessage();
        assertTrue(errorStatus.contains(expectedMessage));

    }

    @Test
    public void generateArticleErrorNotFoundTest() {
        OpenAiRequestAttributes openAiRequestAttributes = new OpenAiRequestAttributes();
        OpenAiChatCompletionResponse openAiChatCompletionResponse = makeResponse("");
        OpenAiChatCompletionRequest request = OpenAiChatCompletionRequest.makeRequest(FILTER_ARTICLE);
        Mockito.doReturn(openAiChatCompletionResponse).when(openAiFeignClient)
                .generate(openAiRequestAttributes.getKey(), request);

        ErrorResponseException exception = assertThrows(ErrorResponseException.class, () -> {
            openAiApiFeignService.generateArticle(TAGS);
        });
        String expectedMessage = "OpenAI sent an incorrect response! Try later!";
        String errorStatus = exception.getErrorStatus().getMessage();
        assertTrue(errorStatus.contains(expectedMessage));
    }

    private OpenAiChatCompletionResponse makeResponse(String string) {
        List<Choice> choices = new ArrayList<>();
        Message message = new Message().setContent(string);
        Choice choice = new Choice()
                .setMessage(message);
        choices.add(choice);
        return new OpenAiChatCompletionResponse()
                .setChoices(choices);
    }
}
