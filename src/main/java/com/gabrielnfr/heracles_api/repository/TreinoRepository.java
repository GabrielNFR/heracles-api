package com.gabrielnfr.heracles_api.repository;

import com.gabrielnfr.heracles_api.model.Treino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long> {
    
}
