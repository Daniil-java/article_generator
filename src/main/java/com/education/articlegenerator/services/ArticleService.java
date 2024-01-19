package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ArticleDto;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.entities.ArticleTopic;

import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.WriteRedisConnectionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleTopicService articleTopicService;
    private final OpenAiApiFeignService openAiApiFeignService;
    private final RedissonClient redissonClient;
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
            throw new ErrorResponseException(ErrorStatus.FAILED_GENERATE);
        }
    }

    private Article generateArticle(Long id) {
        log.info("generate article");
        RLock lock;
        try {
            lock = redissonClient.getFairLock(String.valueOf(id));
            lock.lock();
            try {
                log.info("start generate article");
                ArticleTopic articleTopic = articleTopicService.getTopicById(id);
                if (articleTopic.getStatus().equals(Status.GENERATED)) {
                    return articleTopic.getArticles().get(0);
                }
                ArticleDto articleDto = openAiApiFeignService.generateArticle(articleTopic.getTopicTitle());
                articleTopicService.saveArticleTopic(articleTopic.setStatus(Status.GENERATED));
                log.info("end generate article");
                return articleRepository.save(new Article()
                        .setArticleBody(articleDto.getArticleBody())
                        .setArticleTopic(articleTopic)
                );
            } finally {
                lock.unlock();
            }
        } catch (WriteRedisConnectionException ex) {
            log.error("Redisson Error! Check that the Redis server is running!",ex);
            return null;
        }
    }

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void scheduledGenerationArticle() {
        log.info("Scheduled generation of article is started!");
        List<ArticleTopic> articleTopics = articleTopicService.getArticleTopicsByStatus(Status.CREATED);
        if (articleTopics.isEmpty()) {
            log.info("There are no ungenerated articles!");
            return;
        } else {
            log.info("Found " + articleTopics.size() + " topics without generated articles");
        }
        List<Long> topicIds = articleTopics.stream()
                .map(ArticleTopic::getId)
                .toList();
        getArticlesByTopicId(topicIds);
        log.info("The work is completed. Articles were generated on " + topicIds.size() + " topics");
    }

}
