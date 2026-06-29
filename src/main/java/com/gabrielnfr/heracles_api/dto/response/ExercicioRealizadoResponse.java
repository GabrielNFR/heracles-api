package com.gabrielnfr.heracles_api.dto.response;

import com.gabrielnfr.heracles_api.model.ExercicioRealizado;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExercicioRealizadoResponse {
    @Schema(description = "ID do registro", example = "1")
    private Long id;
    @Schema(description = "ID do exercicio", example = "1")
    private Long exercicioId;
    @Schema(description = "Nome do exercicio", example = "Supino reto")
    private String exercicioNome;
    @Schema(description = "Numero de series", example = "3")
    private Integer series;
    @Schema(description = "Repeticoes", example = "10")
    private String repeticoes;
    @Schema(description = "Carga em kg", example = "60.0")
    private Double carga;
    @Schema(description = "Observacoes", example = "aquecimento leve")
    private String obs;

    public static ExercicioRealizadoResponse fromEntity(ExercicioRealizado er) {
        return new ExercicioRealizadoResponse(
            er.getId(),
            er.getExercicio().getId(),
            er.getExercicio().getNome(),
            er.getSeries(),
            er.getRepeticoes(),
            er.getCarga(),
            er.getObs()
        );
    }
}
