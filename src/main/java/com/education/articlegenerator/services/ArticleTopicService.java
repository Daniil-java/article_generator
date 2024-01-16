package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final GenerationRequestService generationRequestService;
    private final OpenAiApiFeignService openAiApiFeignService;
    public List<ArticleTopic> getAll() {
        return articleTopicRepository.findAll();
    }

    @Transactional
    public List<ArticleTopic> getTopicsByRequestId(Long requestId) {
        Optional<List<ArticleTopic>> topicsList =
                articleTopicRepository.findArticleTopicByGenerationRequestId(requestId);
        if (topicsList.isPresent() && !topicsList.get().isEmpty()) {
            return topicsList.get();
        } else {
            return generateTopic(requestId);
        }

    }

    private List<ArticleTopic> generateTopic(Long requestId) {
        GenerationRequest request = generationRequestService.getRequestById(requestId);
        List<ArticleTopicDto> topicList = openAiApiFeignService.generateTopics(request.getRequestTags());
        List<ArticleTopic> resultList = new ArrayList<>();
        for (ArticleTopicDto articleTopic : topicList) {
            resultList.add(articleTopicRepository.save(new ArticleTopic()
                    .setTopicTitle(articleTopic.getTopicTitle())
                    .setGenerationRequest(request)
                    .setStatus(Status.CREATED))
            );
            request.setStatus(Status.GENERATED);
            generationRequestService.saveRequest(request);
        }
        return resultList;
    }

    public ArticleTopic getTopicById(Long id) {
        return articleTopicRepository.findById(id)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.ARTICLE_TOPIC_NOT_FOUND));
    }

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void scheduledGenerationTopic() {
        log.info("Scheduled generation of article topic is started!");
        List<GenerationRequest> generationRequests = generationRequestService.getRequestsByStatus(Status.CREATED);
        if (generationRequests.isEmpty()) {
            log.info("There are no ungenerated topics!");
            return;
        } else {
            log.info("Found " + generationRequests.size() + " requests without generated topics");
        }

        generationRequests.stream()
                .map(GenerationRequest::getId)
                .forEach(this::getTopicsByRequestId);

        log.info("The work is completed. Topics were generated on " + generationRequests.size() + " requests");
    }

    public List<ArticleTopic> getArticleTopicsByStatus(Status status) {
        Optional<List<ArticleTopic>> articleTopics = articleTopicRepository.findArticleTopicByStatus(status);
        return articleTopics.isEmpty() ? new ArrayList<>() : articleTopics.get();
    }

    public void saveArticleTopic(ArticleTopic articleTopic) {
        articleTopicRepository.save(articleTopic);
    }
}
