package com.education.articlegenerator.services;


import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.dtos.GenerationRequestDto;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.GenerationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public List<GenerationRequest> saveAllRequest(List<GenerationRequest> requests) {
        return generationRequestRepository.saveAll(requests);
    }

    public GenerationRequest getRequestById(Long requestId) {
        return generationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.GENERATION_REQUEST_NOT_FOUND));
    }

    protected List<GenerationRequest> getRequestsByStatus(Status status) {
        Optional<List<GenerationRequest>> generationRequests = generationRequestRepository.findGenerationRequestByStatus(status);
        return generationRequests.isEmpty() ? new ArrayList<>() : generationRequests.get();
    }

}
