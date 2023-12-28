package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.AppException;
import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleTopicService {
    private final ArticleTopicRepository articleTopicRepository;
    private final GenerationRequestService generationRequestService;
    private final OpenAiApiService openAiApiService;
    private final OpenAiApiFeignService openAiApiFeignService;
    public List<ArticleTopic> getAll() {
        return articleTopicRepository.findAll();
    }

    @Transactional
    public List<ArticleTopic> getTopicsByRequestId(Long requestId) {
        Optional<List<ArticleTopic>> topicsList = articleTopicRepository.findArticleTopicByGenerationRequestId(requestId);
        if (topicsList.isPresent() && !topicsList.get().isEmpty()) {
            return topicsList.get();
        } else {
            return generateTopic(requestId);
        }

    }

    private List<ArticleTopic> generateTopic(Long requestId) {
        GenerationRequest request = generationRequestService.getRequestById(requestId);
//        List<ArticleTopicDto> topicList = openAiApiService.generateTopics(request.getRequestTags());
        List<ArticleTopicDto> topicList = openAiApiFeignService.generateTopics(request.getRequestTags());
        List<ArticleTopic> resultList = new ArrayList<>();
        for (ArticleTopicDto articleTopic : topicList) {
            resultList.add(articleTopicRepository.save(new ArticleTopic()
                    .setTopicTitle(articleTopic.getTopicTitle())
                    .setGenerationRequest(request))
            );
        }
        return resultList;
    }

    public ArticleTopic getTopicById(Long id) {
        return articleTopicRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorStatus.ARTICLE_TOPIC_NOT_FOUND));
    }
}
