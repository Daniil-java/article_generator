package com.education.articlegenerator.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "article_topics")
@Data
@NoArgsConstructor

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Accessors(chain = true)
public class ArticleTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "topic_title")
    private String topicTitle;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "article_topic_id")
    private List<Article> articles;

    @ManyToOne
    private GenerationRequest generationRequest;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
