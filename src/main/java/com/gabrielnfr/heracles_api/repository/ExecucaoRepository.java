package com.gabrielnfr.heracles_api.repository;

import com.gabrielnfr.heracles_api.model.Execucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecucaoRepository extends JpaRepository<Execucao, Long> {
    List<Execucao> findByTreinoId(Long treino_id);
}
