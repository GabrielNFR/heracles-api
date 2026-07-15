package com.gabrielnfr.heracles_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.gabrielnfr.heracles_api.dto.error.ErrorResponse;
import com.gabrielnfr.heracles_api.dto.request.ExecucaoRequest;
import com.gabrielnfr.heracles_api.dto.response.ExecucaoResponse;
import com.gabrielnfr.heracles_api.model.Execucao;
import com.gabrielnfr.heracles_api.model.Exercicio;
import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import com.gabrielnfr.heracles_api.service.ExecucaoService;
import com.gabrielnfr.heracles_api.security.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.gabrielnfr.heracles_api.dto.request.ExercicioRealizadoRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Execuções", description = "Gerenciamento de execucoes de treino")
@RestController
@RequiredArgsConstructor
public class ExecucaoController {
    private final ExecucaoService execucaoService;

    private Execucao toEntity(ExecucaoRequest request) {
        Execucao ex  = new Execucao();
        ex.setDataHora(request.getDataHora());

        List<ExercicioRealizado> listaER = new ArrayList<>();

        for (ExercicioRealizadoRequest exReq : request.getExercicios()) {
            ExercicioRealizado er = new ExercicioRealizado();
            Exercicio exercicioRef = new Exercicio();
            exercicioRef.setId(exReq.getExercicioId());
            er.setExercicio(exercicioRef);
            er.setSeries(exReq.getSeries());
            er.setRepeticoes(exReq.getRepeticoes());
            er.setCarga(exReq.getCarga());
            er.setObs(exReq.getObs());
            er.setUnidade(exReq.getUnidade());
            listaER.add(er);
        }
        ex.setExercicios(listaER);

        return ex;
    }

    @Operation(summary = "Criar execucao", description = "Cadastra a execucao de um plano de treino")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Execução criada com sucesso",
                        content = @Content(schema = @Schema(implementation = ExecucaoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Treino não existe",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/treinos/{treinoId}/execucoes")
    public ResponseEntity<ExecucaoResponse> criar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "ID do treino") @PathVariable Long treinoId,
            @Valid @RequestBody ExecucaoRequest request) {
        Execucao ex = toEntity(request);
        Execucao salva = execucaoService.criar(treinoId, ex, userDetails.getId());
        ExecucaoResponse er = ExecucaoResponse.fromEntity(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(er);
    }

    @Operation(summary = "Listar por treino", description = "Lista as execucoes cadastradas de um determinado plano de treino")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Execuções listadas com sucesso",
                        content = @Content(schema = @Schema(implementation = ExecucaoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Treino não existe",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/treinos/{treinoId}/execucoes")
    public ResponseEntity<List<ExecucaoResponse>> listarPorTreino(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "ID do treino") @PathVariable Long treinoId) {
        List<Execucao> listaEx = execucaoService.listarPorTreino(treinoId, userDetails.getId());
        List<ExecucaoResponse> listaResp = listaEx.stream().map(ExecucaoResponse::fromEntity).toList();
        return ResponseEntity.ok(listaResp);
    }

    @Operation(summary = "Buscar execucao", description = "Busca uma execucao especifica pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Execução encontrada com sucesso",
                        content = @Content(schema = @Schema(implementation = ExecucaoResponse.class))),
        @ApiResponse(responseCode = "404", description = "Execução não encontrada",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/execucoes/{id}")
    public ResponseEntity<ExecucaoResponse> buscarPorId(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "ID da execução") @PathVariable Long id) {
        ExecucaoResponse er = ExecucaoResponse.fromEntity(execucaoService.buscarPorId(id, userDetails.getId()));
        return ResponseEntity.ok(er);
    }

    @Operation(summary = "Deletar execucao", description = "Deleta uma execucao pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Execução deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Execução não encontrada",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/execucoes/{id}")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "ID da execução") @PathVariable Long id) {
        execucaoService.deletar(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
