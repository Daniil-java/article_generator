package com.education.articlegenerator.controllers;

import com.education.articlegenerator.dtos.ErrorResponse;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.services.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
@Tag(name = "Статьи", description = "Методы работы со статьями")
public class ArticleController {
    private final ArticleService articleService;

    @Operation(
            summary = "Запрос на получение всех статей",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Article.class)))
                    )
            }
    )
    @GetMapping("/all")
    public List<Article> getAll() {
        return articleService.getAll();
    }

    @Operation(
            summary = "Запрос на получение всех статей по Id заголовка",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Article.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @GetMapping
    public List<Article> getTopics(@RequestParam List<Long> topicIds) {
        return articleService.getArticlesByTopicId(topicIds);
    }
}
