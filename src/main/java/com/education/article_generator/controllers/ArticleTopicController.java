package com.education.article_generator.controllers;

import com.education.article_generator.entities.ArticleTopic;
import com.education.article_generator.services.ArticleTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articletopic")
@RequiredArgsConstructor
public class ArticleTopicController {
    private final ArticleTopicService articleTopicService;

    @GetMapping("/all")
    public List<ArticleTopic> getAll() {
        return articleTopicService.getAll();
    }
}
