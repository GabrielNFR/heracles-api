package com.gabrielnfr.heracles_api.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import com.gabrielnfr.heracles_api.model.Usuario;

import io.jsonwebtoken.ExpiredJwtException;

import org.junit.jupiter.api.Test;

public class JwtServiceTest {
    private JwtService jwtService;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", "chave-secreta-32-bytes-tamanho-minimo!!");
        ReflectionTestUtils.setField(jwtService, "expiration", 60000L);
        userDetails = new UserDetailsImpl(createUsuario());
    }

        private Usuario createUsuario() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setEmail("teste@email.com");
        u.setUsername("teste");
        u.setPasswordHash("hash");
        return u;
    }

    @Test
    void deveGerarTokenValido() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    void deveExtrairEmailDoToken() {
        String token = jwtService.generateToken(userDetails);
        String email = jwtService.extractEmail(token);

        assertEquals("teste@email.com", email);
    }

    @Test
    void deveValidarTokenCorreto() {
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void deveRejeitarTokenDeOutroUsuario() {
        String token = jwtService.generateToken(userDetails);

        Usuario outro = new Usuario();
        outro.setId(2L); outro.setEmail("outro@email.com");
        outro.setUsername("outro"); outro.setPasswordHash("h");
        UserDetailsImpl outroUser = new UserDetailsImpl(outro);

        assertFalse(jwtService.isTokenValid(token, outroUser));
    }

    @Test
    void deveRejeitarTokenExpirado() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "expiration", 1L);
        String token = jwtService.generateToken(userDetails);
        Thread.sleep(10);

        assertThrows(ExpiredJwtException.class,
            () -> jwtService.isTokenValid(token, userDetails));
    }
}
