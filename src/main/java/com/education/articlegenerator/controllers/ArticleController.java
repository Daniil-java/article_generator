package com.education.articlegenerator.controllers;

import com.education.articlegenerator.dtos.ErrorResponse;
import com.education.articlegenerator.entities.Article;
import com.education.articlegenerator.services.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
@Tag(name = "Статьи", description = "Методы работы со статьями")
public class ArticleController {
    private final ArticleService articleService;

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
    public Article getArticle(@RequestParam Long topicId) {
        return articleService.getArticleByTopicId(topicId);
    }
}
