package com.education.articlegenerator.services;

import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
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
