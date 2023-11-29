package com.education.article_generator.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "generation_request")
@Data
@NoArgsConstructor
public class GenerationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "request_tags")
    private String requestTags;

    @OneToMany(mappedBy = "generationRequestId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ArticleTopic> articleTopics;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;

}
