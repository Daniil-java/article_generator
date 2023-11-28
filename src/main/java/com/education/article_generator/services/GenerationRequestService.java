package com.education.article_generator.services;

import com.education.article_generator.entities.GenerationRequest;
import com.education.article_generator.repositories.GenerationRequestRepository;
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
}
