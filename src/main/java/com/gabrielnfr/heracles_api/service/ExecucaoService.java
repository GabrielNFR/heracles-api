package com.gabrielnfr.heracles_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gabrielnfr.heracles_api.repository.ExecucaoRepository;
import com.gabrielnfr.heracles_api.repository.TreinoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.gabrielnfr.heracles_api.model.Execucao;
import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import com.gabrielnfr.heracles_api.model.Treino;

@Service
@RequiredArgsConstructor
public class ExecucaoService {
    private final ExecucaoRepository execucaoRepository;
    private final TreinoRepository treinoRepository;

    @Transactional
    public Execucao criar(Long treinoId, Execucao dadosExecucao) {
        Treino treino = treinoRepository.findById(treinoId)
            .orElseThrow(() -> new EntityNotFoundException("Treino não encontrado com ID: " + treinoId));
        
        dadosExecucao.setTreino(treino);
        if (dadosExecucao.getExercicios() != null) {
            for (ExercicioRealizado exercicio : dadosExecucao.getExercicios()) {
                if (exercicio.getNomeExercicio() == null || exercicio.getNomeExercicio().isBlank()) {
                    throw new IllegalArgumentException("Nome do exercício é obrigatório");
                }
                if (exercicio.getSeries() == null || exercicio.getSeries() <= 0) {
                    throw new IllegalArgumentException("Número de séries deve ser maior que zero");
                }
                if (exercicio.getRepeticoes() == null || exercicio.getRepeticoes().isBlank()) {
                    throw new IllegalArgumentException("Repetições é obrigatório");
                }
                if (exercicio.getCarga() == null || exercicio.getCarga() <= 0) {
                    throw new IllegalArgumentException("Carga deve ser maior que zero");
                }
                exercicio.setExecucao(dadosExecucao);
            }
        } else {
            dadosExecucao.setExercicios(new ArrayList<>());
        }

        treino.getExecucoes().add(dadosExecucao);
        
        return execucaoRepository.save(dadosExecucao);
    }

    public List<Execucao> listarPorTreino(Long treinoId) {
        treinoRepository.findById(treinoId)
            .orElseThrow(() -> new EntityNotFoundException("Treino não encontrado com ID: " + treinoId));

        return execucaoRepository.findByTreinoId(treinoId);
    }

    @Transactional
    public Execucao buscarPorId(Long id) {
        return execucaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Execução não encontrada com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        Execucao execucao = execucaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Execução não encontrada com ID: " + id));
        execucao.getTreino().getExecucoes().remove(execucao);
        execucaoRepository.delete(execucao);
    }
}
