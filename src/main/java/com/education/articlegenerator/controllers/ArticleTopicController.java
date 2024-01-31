package com.education.articlegenerator.controllers;

import com.education.articlegenerator.dtos.ErrorResponse;
import com.education.articlegenerator.entities.ArticleTopic;
import com.education.articlegenerator.services.ArticleTopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articletopic")
@RequiredArgsConstructor
@Tag(name = "Заголовки статей", description = "Методы работы с заголовками")
public class ArticleTopicController {
    private final ArticleTopicService articleTopicService;

    @Operation(
            summary = "Запрос на получение всех заголовков по Id запроса",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleTopic.class)))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping
    public ArticleTopic getTopic(@RequestParam Long topicId) {
        return articleTopicService.getTopicById(topicId);
    }
}
