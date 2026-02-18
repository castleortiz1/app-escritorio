package com.appescritorio.backend.service;

import com.appescritorio.backend.controller.dto.AuthResponse;
import com.appescritorio.backend.controller.dto.LoginRequest;
import com.appescritorio.backend.controller.dto.RefreshTokenRequest;
import com.appescritorio.backend.controller.dto.RegisterRequest;
import com.appescritorio.backend.model.AppUser;
import com.appescritorio.backend.model.UserRole;
import com.appescritorio.backend.repository.AppUserRepository;
import com.appescritorio.backend.security.JwtService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String username = sanitize(request.getUsername());
        String password = sanitize(request.getPassword());

        if (username == null || password == null) {
            throw new IllegalArgumentException("username y password son obligatorios");
        }

        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("el usuario ya existe");
        }

        AppUser user = new AppUser(username, passwordEncoder.encode(password), UserRole.USER);
        appUserRepository.save(user);

        return buildAuthResponse("usuario registrado", user);
    }

    public AuthResponse login(LoginRequest request) {
        String username = sanitize(request.getUsername());
        String password = sanitize(request.getPassword());

        if (username == null || password == null) {
            throw new IllegalArgumentException("username y password son obligatorios");
        }

        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("credenciales inválidas");
        }

        return buildAuthResponse("login exitoso", user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = sanitize(request.getRefreshToken());
        if (refreshToken == null) {
            throw new IllegalArgumentException("refreshToken es obligatorio");
        }

        try {
            if (!jwtService.isRefreshTokenValid(refreshToken)) {
                throw new IllegalArgumentException("refresh token inválido");
            }
            String username = jwtService.extractUsernameFromRefreshToken(refreshToken);
            AppUser user = appUserRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("usuario no encontrado"));

            return buildAuthResponse("token actualizado", user);
        } catch (JwtException ex) {
            throw new IllegalArgumentException("refresh token inválido");
        }
    }

    public void ensureAdminUser(String username, String password) {
        if (sanitize(username) == null || sanitize(password) == null) {
            return;
        }

        if (appUserRepository.existsByUsername(username)) {
            return;
        }

        AppUser admin = new AppUser(username.trim(), passwordEncoder.encode(password.trim()), UserRole.ADMIN);
        appUserRepository.save(admin);
    }

    private AuthResponse buildAuthResponse(String message, AppUser user) {
        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        return new AuthResponse(message, user.getUsername(), user.getRole().name(), accessToken, refreshToken);
    }

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
