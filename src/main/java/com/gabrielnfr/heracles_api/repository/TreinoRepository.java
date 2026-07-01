package com.gabrielnfr.heracles_api.repository;

import com.gabrielnfr.heracles_api.model.Treino;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long> {
    List<Treino> findByUsuarioId(Long usuarioId);
}
