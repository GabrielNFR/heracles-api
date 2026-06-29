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
    @NotNull(message = "ID do exercicio e obrigatorio")
    @Schema(description = "ID do exercicio do treino", example = "1")
    private Long exercicioId;
    @NotNull(message = "Numero de series e obrigatorio")
    @Min(value = 1, message = "Numero de series deve ser maior que zero")
    @Schema(description = "Numero de series", example = "3")
    private Integer series;
    @NotBlank(message = "Numero de repeticoes e obrigatorio")
    @Schema(description = "Repeticoes (numero, faixa ou texto)", example = "10")
    private String repeticoes;
    @NotNull(message = "Carga e obrigatoria")
    @Positive(message = "Carga deve ser maior que zero")
    @Schema(description = "Carga em kg", example = "60.0")
    private Double carga;
    @Schema(description = "Observacoes", example = "aquecimento leve")
    private String obs;
}
