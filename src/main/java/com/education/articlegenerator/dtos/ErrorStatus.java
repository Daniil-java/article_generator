package com.education.articlegenerator.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Article does not exist"),
    ARTICLE_TOPIC_NOT_FOUND(HttpStatus.NOT_FOUND, "ArticleTopic does not exist"),
    GENERATION_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Request does not exist"),
    ARTICLE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "Try again later"),
    ARTICLE_TOPIC_BAD_REQUEST(HttpStatus.BAD_REQUEST, "Try again later");

    private HttpStatus httpStatus;
    private String message;
}
