package com.education.articlegenerator.services;

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

    public List<ArticleTopic> getAll() {
        return articleTopicRepository.findAll();
    }

    @Transactional
    public List<ArticleTopic> getTopicsByRequestId(Long requestId) {
        Optional<List<ArticleTopic>> topicsList = articleTopicRepository.findArticleTopicByGenerationRequestId(requestId);
        if (topicsList.isPresent() && !topicsList.get().isEmpty()) {
            return topicsList.get();
        } else {
            return toGenerateTopic(requestId);
        }

    }

    private List<ArticleTopic> toGenerateTopic(Long requestId) {
        GenerationRequest request = generationRequestService.getRequestById(requestId);
        List<String> tagsList = openAiApiService.generateTopics(request.getRequestTags());
        List<ArticleTopic> topicList = new ArrayList<>();
        for (String tag : tagsList) {
            topicList.add(articleTopicRepository.save(new ArticleTopic()
                    .setTopicTitle(tag)
                    .setGenerationRequest(request)));
        }
        return topicList;
    }

    public ArticleTopic getTopicById(Long id) {
        return articleTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Topic is not exist!. ArticleTopicService.getTopicsById(" + id + ")"
                ));
    }
}
