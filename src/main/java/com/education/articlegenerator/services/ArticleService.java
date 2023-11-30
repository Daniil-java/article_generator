package com.education.articlegenerator.services;

import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    public List<Article> getAll() {
        return articleRepository.findAll();
    }
}
