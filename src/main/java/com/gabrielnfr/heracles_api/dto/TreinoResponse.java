package com.gabrielnfr.heracles_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.gabrielnfr.heracles_api.model.Treino;

@Getter
@AllArgsConstructor
public class TreinoResponse {
    private Long id;
    private String nome;

    public static TreinoResponse fromEntity(Treino treino) {
        return new TreinoResponse(treino.getId(), treino.getNome());
    }
}
