package com.education.articlegenerator.controllers;

import com.education.articlegenerator.dtos.ErrorResponse;
import com.education.articlegenerator.entities.GenerationRequest;
import com.education.articlegenerator.services.GenerationRequestService;
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
@RequestMapping("/api/v1/generationrequest")
@RequiredArgsConstructor
@Tag(name = "Запросы генерации", description = "Методы работы с запросами генерации")
public class GenerationRequestController {
    private final GenerationRequestService generationRequestService;

    @Operation(
            summary = "Запрос на получение всех запросов на генерацию",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenerationRequest.class)))
                    )
            }
    )
    @GetMapping("/all")
    public List<GenerationRequest> getAllRequests() {
        return generationRequestService.getAllRequests();
    }

    @Operation(
            summary = "Запрос на создание запроса на генерацию",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = GenerationRequest.class))
                    ),
                    @ApiResponse(
                            description = "Провальный ответ", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/")
    public GenerationRequest createRequest(@RequestBody GenerationRequest request) {
        return generationRequestService.createRequest(request);
    }
}