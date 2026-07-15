package com.gabrielnfr.heracles_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielnfr.heracles_api.model.RefreshToken;
import com.gabrielnfr.heracles_api.model.Usuario;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsuario(Usuario usuario);
}
