package com.education.article_generator.repositories;

import com.education.article_generator.entities.GenerationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenerationRequestRepository extends JpaRepository<GenerationRequest, Long> {

}
