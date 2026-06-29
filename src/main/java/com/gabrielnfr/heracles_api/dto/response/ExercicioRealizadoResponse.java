package com.gabrielnfr.heracles_api.dto.response;

import com.gabrielnfr.heracles_api.model.ExercicioRealizado;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExercicioRealizadoResponse {
    @Schema(description = "ID do exercicio", example = "1")
    private Long id;
    @Schema(description = "Nome do exercicio", example = "Supino reto")
    private String nomeExercicio;
    @Schema(description = "Numero de series", example = "3")
    private Integer series;
    @Schema(description = "Repeticoes", example = "10")
    private String repeticoes;
    @Schema(description = "Carga em kg", example = "60.0")
    private Double carga;
    @Schema(description = "Observacoes", example = "aquecimento leve")
    private String obs;

    public static ExercicioRealizadoResponse fromEntity(ExercicioRealizado ex) {
        return new ExercicioRealizadoResponse(ex.getId(), ex.getNomeExercicio(), ex.getSeries(), ex.getRepeticoes(),
            ex.getCarga(), ex.getObs());
    }
}
