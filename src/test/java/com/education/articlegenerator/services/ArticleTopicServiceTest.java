package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest(classes = ArticleTopicService.class)
public class ArticleTopicServiceTest {
    @Autowired
    private ArticleTopicService articleTopicService;
    @MockBean
    private ArticleTopicRepository articleTopicRepository;
    @MockBean
    private GenerationRequestService generationRequestService;
    @MockBean
    private OpenAiApiService openAiApiService;
    @MockBean
    private OpenAiApiFeignService openAiApiFeignService;
    private final ArticleTopic TEST_TOPIC = new ArticleTopic().setId(1L).setStatus(Status.CREATED);
    private final Long TRUE_ID = 1L;
    private final Long FALSE_ID = 2L;

    @BeforeEach
    public void setUp() {
        Mockito.doReturn(Optional.empty()).when(articleTopicRepository)
                .findArticleTopicByGenerationRequestId(TRUE_ID);
        GenerationRequest generationRequest = new GenerationRequest().setId(TRUE_ID);
        Mockito.doReturn(generationRequest).when(generationRequestService).getRequestById(generationRequest.getId());
        ArrayList<ArticleTopicDto> articleTopicDtos = new ArrayList<>();
        articleTopicDtos.add(new ArticleTopicDto());
        Mockito.doReturn(articleTopicDtos).when(openAiApiFeignService).generateTopics(generationRequest.getRequestTags());
        Mockito.doReturn(articleTopicDtos).when(openAiApiService).generateTopics(generationRequest.getRequestTags());

        Mockito.doReturn(TEST_TOPIC).when(articleTopicRepository).save(new ArticleTopic()
                .setTopicTitle(TEST_TOPIC.getTopicTitle())
                .setGenerationRequest(generationRequest)
                .setStatus(Status.CREATED));
        ArrayList<ArticleTopic> articleTopicArrayList = new ArrayList<>();
        articleTopicArrayList.add(new ArticleTopic());
        Mockito.doReturn(Optional.of(articleTopicArrayList)).when(articleTopicRepository)
                .findArticleTopicByGenerationRequestId(FALSE_ID);
        ///
        Mockito.doReturn(Optional.of(TEST_TOPIC)).when(articleTopicRepository).findById(TRUE_ID);
        Mockito.doReturn(Optional.empty()).when(articleTopicRepository).findById(FALSE_ID);
        ///
        ArrayList<ArticleTopic> articleTopics = new ArrayList<>();
        articleTopics.add(TEST_TOPIC);
        Mockito.doReturn(Optional.of(articleTopics))
                .when(articleTopicRepository).findArticleTopicByStatus(Status.CREATED);
        Mockito.doReturn(Optional.of(new ArrayList<ArticleTopic>()))
                .when(articleTopicRepository).findArticleTopicByStatus(Status.GENERATED);
    }

    @Test
    public void getTopicsByRequestIdTest() {
        Assertions.assertEquals(TEST_TOPIC, articleTopicService.getTopicsByRequestId(TRUE_ID).get(0));

        ArrayList<ArticleTopic> articleTopicArrayList = new ArrayList<>();
        articleTopicArrayList.add(new ArticleTopic());
        Assertions.assertEquals(articleTopicArrayList, articleTopicService.getTopicsByRequestId(FALSE_ID));
    }

    @Test
    public void getTopicByIdTest() {
        Assertions.assertEquals(TEST_TOPIC, articleTopicService.getTopicById(TRUE_ID));

        ErrorResponseException exception = assertThrows(ErrorResponseException.class, () -> {
            articleTopicService.getTopicById(FALSE_ID);
        });

        String expectedMessage = "ArticleTopic does not exist";
        String errorStatus = exception.getErrorStatus().getMessage();
        assertTrue(errorStatus.contains(expectedMessage));
    }

    @Test
    public void getArticleTopicsByStatusTest() {
        ArrayList<ArticleTopic> articleTopics = new ArrayList<>();
        articleTopics.add(TEST_TOPIC);

        Assertions.assertEquals(articleTopics, articleTopicService.getArticleTopicsByStatus(Status.CREATED));
        assertTrue(articleTopicService.getArticleTopicsByStatus(Status.GENERATED).isEmpty());
    }
}
