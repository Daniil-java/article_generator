package com.education.articlegenerator.controllers;

import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.services.GenerationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/generationrequest")
@RequiredArgsConstructor
public class GenerationRequestController {

    private final GenerationRequestService generationRequestService;

    @GetMapping("/all")
    public List<GenerationRequest> getAllRequests() {
        return generationRequestService.getAllRequests();
    }
}