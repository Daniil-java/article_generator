package com.education.articlegenerator.dtos;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
public class ErrorResponse {
    private String reasonPhrase;
    private ErrorStatus errorCode;
    private String message;
    private String address;
    private int status;
    private LocalDateTime created;
}
