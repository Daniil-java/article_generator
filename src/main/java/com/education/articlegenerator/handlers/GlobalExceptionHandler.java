package com.education.articlegenerator.handlers;

import com.education.articlegenerator.dtos.AppException;
import com.education.articlegenerator.dtos.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppException> catchAppException(AppException e) {
        log.error(e.getException().getMessage());
        return new ResponseEntity<>(new AppException(StatusCode.BAD_REQUEST), HttpStatus.NOT_FOUND);
    }
}
