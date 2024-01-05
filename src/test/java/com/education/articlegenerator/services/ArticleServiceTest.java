package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ArticleDto;
import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.repositories.ArticleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ArticleService.class)
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private ArticleTopicService articleTopicService;
    @MockBean
    private OpenAiApiService openAiApiService;
    @MockBean
    private OpenAiApiFeignService openAiApiFeignService;

    @Test
    void getArticlesByTopicIdTest() {
        Article article = new Article().setId(1L);
        Mockito.doReturn(Optional.of(article)).when(articleRepository).findArticleByArticleTopicId(1L);

        ArrayList<Article> result = new ArrayList<>();
        result.add(article);

        ArrayList<Long> parameters = new ArrayList<>();
        parameters.add(1L);
        Assertions.assertEquals(result, articleService.getArticlesByTopicId(parameters));

        parameters.clear();
        result.clear();
        Mockito.doReturn(Optional.empty()).when(articleRepository).findArticleByArticleTopicId(null);

        ErrorResponseException exception = assertThrows(ErrorResponseException.class, () -> {
            articleService.getArticlesByTopicId(parameters);
        });

        String expectedMessage = "Failed to generate article! Try later!";
        String errorStatus = exception.getErrorStatus().getMessage();
        assertTrue(errorStatus.contains(expectedMessage));

        parameters.add(2L);
        Mockito.doReturn(Optional.empty()).when(articleRepository).findArticleByArticleTopicId(2L);

        ArticleTopic articleTopic = new ArticleTopic().setId(2L);
        Mockito.doReturn(articleTopic).when(articleTopicService).getTopicById(2L);

        ArticleDto articleDto = new ArticleDto();
        Mockito.doReturn(articleDto).when(openAiApiFeignService).generateArticle(articleTopic.getTopicTitle());
        Mockito.doReturn(articleDto).when(openAiApiService).generateArticle(articleTopic.getTopicTitle());

        article.setId(2L);
        Mockito.doReturn(article).when(articleRepository).save(new Article()
                .setArticleBody(articleDto.getArticleBody())
                .setArticleTopic(articleTopic)
        );

        result.add(article);
        Assertions.assertEquals(result, articleService.getArticlesByTopicId(parameters));
    }
}
