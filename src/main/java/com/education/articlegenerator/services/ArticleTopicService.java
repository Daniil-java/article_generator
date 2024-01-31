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
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.WriteRedisConnectionException;
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
    public List<ArticleTopic> saveAllTopics(List<ArticleTopic> articleTopics) {
        return articleTopicRepository.saveAll(articleTopics);
    }

    protected List<ArticleTopic> getArticleTopicsByStatus(Status status) {
        Optional<List<ArticleTopic>> articleTopics = articleTopicRepository.findArticleTopicByStatus(status);
        return articleTopics.isEmpty() ? new ArrayList<>() : articleTopics.get();
    }
    @Transactional
    public void generateTopics(GenerationRequest generationRequest) throws JsonProcessingException {
        log.info("Topic generating");
        List<ArticleTopic> articleTopics = new ArrayList<>();
        RMap<Long, GenerationRequest> map = redissonClient.getMap("topicsGenerating");
        RLock lock = map.getFairLock(generationRequest.getId());
        try {
            lock.lock();

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

        } catch (WriteRedisConnectionException ex) {
            log.error("Redisson Error! Check that the Redis server is running!",ex);
        } finally {
            lock.unlock();
        }
    }

}
