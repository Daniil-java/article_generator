package com.education.articlegenerator.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Модель заголовка статьи")
public class ArticleTopicDto {
    @Schema(description = "Текст заголовка")
    private String topicTitle;
}
