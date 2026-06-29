package com.gabrielnfr.heracles_api.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    @Schema(description = "Timestamp do erro", example = "2026-06-29T14:30:00")
    private final LocalDateTime timestamp;
    @Schema(description = "Codigo HTTP", example = "404")
    private final int status;
    @Schema(description = "Tipo do erro", example = "Not Found")
    private final String error;
    @Schema(description = "Mensagem descritiva", example = "Treino nao encontrado com ID: 999")
    private final String message;
    @Schema(description = "Caminho da requisicao", example = "/treinos/999")
    private final String path;
    @Schema(description = "Erros por campo (apenas para 400)")
    private final Map<String, String> fieldErrors;
}
