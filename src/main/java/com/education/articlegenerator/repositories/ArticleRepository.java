package com.education.articlegenerator.repositories;

import com.education.articlegenerator.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<List<Article>> findAllByArticleTopicId(Long id);

    Optional<Article> findArticleByArticleTopicId(Long id);
}
