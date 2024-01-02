package com.education.articlegenerator.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    CREATED(0),
    GENERATED(1);

    private int code;

}
