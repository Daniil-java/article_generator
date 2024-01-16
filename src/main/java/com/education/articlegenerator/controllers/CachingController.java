package com.education.articlegenerator.controllers;

import com.education.articlegenerator.services.CachingService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/caching")
@RequiredArgsConstructor
public class CachingController {
    private final CachingService cachingService;

    @GetMapping("/clearAttributesCaches")
    public void clearAttributesCaches() {
        cachingService.evictAllattributesCacheValues();
    }
}
