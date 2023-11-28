package com.education.article_generator.services;

import com.education.article_generator.entities.ArticleTopic;
import com.education.article_generator.repositories.ArticleTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleTopicService {
    private final ArticleTopicRepository articleTopicRepository;
    public List<ArticleTopic> getAll() {
        return articleTopicRepository.findAll();
    }
}
