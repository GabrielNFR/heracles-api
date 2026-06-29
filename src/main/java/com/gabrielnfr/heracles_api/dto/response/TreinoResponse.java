package com.gabrielnfr.heracles_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.gabrielnfr.heracles_api.model.Treino;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
public class TreinoResponse {
    @Schema(description = "ID do treino", example = "1")
    private Long id;
    @Schema(description = "Nome do treino", example = "Costas")
    private String nome;

    public static TreinoResponse fromEntity(Treino treino) {
        return new TreinoResponse(treino.getId(), treino.getNome());
    }
}
