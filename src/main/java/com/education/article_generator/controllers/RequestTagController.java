package com.education.article_generator.controllers;

import com.education.article_generator.entities.RequestTag;
import com.education.article_generator.services.RequestTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requesttag")
@RequiredArgsConstructor
public class RequestTagController {
    private final RequestTagService requestTagService;

    @GetMapping("/all")
    public List<RequestTag> getAll() {
        return requestTagService.getAll();
    }
}
