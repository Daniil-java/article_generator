package com.education.article_generator.repositories;

import com.education.article_generator.entities.GenerationRequest;
import com.education.article_generator.entities.RequestTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTagRepository extends JpaRepository<RequestTag, Long> {
}
