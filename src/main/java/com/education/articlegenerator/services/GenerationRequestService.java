package com.education.articlegenerator.services;


import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.dtos.GenerationRequestDto;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.GenerationRequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class GenerationRequestService {
    private final GenerationRequestRepository generationRequestRepository;
    private final ArticleTopicService articleTopicService;

    public GenerationRequest createRequest(GenerationRequestDto requestDto) {
        return generationRequestRepository.save(
                new GenerationRequest().setRequestTags(requestDto.getRequestTags())
        );
    }

    public GenerationRequest getRequestById(Long requestId) {
        return generationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.GENERATION_REQUEST_NOT_FOUND));
    }

    public List<GenerationRequest> getRequestsByStatus(Status status) {
        Optional<List<GenerationRequest>> generationRequests = generationRequestRepository.findGenerationRequestByStatus(status);
        return generationRequests.isEmpty() ? new ArrayList<>() : generationRequests.get();
    }

    @Transactional
    @Scheduled(fixedRate = 10000)
    public void scheduledGenerationTopics() {
        log.info("Scheduled generation of topics is started!");
        List<GenerationRequest> generationRequests = getRequestsByStatus(Status.CREATED);
        List<GenerationRequest> resultList = new ArrayList<>();

        int counter = 0;
        for (GenerationRequest generationRequest: generationRequests) {
            try {
                articleTopicService.generateTopics(generationRequest);
                resultList.add(generationRequest.setStatus(Status.GENERATED));
                counter++;
            } catch (JsonProcessingException e) {
                resultList.add(generationRequest.setStatus(Status.ERROR));
                throw new ErrorResponseException(ErrorStatus.FAILED_GENERATE);
            } finally {
                generationRequestRepository.saveAll(resultList);
                log.info("Topics were generated on " + counter + " requests");
            }
        }
        log.info("Scheduled generation of topics is end!");
    }
}
