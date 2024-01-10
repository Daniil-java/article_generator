package com.education.articlegenerator.repositories;

import com.education.articlegenerator.entities.OpenAiRequestAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OpenAiRequestAttributesRepository extends JpaRepository<OpenAiRequestAttributes, Long> {
    Optional<OpenAiRequestAttributes> findByName(String name);
}
