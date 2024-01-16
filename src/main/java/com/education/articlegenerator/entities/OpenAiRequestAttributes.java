package com.education.articlegenerator.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "open_ai_request_attributes")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OpenAiRequestAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "key")
    private String key;

    @Column(name = "request_message")
    private String requestMessage;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;

}
