package com.gabrielnfr.heracles_api.service;

import org.springframework.stereotype.Service;

import com.gabrielnfr.heracles_api.repository.TreinoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.gabrielnfr.heracles_api.model.Exercicio;
import com.gabrielnfr.heracles_api.model.Treino;
import com.gabrielnfr.heracles_api.model.Usuario;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreinoService {
    private final TreinoRepository treinoRepository;

    @Transactional
    public Treino criar(Usuario usuario, String nome, List<String> nomesExercicios) {
        Treino treino = new Treino();
        treino.setNome(nome);

        List<Exercicio> exercicios = new ArrayList<>();
        for (String nomeEx : nomesExercicios) {
            Exercicio exercicio = new Exercicio();
            exercicio.setNome(nomeEx);
            exercicio.setTreino(treino);
            exercicios.add(exercicio);
        }
        treino.setExercicios(exercicios);
        treino.setUsuario(usuario);

        return treinoRepository.save(treino);
    }

    public List<Treino> listarTodos(Long usuarioId) {
        return treinoRepository.findByUsuarioId(usuarioId);
    }

    public Treino buscarPorId(Long id, Long usuarioId) {
        Treino treino = treinoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Treino nao encontrado com ID: " + id));
        if (!treino.getUsuario().getId().equals(usuarioId)) {
            throw new EntityNotFoundException("Treino nao encontrado com ID: " + id);
        }
        return treino;
    }

    @Transactional
    public void deletar(Long id, Long usuarioId) {
        Treino treino = treinoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Treino nao encontrado com ID: " + id));
        if (!treino.getUsuario().getId().equals(usuarioId)) {
            throw new EntityNotFoundException("Treino nao encontrado com ID: " + id);
        }
        treinoRepository.delete(treino);
    }
}
