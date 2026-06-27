package com.gabrielnfr.heracles_api.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.gabrielnfr.heracles_api.model.Execucao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExecucaoResponse {
    private Long id;
    private LocalDateTime dataHora;
    private Long treinoId;
    private String treinoNome;
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
