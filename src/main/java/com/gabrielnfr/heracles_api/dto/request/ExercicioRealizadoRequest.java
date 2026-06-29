package com.gabrielnfr.heracles_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExercicioRealizadoRequest {
    @NotBlank(message = "Nome do exercício é obrigatório")
    @Schema(description = "Nome do exercicio", example = "Supino reto")
    private String nomeExercicio;
    @NotNull(message = "Número de séries é obrigatório")
    @Min(value = 1, message = "Número de séries deve ser maior que zero")
    @Schema(description = "Numero de series", example = "3")
    private Integer series;
    @NotBlank(message = "Número de repetições é obrigatório")
    @Schema(description = "Repeticoes (numero, faixa ou texto)", example = "10")
    private String repeticoes;
    @NotNull(message = "Carga é obrigatória")
    @Positive(message = "Carga deve ser maior que zero")
    @Schema(description = "Carga em kg", example = "60.0")
    private Double carga;
    @Schema(description = "Observacoes", example = "aquecimento leve")
    private String obs;
}
