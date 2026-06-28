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
import com.gabrielnfr.heracles_api.dto.TreinoRequest;
import com.gabrielnfr.heracles_api.dto.TreinoResponse;
import com.gabrielnfr.heracles_api.model.Treino;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/treinos")
public class TreinoController {
    private final TreinoService treinoService;

    @PostMapping
    public ResponseEntity<TreinoResponse> criar(@Valid @RequestBody TreinoRequest request) {
        TreinoResponse tr = TreinoResponse.fromEntity(treinoService.criar(request.getNome()));
        return ResponseEntity.status(HttpStatus.CREATED).body(tr);
    }

    @GetMapping 
    public ResponseEntity<List<TreinoResponse>> listarTodos() {
        List<Treino> t = treinoService.listarTodos();
        List<TreinoResponse> tr = t.stream().map(TreinoResponse::fromEntity).toList();
        return ResponseEntity.ok(tr);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreinoResponse> buscarPorId(@PathVariable Long id) {
        TreinoResponse tr = TreinoResponse.fromEntity(treinoService.buscarPorId(id));
        return ResponseEntity.ok(tr);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        treinoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}   
