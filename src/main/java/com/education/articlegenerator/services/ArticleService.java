package com.education.articlegenerator.services;

import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.repositories.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleTopicService articleTopicService;
    private final OpenAiApiService openAiApiService;
    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    @Transactional
    public List<Article> getArticlesByTopicId(List<Long> topicIds) {
        List<Article> articleList = new ArrayList<>();
        for (Long id : topicIds) {
            Optional<List<Article>> list = articleRepository.findAllByArticleTopicId(id);
            if (list.isPresent() && !list.get().isEmpty()) {
                articleList.addAll(list.get());
            } else {
                articleList.addAll(toGenerateArticle(id));
            }
        }
        if (!articleList.isEmpty()) {
            return articleList;
        } else {
            throw new RuntimeException("articleList is Empty! ArticleService");
        }
    }

    private List<Article> toGenerateArticle(Long id) {
        List<Article> articleResultList = new ArrayList<>();
        ArticleTopic articleTopic = articleTopicService.getTopicsById(id);
        String articles = openAiApiService.getArticle(articleTopic.getTopicTitle());
        List<String> tagsList = Arrays.asList(articles.split(";"));
        for (String s : tagsList) {
            articleResultList.add(articleRepository.save(new Article(articleTopic, s)));
        }
        return articleResultList;
    }
}
