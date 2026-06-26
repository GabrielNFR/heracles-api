package com.gabrielnfr.heracles_api.service;

import org.springframework.stereotype.Service;

import com.gabrielnfr.heracles_api.repository.TreinoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.gabrielnfr.heracles_api.model.Treino;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class TreinoService {
    private final TreinoRepository treinoRepository;

    @Transactional
    public Treino criar(String nome) {
        Treino treino = new Treino();
        treino.setNome(nome);
        return treinoRepository.save(treino);
    }

    public List<Treino> listarTodos() {
        return treinoRepository.findAll(Sort.by("nome"));
    }

    public Treino buscarPorId(Long id) {
        return treinoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Treino não encontrado com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        Treino treino = treinoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Treino não encontrado com ID: " + id));
        treinoRepository.delete(treino);
    }
}
