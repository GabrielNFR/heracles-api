package com.gabrielnfr.heracles_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gabrielnfr.heracles_api.service.TreinoService;
import com.gabrielnfr.heracles_api.dto.error.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gabrielnfr.heracles_api.dto.request.ExercicioRequest;
import com.gabrielnfr.heracles_api.dto.request.TreinoRequest;
import com.gabrielnfr.heracles_api.dto.response.TreinoResponse;
import com.gabrielnfr.heracles_api.model.Treino;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Tag(name = "Treinos", description = "Gerenciamento de planos de treino")
@RestController
@RequiredArgsConstructor
@RequestMapping("/treinos")
public class TreinoController {
    private final TreinoService treinoService;

    @Operation(summary = "Criar treino", description = "Cadastra um novo plano de treino")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Treino criado com sucesso",
                        content = @Content(schema = @Schema(implementation = TreinoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TreinoResponse> criar(@Valid @RequestBody TreinoRequest request) {
        List<String> nomesExercicios = request.getExercicios().stream()
            .map(ExercicioRequest::getNome)
            .toList();
        TreinoResponse tr = TreinoResponse.fromEntity(
            treinoService.criar(request.getNome(), nomesExercicios));
        return ResponseEntity.status(HttpStatus.CREATED).body(tr);
    }

    @Operation(summary = "Listar planos de treino", description = "Lista todos os planos de treino")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Treinos listados com sucesso",
                        content = @Content(schema = @Schema(implementation = TreinoResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping 
    public ResponseEntity<List<TreinoResponse>> listarTodos() {
        List<Treino> t = treinoService.listarTodos();
        List<TreinoResponse> tr = t.stream().map(TreinoResponse::fromEntity).toList();
        return ResponseEntity.ok(tr);
    }
    
    @Operation(summary = "Buscar plano de treino", description = "Busca um plano especifico pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Treino encontrado com sucesso",
                        content = @Content(schema = @Schema(implementation = TreinoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Treino não encontrado",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TreinoResponse> buscarPorId(@Parameter(description = "ID do treino") @PathVariable Long id) {
        TreinoResponse tr = TreinoResponse.fromEntity(treinoService.buscarPorId(id));
        return ResponseEntity.ok(tr);
    }

    @Operation(summary = "Deletar treino", description = "Deleta um plano de treino pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Treino deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Treino não encontrado",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Parameter(description = "ID do treino") @PathVariable Long id) {
        treinoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}   
