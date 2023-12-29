package com.education.articlegenerator.dtos;

import lombok.Data;

@Data
public class ErrorResponseException extends RuntimeException{
    private ErrorStatus errorStatus;

    public ErrorResponseException(ErrorStatus errorStatus, Throwable ex) {
        super(ex);
        this.errorStatus = errorStatus;
    }

    public ErrorResponseException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
