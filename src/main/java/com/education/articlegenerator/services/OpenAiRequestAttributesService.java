package com.education.articlegenerator.services;

import com.education.articlegenerator.configurations.CacheConfig;
import com.education.articlegenerator.dtos.ErrorResponseException;
import com.education.articlegenerator.dtos.ErrorStatus;
import com.education.articlegenerator.entities.OpenAiRequestAttributes;
import com.education.articlegenerator.repositories.OpenAiRequestAttributesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiRequestAttributesService {
    private final OpenAiRequestAttributesRepository openAiRequestAttributesRepository;

    @Cacheable(value = CacheConfig.AG_OPENAI_REQUEST_ATTRIBUTES_CACHE)
    public OpenAiRequestAttributes getByNameCachable(String name) {
        return openAiRequestAttributesRepository.findByName(name)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.KEY_DOESNT_EXIST));
    }

}
