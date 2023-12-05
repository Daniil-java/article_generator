package com.education.articlegenerator.repositories;

import com.education.articlegenerator.entities.OpenAiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenAiApiRepository extends JpaRepository<OpenAiKey, Long> {
    Optional<OpenAiKey> findByName(String name);
}
