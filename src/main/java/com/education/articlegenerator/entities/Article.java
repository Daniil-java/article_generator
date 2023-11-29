package com.education.articlegenerator.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_topic_id")
    private ArticleTopic articleTopic;

    @Column(name = "article_body")
    private String articleBody;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
}
