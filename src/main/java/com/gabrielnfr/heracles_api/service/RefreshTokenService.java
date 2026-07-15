package com.gabrielnfr.heracles_api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabrielnfr.heracles_api.model.RefreshToken;
import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken criar(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(usuario);
        token.setExpiracao(LocalDateTime.now().plusDays(30));
        return refreshTokenRepository.save(token);
    }

    public RefreshToken validar(String token) {
        return refreshTokenRepository.findByToken(token)
            .filter(t -> t.getExpiracao().isAfter(LocalDateTime.now()))
            .orElseThrow(() -> new IllegalArgumentException("Refresh token invalido ou expirado"));
    }

    @Transactional
    public void revogar(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);
    }
}