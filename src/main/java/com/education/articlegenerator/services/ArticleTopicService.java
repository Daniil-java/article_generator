package com.education.articlegenerator.services;

import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleTopicService {
    private final ArticleTopicRepository articleTopicRepository;
    private final GenerationRequestService generationRequestService;
    private final OpenAiApiService openAiApiService;

    public List<ArticleTopic> getAll() {
        return articleTopicRepository.findAll();
    }

    @Transactional
    public List<ArticleTopic> getTopicsByRequestId(Long requestId) {
        //TODO Optional обработчик
        Optional<List<ArticleTopic>> topicsList = articleTopicRepository.findArticleTopicByGenerationRequestId(requestId);
        if (topicsList.isPresent() && !topicsList.get().isEmpty()) {
            return topicsList.get();
        } else {
            return toGenerateTopic(requestId);
        }

    }

    private List<ArticleTopic> toGenerateTopic(Long requestId) {
        GenerationRequest request = generationRequestService.getRequestById(requestId);
        String tags = openAiApiService.getTopics(request.getRequestTags());
        List<String> tagsList = Arrays.asList(tags.split(", "));
        for (String s : tagsList) {
            articleTopicRepository.save(new ArticleTopic(s, request));
        }
        Optional<List<ArticleTopic>> topicsList = articleTopicRepository.findArticleTopicByGenerationRequestId(requestId);
        if (topicsList.isPresent()) {
            return topicsList.get();
        } else {
            throw new RuntimeException("TopicGenerateError. ArticleTopicService.toGenerate(" + requestId + ")");
        }
    }

    public ArticleTopic getTopicsById(Long id) {
        Optional<ArticleTopic> articleTopic = articleTopicRepository.findById(id);
        if (articleTopic.isPresent()) {
            return articleTopic.get();
        } else {
            throw new RuntimeException("Topic is not exist!. ArticleTopicService.getTopicsById(" + id + ")");
        }
    }
}
