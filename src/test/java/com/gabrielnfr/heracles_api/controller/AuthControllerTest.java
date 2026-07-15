package com.gabrielnfr.heracles_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gabrielnfr.heracles_api.dto.request.LoginRequest;
import com.gabrielnfr.heracles_api.dto.request.RefreshTokenRequest;
import com.gabrielnfr.heracles_api.dto.request.RegisterRequest;
import com.gabrielnfr.heracles_api.dto.response.AuthResponse;
import com.gabrielnfr.heracles_api.model.RefreshToken;
import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.repository.UsuarioRepository;
import com.gabrielnfr.heracles_api.security.JwtService;
import com.gabrielnfr.heracles_api.security.UserDetailsImpl;
import com.gabrielnfr.heracles_api.security.UserDetailsServiceImpl;
import com.gabrielnfr.heracles_api.service.RefreshTokenService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private RefreshTokenService refreshTokenService;
    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private Usuario usuario;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setup() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("teste@email.com");
        registerRequest.setUsername("teste");
        registerRequest.setPassword("123456");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setPasswordHash("hash");
        usuario.setUsername("teste");

        userDetails = new UserDetailsImpl(usuario);
    }

    @Test
    void deveRegistrarUsuario() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Conta criada com sucesso", response.getBody());
    }

    @Test
    void deveRejeitarEmailDuplicado() {
        when(usuarioRepository.findByEmail("teste@email.com"))
            .thenReturn(Optional.of(new Usuario()));

        ResponseEntity<String> response = authController.register(registerRequest);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Email ja cadastrado", response.getBody());
    }

    @Test
    void deveLogarComSucesso() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@email.com");
        loginRequest.setPassword("123456");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-uuid-123");
        refreshToken.setUsuario(usuario);

        when(userDetailsServiceImpl.loadUserByUsername("teste@email.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(userDetails)).thenReturn("token-teste-123");
        when(refreshTokenService.criar(usuario)).thenReturn(refreshToken);

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("token-teste-123", response.getBody().getToken());
        assertEquals("refresh-uuid-123", response.getBody().getRefreshToken());
    }

    @Test
    void deveRejeitarSenhaIncorreta() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@email.com");
        loginRequest.setPassword("senha-errada");

        when(userDetailsServiceImpl.loadUserByUsername("teste@email.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("senha-errada", "hash")).thenReturn(false);

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void deveRenovarTokenComRefreshToken() {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken("refresh-uuid-123");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-uuid-123");
        refreshToken.setUsuario(usuario);

        when(refreshTokenService.validar("refresh-uuid-123")).thenReturn(refreshToken);
        when(userDetailsServiceImpl.loadUserByUsername("teste@email.com")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("novo-token-456");

        ResponseEntity<AuthResponse> response = authController.refresh(refreshRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("novo-token-456", response.getBody().getToken());
        assertEquals("refresh-uuid-123", response.getBody().getRefreshToken());
    }

    @Test
    void deveFazerLogout() {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken("refresh-uuid-123");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-uuid-123");
        refreshToken.setUsuario(usuario);

        when(refreshTokenService.validar("refresh-uuid-123")).thenReturn(refreshToken);

        ResponseEntity<Void> response = authController.logout(refreshRequest);

        assertEquals(204, response.getStatusCode().value());
    }
}