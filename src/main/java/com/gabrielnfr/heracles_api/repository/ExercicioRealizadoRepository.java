package com.gabrielnfr.heracles_api.repository;

import com.gabrielnfr.heracles_api.model.ExercicioRealizado;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExercicioRealizadoRepository extends JpaRepository<ExercicioRealizado, Long>{
    List<ExercicioRealizado> findByExecucaoId(Long execucaoId);
}
