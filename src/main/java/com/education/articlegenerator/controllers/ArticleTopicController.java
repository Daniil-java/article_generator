package com.education.articlegenerator.controllers;

import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.services.ArticleTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
public class ArticleTopicController {
    private final ArticleTopicService articleTopicService;

    @GetMapping("/all")
    public List<ArticleTopic> getAll() {
        return articleTopicService.getAll();
    }

    @GetMapping
    public List<ArticleTopic> getTopics(@RequestParam Long requestId) {
        return articleTopicService.getTopicsByRequestId(requestId);
    }
}
