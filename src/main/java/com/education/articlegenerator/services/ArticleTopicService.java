package com.education.articlegenerator.services;

import com.education.articlegenerator.dtos.ArticleTopicDto;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.ArticleTopicRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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
        Optional<List<ArticleTopic>> topicsList =
                articleTopicRepository.findArticleTopicByGenerationRequestId(requestId);
        if (topicsList.isPresent() && !topicsList.get().isEmpty()) {
            return topicsList.get();
        } else {
            return toGenerateTopic(requestId);
        }

    }

    private List<ArticleTopic> toGenerateTopic(Long requestId) {
        GenerationRequest request = generationRequestService.getRequestById(requestId);
//        List<ArticleTopicDto> topicList = openAiApiService.generateTopics(request.getRequestTags());
        List<ArticleTopicDto> topicList = openAiApiFeignService.generateTopics(request.getRequestTags());
        List<ArticleTopic> resultList = new ArrayList<>();
        for (ArticleTopicDto articleTopic : topicList) {
            resultList.add(articleTopicRepository.save(new ArticleTopic()
                    .setTopicTitle(articleTopic.getTopicTitle())
                    .setGenerationRequest(request)
                    .setStatus(Status.CREATED))
            );
            request.setStatus(Status.GENERATED);
            generationRequestService.modifyRequest(request);
        }
        return resultList;
    }

    public ArticleTopic getTopicById(Long id) {
        return articleTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Topic is not exist!. ArticleTopicService.getTopicsById(" + id + ")"
                ));
    }

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void scheduledGenerationTopic() {
        System.out.println("scheduledGenerationTopic");
        List<GenerationRequest> generationRequests = generationRequestService.getRequestsByStatus(Status.CREATED);
        if (generationRequests.isEmpty()) return;

        for (GenerationRequest gr : generationRequests) {
            getTopicsByRequestId(gr.getId());
        }
    }

    public List<ArticleTopic> getArticleTopicsByStatus(Status status) {
        return articleTopicRepository.findArticleTopicByStatus(status)
                .orElseThrow(() -> new RuntimeException(
                        "Topic is not exist!. ArticleTopicService.getArticleTopicsByStatus"));

    }

    public void modifyArticleTopic(ArticleTopic articleTopic) {
        articleTopicRepository.save(articleTopic);
    }
}
