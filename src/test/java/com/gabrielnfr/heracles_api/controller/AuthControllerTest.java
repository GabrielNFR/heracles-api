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
import com.gabrielnfr.heracles_api.dto.request.RegisterRequest;
import com.gabrielnfr.heracles_api.dto.response.AuthResponse;
import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.repository.UsuarioRepository;
import com.gabrielnfr.heracles_api.security.JwtService;
import com.gabrielnfr.heracles_api.security.UserDetailsImpl;
import com.gabrielnfr.heracles_api.security.UserDetailsServiceImpl;

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
    @InjectMocks
    private AuthController authController;

    private RegisterRequest request;

    @BeforeEach
    void setup() {
        request = new RegisterRequest();
        request.setEmail("teste@email.com");
        request.setUsername("teste");
        request.setPassword("123456");
    }

    @Test
    void deveRegistrarUsuario() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        ResponseEntity<String> response = authController.register(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Conta criada com sucesso", response.getBody());
    }
    
    @Test
    void deveRejeitarEmailDuplicado() {
        when(usuarioRepository.findByEmail("teste@email.com"))
            .thenReturn(Optional.of(new Usuario()));

        ResponseEntity<String> response = authController.register(request);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Email já cadastrado", response.getBody());
    }

    @Test
    void deveLogarComSucesso() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@email.com");
        loginRequest.setPassword("123456");
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setPasswordHash("hash");
        usuario.setUsername("teste@email.com");

        UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

        when(userDetailsServiceImpl.loadUserByUsername("teste@email.com"))
            .thenReturn(userDetails);
        when(passwordEncoder.matches("123456", "hash")).thenReturn(true);
        when(jwtService.generateToken(userDetails)).thenReturn("token-teste-123");

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("token-teste-123", response.getBody().getToken());
    }

    @Test
    void deveRejeitarSenhaIncorreta() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teste@email.com");
        loginRequest.setPassword("senha-errada");
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setPasswordHash("hash");
        usuario.setUsername("teste@email.com");

        UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

        when(userDetailsServiceImpl.loadUserByUsername("teste@email.com"))
            .thenReturn(userDetails);
        when(passwordEncoder.matches("senha-errada", "hash")).thenReturn(false);

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(401, response.getStatusCode().value());
    }
}
