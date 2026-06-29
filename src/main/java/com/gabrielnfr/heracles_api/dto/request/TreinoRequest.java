package com.gabrielnfr.heracles_api.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TreinoRequest {
    @Schema(description = "Nome do treino", example = "Peito", maxLength = 100)
    @NotBlank(message = "Nome do treino e obrigatorio")
    private String nome;

    @NotNull
    @Size(min = 1, message = "Treino deve ter pelo menos 1 exercicio")
    @Schema(description = "Exercicios do treino")
    private List<@Valid ExercicioRequest> exercicios;
}
