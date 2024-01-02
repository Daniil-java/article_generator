package com.education.articlegenerator.repositories;

import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenerationRequestRepository extends JpaRepository<GenerationRequest, Long> {
    Optional<List<GenerationRequest>> findGenerationRequestByStatus(Status status);

}
