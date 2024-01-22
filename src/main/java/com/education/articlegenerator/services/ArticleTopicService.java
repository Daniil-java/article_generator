package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class ArticleTopicService {
    private final ArticleTopicRepository articleTopicRepository;
    private final ArticleService articleService;
    private final OpenAiApiFeignService openAiApiFeignService;
    private final RedissonClient redissonClient;

    public ArticleTopic getTopicById(Long id) {
        return articleTopicRepository.findById(id)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.ARTICLE_TOPIC_NOT_FOUND));
    }

    public List<ArticleTopic> getArticleTopicsByStatus(Status status) {
        Optional<List<ArticleTopic>> articleTopics = articleTopicRepository.findArticleTopicByStatus(status);
        return articleTopics.isEmpty() ? new ArrayList<>() : articleTopics.get();
    }
    @Transactional
    public void generateTopics(GenerationRequest generationRequest) throws JsonProcessingException {
        log.info("Topic generating");
        List<ArticleTopic> articleTopics = new ArrayList<>();
        RLock lock;
        try {
            lock = redissonClient.getFairLock(String.valueOf(generationRequest));
            lock.lock();
            try {
                Optional<List<ArticleTopic>> optionalArticleTopics =
                        articleTopicRepository.findArticleTopicByGenerationRequestId(
                                generationRequest.getId()
                        );

                if (optionalArticleTopics.isPresent() && !optionalArticleTopics.get().isEmpty()) return;
                List<ArticleTopicDto> articleTopicDtos = openAiApiFeignService.generateTopics(
                        generationRequest.getRequestTags());
                for (ArticleTopicDto articleTopicDto: articleTopicDtos) {
                    articleTopics.add(new ArticleTopic()
                            .setTopicTitle(articleTopicDto.getTopicTitle())
                            .setGenerationRequest(new GenerationRequest()
                                    .setId(generationRequest.getId()))
                            .setStatus(Status.CREATED));
                }
                articleTopicRepository.saveAll(articleTopics);
            } finally {
                lock.unlock();
            }
        } catch (WriteRedisConnectionException ex) {
            log.error("Redisson Error! Check that the Redis server is running!",ex);
        }
    }

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void scheduledGenerationArticle() {
        log.info("Scheduled generation of article is started!");
        List<ArticleTopic> articleTopics = getArticleTopicsByStatus(Status.CREATED);
        List<ArticleTopic> resultList = new ArrayList<>();

        int counter = 0;
        for (ArticleTopic articleTopic: articleTopics) {
            try {
                articleService.generateArticleByTopic(articleTopic);
                resultList.add(articleTopic.setStatus(Status.GENERATED));
                counter++;
            } catch (JsonProcessingException e) {
                resultList.add(articleTopic.setStatus(Status.ERROR));
                throw new ErrorResponseException(ErrorStatus.FAILED_GENERATE);
            } finally {
                articleTopicRepository.saveAll(resultList);
                log.info("Articles were generated on " + counter + " topics");
            }
        }
        log.info("Scheduled generation of article is end!");
    }
}
