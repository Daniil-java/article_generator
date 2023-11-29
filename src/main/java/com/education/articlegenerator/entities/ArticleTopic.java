package com.education.articlegenerator.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "article_topics")
@Data
@NoArgsConstructor
public class ArticleTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "topic_title")
    private String topicTitle;

    @ManyToOne
    @JoinColumn(name = "generation_request_id")
    private GenerationRequest generationRequest;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
}
