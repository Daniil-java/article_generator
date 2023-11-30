package com.education.articlegenerator.repositories;

import com.education.articlegenerator.entities.GenerationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenerationRequestRepository extends JpaRepository<GenerationRequest, Long> {

}
