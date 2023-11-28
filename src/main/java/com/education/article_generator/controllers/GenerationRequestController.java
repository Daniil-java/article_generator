package com.education.article_generator.controllers;

import com.education.article_generator.entities.GenerationRequest;
import com.education.article_generator.services.GenerationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/generationrequest")
@RequiredArgsConstructor
public class GenerationRequestController {

    private final GenerationRequestService generationRequestService;

    @GetMapping("/getRequest")
    public List<GenerationRequest> getAllRequests(@RequestBody String category) {
        return generationRequestService.getAllRequests();
    }
}
