package com.education.articlegenerator.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Модель статьи")
public class ArticleDto {
    @Schema(description = "Текст статьи")
    private String articleBody;
}
