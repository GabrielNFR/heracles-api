package com.gabrielnfr.heracles_api.dto;

import com.gabrielnfr.heracles_api.model.ExercicioRealizado;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExercicioRealizadoResponse {
    private Long id;
    private String nomeExercicio;
    private Integer series;
    private String repeticoes;
    private Double carga;
    private String obs;

    public static ExercicioRealizadoResponse fromEntity(ExercicioRealizado ex) {
        return new ExercicioRealizadoResponse(ex.getId(), ex.getNomeExercicio(), ex.getSeries(), ex.getRepeticoes(),
            ex.getCarga(), ex.getObs());
    }
}
