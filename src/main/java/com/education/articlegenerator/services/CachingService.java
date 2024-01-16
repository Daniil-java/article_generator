package com.education.articlegenerator.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CachingService {
    @CacheEvict(value = "attributes", allEntries = true)
    public void evictAllattributesCacheValues() {
        //Evict of Attributes Cache
    }
}
