package com.gabrielnfr.heracles_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gabrielnfr.heracles_api.repository.ExecucaoRepository;
import com.gabrielnfr.heracles_api.repository.ExercicioRepository;
import com.gabrielnfr.heracles_api.repository.TreinoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.gabrielnfr.heracles_api.model.Execucao;
import com.gabrielnfr.heracles_api.model.Exercicio;
import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import com.gabrielnfr.heracles_api.model.Treino;

@Service
@RequiredArgsConstructor
public class ExecucaoService {
    private final ExecucaoRepository execucaoRepository;
    private final TreinoRepository treinoRepository;
    private final ExercicioRepository exercicioRepository;

    @Transactional
    public Execucao criar(Long treinoId, Execucao dadosExecucao, Long usuarioId) {
        Treino treino = treinoRepository.findById(treinoId)
            .orElseThrow(() -> new EntityNotFoundException("Treino nao encontrado com ID: " + treinoId));
        if (!treino.getUsuario().getId().equals(usuarioId)) {
            throw new EntityNotFoundException("Treino nao encontrado com ID: " + treinoId);
        }
        
        dadosExecucao.setTreino(treino);
        if (dadosExecucao.getExercicios() != null) {
            for (ExercicioRealizado realizado : dadosExecucao.getExercicios()) {
                if (realizado.getExercicio() == null || realizado.getExercicio().getId() == null) {
                    throw new IllegalArgumentException("Exercicio e obrigatorio");
                }
                Exercicio exercicio = exercicioRepository.findById(realizado.getExercicio().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Exercicio nao encontrado com ID: " + realizado.getExercicio().getId()));
                if (!exercicio.getTreino().getId().equals(treinoId)) {
                    throw new IllegalArgumentException(
                        "Exercicio '" + exercicio.getNome() + "' nao pertence ao treino " + treino.getNome());
                }
                if (realizado.getSeries() == null || realizado.getSeries() <= 0) {
                    throw new IllegalArgumentException("Numero de series deve ser maior que zero");
                }
                if (realizado.getRepeticoes() == null || realizado.getRepeticoes().isBlank()) {
                    throw new IllegalArgumentException("Repeticoes e obrigatorio");
                }
                if (realizado.getCarga() == null || realizado.getCarga() <= 0) {
                    throw new IllegalArgumentException("Carga deve ser maior que zero");
                }
                realizado.setExercicio(exercicio);
                realizado.setExecucao(dadosExecucao);
            }
        } else {
            dadosExecucao.setExercicios(new ArrayList<>());
        }

        treino.getExecucoes().add(dadosExecucao);
        
        return execucaoRepository.save(dadosExecucao);
    }

    public List<Execucao> listarPorTreino(Long treinoId, Long usuarioId) {
        Treino treino = treinoRepository.findById(treinoId)
            .orElseThrow(() -> new EntityNotFoundException("Treino não encontrado com ID: " + treinoId));
        if (!treino.getUsuario().getId().equals(usuarioId)) {
            throw new EntityNotFoundException("Treino não encontrado com ID: " + treinoId);
        }
        return execucaoRepository.findByTreinoId(treinoId);
    }

    @Transactional
    public Execucao buscarPorId(Long id, Long usuarioId) {
        Execucao execucao = execucaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Execução não encontrada com ID: " + id));
        if (!execucao.getTreino().getUsuario().getId().equals(usuarioId)) {
            throw new EntityNotFoundException("Execução não encontrada com ID: " + id);
        }
        return execucao;
    }

    @Transactional
    public void deletar(Long id, Long usuarioId) {
        Execucao execucao = execucaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Execução não encontrada com ID: " + id));
        if (!execucao.getTreino().getUsuario().getId().equals(usuarioId)) {
            throw new EntityNotFoundException("Execução não encontrada com ID: " + id);
        }
        execucao.getTreino().getExecucoes().remove(execucao);
        execucaoRepository.delete(execucao);
    }
}
