package com.gabrielnfr.heracles_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TreinoRequest {
    @Schema(description = "Nome do treino", example = "Peito", maxLength = 100)
    @NotBlank(message = "Nome do treino é obrigatório")
    private String nome;
}
