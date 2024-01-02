package com.education.articlegenerator.services;


import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import com.education.articlegenerator.repositories.GenerationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerationRequestService {
    private final GenerationRequestRepository generationRequestRepository;

    public List<GenerationRequest> getAllRequests() {
        return generationRequestRepository.findAll();
    }

    public GenerationRequest createRequest(GenerationRequest request) {
        return generationRequestRepository.save(request);
    }

    public GenerationRequest getRequestById(Long requestId) {
        return generationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.GENERATION_REQUEST_NOT_FOUND));
    }

    public List<GenerationRequest> getRequestsByStatus(Status status) {
        Optional<List<GenerationRequest>> generationRequests = generationRequestRepository.findGenerationRequestByStatus(status);
        return generationRequests.isEmpty() ? new ArrayList<>() : generationRequests.get();
    }

    public void saveRequest(GenerationRequest generationRequest) {
        generationRequestRepository.save(generationRequest);
    }
}
