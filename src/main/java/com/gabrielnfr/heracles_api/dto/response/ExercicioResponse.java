package com.gabrielnfr.heracles_api.dto.response;

import com.gabrielnfr.heracles_api.model.Exercicio;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExercicioResponse {
    @Schema(description = "ID do exercicio", example = "1")
    private Long id;
    @Schema(description = "Nome do exercicio", example = "Supino reto")
    private String nome;

    public static ExercicioResponse fromEntity(Exercicio exercicio) {
        return new ExercicioResponse(exercicio.getId(), exercicio.getNome());
    }
}
