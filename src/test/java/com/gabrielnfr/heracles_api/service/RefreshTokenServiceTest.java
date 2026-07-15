package com.gabrielnfr.heracles_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielnfr.heracles_api.model.RefreshToken;
import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void deveCriarRefreshToken() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(refreshTokenRepository.save(any(RefreshToken.class)))
            .thenAnswer(inv -> inv.getArgument(0));

        RefreshToken resultado = refreshTokenService.criar(usuario);

        assertNotNull(resultado.getToken());
        assertEquals(36, resultado.getToken().length()); // UUID = 36 chars
        assertEquals(usuario, resultado.getUsuario());
        assertTrue(resultado.getExpiracao().isAfter(LocalDateTime.now()));

        verify(refreshTokenRepository).deleteByUsuario(usuario);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void deveValidarTokenExistente() {
        RefreshToken token = new RefreshToken();
        token.setToken("uuid-valido");
        token.setExpiracao(LocalDateTime.now().plusDays(10));

        when(refreshTokenRepository.findByToken("uuid-valido"))
            .thenReturn(Optional.of(token));

        RefreshToken resultado = refreshTokenService.validar("uuid-valido");

        assertEquals("uuid-valido", resultado.getToken());
    }

    @Test
    void deveLancarExcecaoQuandoTokenNaoEncontrado() {
        when(refreshTokenRepository.findByToken("inexistente"))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> refreshTokenService.validar("inexistente"));
    }

    @Test
    void deveLancarExcecaoQuandoTokenExpirado() {
        RefreshToken token = new RefreshToken();
        token.setToken("uuid-expirado");
        token.setExpiracao(LocalDateTime.now().minusDays(1));

        when(refreshTokenRepository.findByToken("uuid-expirado"))
            .thenReturn(Optional.of(token));

        assertThrows(IllegalArgumentException.class,
            () -> refreshTokenService.validar("uuid-expirado"));
    }

    @Test
    void deveRevogarTokensDoUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        refreshTokenService.revogar(usuario);

        verify(refreshTokenRepository).deleteByUsuario(usuario);
    }
}