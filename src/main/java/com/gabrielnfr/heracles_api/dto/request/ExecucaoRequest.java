package com.gabrielnfr.heracles_api.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExecucaoRequest {
    @NotNull
    @Schema(description = "Data e hora da execucao", example = "2026-06-29T14:30:00")
    private LocalDateTime dataHora;
    @NotNull
    @Size(min = 1, message = "A execução deve ter pelo menos 1 exercício")
    @Schema(description = "Lista de exercicios realizados")
    private List<@Valid ExercicioRealizadoRequest> exercicios;
}
