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

import com.gabrielnfr.heracles_api.dto.request.ExecucaoRequest;
import com.gabrielnfr.heracles_api.dto.response.ExecucaoResponse;
import com.gabrielnfr.heracles_api.model.Execucao;
import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import com.gabrielnfr.heracles_api.service.ExecucaoService;
import com.gabrielnfr.heracles_api.dto.request.ExercicioRealizadoRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
            er.setNomeExercicio(exReq.getNomeExercicio());
            er.setSeries(exReq.getSeries());
            er.setRepeticoes(exReq.getRepeticoes());
            er.setCarga(exReq.getCarga());
            er.setObs(exReq.getObs());
            listaER.add(er);
        }
        ex.setExercicios(listaER);

        return ex;
    }

    @PostMapping("/treinos/{treinoId}/execucoes")
    public ResponseEntity<ExecucaoResponse> criar(@PathVariable Long treinoId, @Valid @RequestBody ExecucaoRequest request) {
        Execucao ex = toEntity(request);
        Execucao salva = execucaoService.criar(treinoId, ex);
        ExecucaoResponse er = ExecucaoResponse.fromEntity(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(er);
    }

    @GetMapping("/treinos/{treinoId}/execucoes")
    public ResponseEntity<List<ExecucaoResponse>> listarPorTreino(@PathVariable Long treinoId) {
        List<Execucao> listaEx = execucaoService.listarPorTreino(treinoId);
        List<ExecucaoResponse> listaResp = listaEx.stream().map(ExecucaoResponse::fromEntity).toList();
        return ResponseEntity.ok(listaResp);
    }

    @GetMapping("/execucoes/{id}")
    public ResponseEntity<ExecucaoResponse> buscarPorId(@PathVariable Long id) {
        ExecucaoResponse er = ExecucaoResponse.fromEntity(execucaoService.buscarPorId(id));
        return ResponseEntity.ok(er);
    }

    @DeleteMapping("/execucoes/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        execucaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
