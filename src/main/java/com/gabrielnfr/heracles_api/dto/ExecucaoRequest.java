package com.gabrielnfr.heracles_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExecucaoRequest {
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataHora;
    @NotNull
    @Size(min = 1, message = "A execução deve ter pelo menos 1 exercício")
    private List<ExercicioRealizadoRequest> exercicios;
}
