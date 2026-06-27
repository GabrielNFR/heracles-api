package com.gabrielnfr.heracles_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TreinoRequest {
    @NotBlank(message = "Nome do treino é obrigatório")
    private String nome;
}
