package com.education.article_generator.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_tags")
@Data
@NoArgsConstructor
public class RequestTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "generation_request_id")
    private GenerationRequest generationRequest;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
}
