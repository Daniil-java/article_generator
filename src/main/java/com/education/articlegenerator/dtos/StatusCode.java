package com.education.articlegenerator.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    BAD_REQUEST(400, "BAD REQUEST");

    private int code;
    private String message;
}
