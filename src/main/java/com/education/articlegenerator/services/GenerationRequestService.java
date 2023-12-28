package com.education.articlegenerator.services;


import com.education.articlegenerator.dtos.AppException;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.repositories.GenerationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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
                .orElseThrow(() -> new AppException(ErrorStatus.GENERATION_REQUEST_NOT_FOUND));
    }
}
