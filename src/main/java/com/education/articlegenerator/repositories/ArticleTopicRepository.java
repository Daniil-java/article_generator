package com.education.articlegenerator.repositories;

import com.education.articlegenerator.entities.ArticleTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleTopicRepository extends JpaRepository<ArticleTopic, Long> {
}
