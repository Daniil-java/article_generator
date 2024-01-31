package com.education.articlegenerator.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Модель запроса на генерацию")
public class GenerationRequestDto {
    @Schema(description = "Теги для статьи")
    private String requestTags;
}
