package com.education.article_generator.services;

import com.education.article_generator.entities.RequestTag;
import com.education.article_generator.repositories.RequestTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestTagService {
    private final RequestTagRepository requestTagRepository;
    public List<RequestTag> getAll() {
        return requestTagRepository.findAll();
    }
}
