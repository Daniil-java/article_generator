package com.education.articlegenerator.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    ARTICLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Article does not exist"),
    ARTICLE_TOPIC_NOT_FOUND(HttpStatus.BAD_REQUEST, "ArticleTopic does not exist"),
    GENERATION_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "Request does not exist"),
    OPENAI_INCORRECT_ANSWER(HttpStatus.BAD_REQUEST, "OpenAI sent an incorrect response! Try later!"),
    FAILED_GENERATE(HttpStatus.BAD_REQUEST, "Failed to generate article! Try later!"),
    KEY_DOESNT_EXIST(HttpStatus.BAD_REQUEST, "Key does not exist! You need a key!");

    private HttpStatus httpStatus;
    private String message;
}
