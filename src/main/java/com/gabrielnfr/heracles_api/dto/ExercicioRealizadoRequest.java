package com.gabrielnfr.heracles_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
public class ExercicioRealizadoRequest {
    @NotBlank(message = "Nome do exercício é obrigatório")
    private String nomeExercicio;
    @NotNull(message = "Número de séries é obrigatório")
    @Min(value = 1, message = "Número de séries deve ser maior que zero")
    private Integer series;
    @NotBlank(message = "Número de repetições é obrigatório")
    private String repeticoes;
    @NotNull(message = "Carga é obrigatória")
    @Positive(message = "Carga deve ser maior que zero")
    private Double carga;
    private String obs;
}
