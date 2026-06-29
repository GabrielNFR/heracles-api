package com.gabrielnfr.heracles_api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.gabrielnfr.heracles_api.model.Execucao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExecucaoResponse {
    @Schema(description = "ID da execucao", example = "1")
    private Long id;
    @Schema(description = "Data e hora da execucao", example = "2026-06-29T14:30:00")
    private LocalDateTime dataHora;
    @Schema(description = "ID do treino", example = "1")
    private Long treinoId;
    @Schema(description = "Nome do treino", example = "Peito")
    private String treinoNome;
    @Schema(description = "Exercicios realizados na execucao")
    private List<ExercicioRealizadoResponse> exercicios;

    public static ExecucaoResponse fromEntity(Execucao execucao) {
        List<ExercicioRealizadoResponse> exerciciosDTO = execucao.getExercicios().stream()
            .map(ExercicioRealizadoResponse::fromEntity)
            .toList();

        return new ExecucaoResponse(
            execucao.getId(),
            execucao.getDataHora(),
            execucao.getTreino().getId(),
            execucao.getTreino().getNome(),
            exerciciosDTO
        );
    }
}
