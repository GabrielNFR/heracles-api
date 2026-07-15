package com.gabrielnfr.heracles_api.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.gabrielnfr.heracles_api.dto.request.LoginRequest;
import com.gabrielnfr.heracles_api.dto.request.RefreshTokenRequest;
import com.gabrielnfr.heracles_api.dto.request.RegisterRequest;
import com.gabrielnfr.heracles_api.dto.response.AuthResponse;
import com.gabrielnfr.heracles_api.model.RefreshToken;
import com.gabrielnfr.heracles_api.model.Usuario;
import com.gabrielnfr.heracles_api.repository.UsuarioRepository;
import com.gabrielnfr.heracles_api.security.JwtService;
import com.gabrielnfr.heracles_api.security.UserDetailsServiceImpl;
import com.gabrielnfr.heracles_api.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email ja cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setUsername(request.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setDataCriacao(LocalDateTime.now());

        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Conta criada com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        UserDetails userDetails;
        try {
            userDetails = userDetailsServiceImpl.loadUserByUsername(request.getEmail());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).get();
        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.criar(usuario);
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.validar(request.getRefreshToken());
        String accessToken = jwtService.generateToken(
            userDetailsServiceImpl.loadUserByUsername(refreshToken.getUsuario().getEmail())
        );
        return ResponseEntity.ok(new AuthResponse(accessToken, request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.validar(request.getRefreshToken());
        refreshTokenService.revogar(refreshToken.getUsuario());
        return ResponseEntity.noContent().build();
    }
}