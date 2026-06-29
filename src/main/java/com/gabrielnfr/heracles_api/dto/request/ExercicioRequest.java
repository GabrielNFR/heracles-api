package com.gabrielnfr.heracles_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExercicioRequest {
    @NotBlank(message = "Nome do exercicio e obrigatorio")
    @Schema(description = "Nome do exercicio", example = "Supino reto")
    private String nome;
}
