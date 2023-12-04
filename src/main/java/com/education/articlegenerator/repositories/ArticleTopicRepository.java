package com.education.articlegenerator.repositories;

import com.education.articlegenerator.entities.ArticleTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleTopicRepository extends JpaRepository<ArticleTopic, Long> {
    Optional<List<ArticleTopic>> findArticleTopicByGenerationRequestId(Long id); 

}
