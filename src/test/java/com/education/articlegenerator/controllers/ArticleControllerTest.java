package com.education.articlegenerator.controllers;

import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.repositories.ArticleRepository;
import com.education.articlegenerator.services.ArticleTopicService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class ArticleControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private ArticleTopicService articleTopicService;
    private static Article article;
    private static List<Article> articles;

    @BeforeAll
    public static void setUp() {
        article = new Article().setId(1L);
        articles = new ArrayList<>(Arrays.asList(article));
    }
    @Test
    void getAllTest() throws Exception {
        given(articleRepository.findAll()).willReturn(articles);
        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/article/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
        ;
    }

    @Test
    void getTopicsTest() throws Exception {
        given(articleRepository.findArticleByArticleTopicId(article.getId()))
                .willReturn(Optional.of(article));

        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/article?topicIds=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
        ;
    }
}
