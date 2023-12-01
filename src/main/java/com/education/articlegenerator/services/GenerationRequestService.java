package com.education.articlegenerator.services;


import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.repositories.GenerationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Optional<GenerationRequest> generationRequest = generationRequestRepository.findById(requestId);
        if (generationRequest.isPresent()) {
            return generationRequest.get();
        } else {
            throw new RuntimeException("generationrequest entity is not exist");
        }
    }
}
