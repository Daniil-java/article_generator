package com.education.articlegenerator.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Article {
    public Article(ArticleTopic articleTopic, String articleBody) {
        this.articleTopic = articleTopic;
        this.articleBody = articleBody;
    }

    public Article(String articleBody) {
        this.articleBody = articleBody;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private ArticleTopic articleTopic;

    @Column(name = "article_body")
    private String articleBody;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
}
