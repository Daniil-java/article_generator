package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ArticleDto;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.repositories.ArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.WriteRedisConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final OpenAiApiFeignService openAiApiFeignService;
    private final RedissonClient redissonClient;

    public Article getArticleByTopicId(Long topicId) {
        return articleRepository.findById(topicId)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.ARTICLE_NOT_FOUND));
    }

    @Transactional
    public void generateArticleByTopic(ArticleTopic articleTopic) throws JsonProcessingException {
        log.info("Article generating");
        RMap<Long, ArticleTopic> map = redissonClient.getMap("articlesGenerating");
        RLock lock = map.getFairLock(articleTopic.getId());
        try {
            if (!articleRepository.findArticleByArticleTopicId(articleTopic.getId()).isEmpty()) {
                return;
            }
            ArticleDto articleDto = openAiApiFeignService.generateArticle(articleTopic.getTopicTitle());
            articleRepository.save(new Article()
                    .setArticleBody(articleDto.getArticleBody())
                    .setArticleTopic(articleTopic)
            );
        } catch (WriteRedisConnectionException ex) {
            log.error("Redisson Error! Check that the Redis server is running!",ex);
        } finally {
            lock.unlock();
        }
    }
}
