package com.gabrielnfr.heracles_api.repository;

import org.springframework.stereotype.Repository;
import com.gabrielnfr.heracles_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
}
