package com.gabrielnfr.heracles_api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.gabrielnfr.heracles_api.model.Treino;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
public class TreinoResponse {
    @Schema(description = "ID do treino", example = "1")
    private Long id;
    @Schema(description = "Nome do treino", example = "Peito")
    private String nome;
    @Schema(description = "Exercicios do treino")
    private List<ExercicioResponse> exercicios;

    public static TreinoResponse fromEntity(Treino treino) {
        List<ExercicioResponse> exerciciosDTO = treino.getExercicios().stream()
            .map(ExercicioResponse::fromEntity)
            .toList();
        return new TreinoResponse(treino.getId(), treino.getNome(), exerciciosDTO);
    }
}
