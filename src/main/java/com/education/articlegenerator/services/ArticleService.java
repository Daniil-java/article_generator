package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ArticleDto;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.repositories.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleTopicService articleTopicService;
    private final OpenAiApiService openAiApiService;
    private final OpenAiApiFeignService openAiApiFeignService;
    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    @Transactional
    public List<Article> getArticlesByTopicId(List<Long> topicIds) {
        List<Article> articleList = new ArrayList<>();
        for (Long id : topicIds) {
            Optional<Article> article = articleRepository.findArticleByArticleTopicId(id);
            if (article.isPresent()) {
                articleList.add(article.get());
            } else {
                articleList.add(generateArticle(id));
            }
        }
        if (!articleList.isEmpty()) {
            return articleList;
        } else {
            throw new RuntimeException("articleList is Empty! ArticleService");
        }
    }

    private Article generateArticle(Long id) {
        ArticleTopic articleTopic = articleTopicService.getTopicById(id);
//        ArticleDto articleDto = openAiApiService.generateArticle(articleTopic.getTopicTitle());
        ArticleDto articleDto = openAiApiFeignService.generateArticle(articleTopic.getTopicTitle());
        return articleRepository.save(new Article()
                .setArticleBody(articleDto.getArticleBody())
                .setArticleTopic(articleTopic)
        );
    }
}
