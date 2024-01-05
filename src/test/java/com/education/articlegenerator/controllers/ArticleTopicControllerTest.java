package com.education.articlegenerator.controllers;

import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
import com.education.articlegenerator.services.ArticleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleTopicControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ArticleTopicRepository articleTopicRepository;
    @MockBean
    private ArticleService articleService;
    private static ArticleTopic articleTopic;
    private static List<ArticleTopic> articleTopics;

    @BeforeAll
    public static void setUp() {
        articleTopic = new ArticleTopic().setId(1L);
        articleTopics = new ArrayList<>(Arrays.asList(articleTopic));
    }

    @Test
    public void getAllTest() throws Exception {
        given(articleTopicRepository.findAll()).willReturn(articleTopics);
        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/articletopic/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
        ;
    }

    @Test
    public void getTopicsTest() throws Exception {
        given(articleTopicRepository.findArticleTopicByGenerationRequestId(articleTopic.getId()))
                .willReturn(Optional.of(articleTopics));

        this.mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/articletopic?requestId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
        ;
    }
}
