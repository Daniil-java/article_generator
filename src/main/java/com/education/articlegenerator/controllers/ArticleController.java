package com.education.articlegenerator.controllers;

import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/all")
    public List<Article> getAll() {
        return articleService.getAll();
    }
}
