package com.education.articlegenerator.services;

import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {
    private final GenerationRequestService generationRequestService;
    private final ArticleTopicService articleTopicService;
    private final ArticleService articleService;

    @Scheduled(fixedRate = 10000)
    public void scheduledGenerationTopics() {
        log.info("Scheduled generation of topics is started!");
        List<GenerationRequest> generationRequests = generationRequestService.getRequestsByStatus(Status.CREATED);

        for (GenerationRequest generationRequest: generationRequests) {
            try {
                articleTopicService.generateTopics(generationRequest);
                generationRequest.setStatus(Status.GENERATED);
            } catch (Exception e) {
                generationRequest
                        .setStatus(Status.ERROR)
                        .setErrorCause(e.getMessage());
                log.error("Failed generate topic!");
            }
        }

        generationRequestService.saveAllRequest(generationRequests);

        log.info("Topics processed: {}", generationRequests.size());
        log.info("Scheduled generation of topics is end!");
    }

    @Scheduled(fixedRate = 10000)
    public void scheduledGenerationArticles() {
        log.info("Scheduled generation of article is started!");
        List<ArticleTopic> articleTopics = articleTopicService.getArticleTopicsByStatus(Status.CREATED);

        for (ArticleTopic articleTopic: articleTopics) {
            try {
                articleService.generateArticleByTopic(articleTopic);
                articleTopic.setStatus(Status.GENERATED);
            } catch (Exception e) {
                articleTopic
                        .setStatus(Status.ERROR)
                        .setErrorCause(e.getMessage());
                log.error("Failed generate article!");
            }
        }

        articleTopicService.saveAllTopics(articleTopics);

        log.info("Articles processed {}", articleTopics.size());
        log.info("Scheduled generation of article is end!");
    }
}
