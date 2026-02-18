package com.appescritorio.backend.service;

import com.appescritorio.backend.controller.dto.AuthResponse;
import com.appescritorio.backend.controller.dto.LoginRequest;
import com.appescritorio.backend.controller.dto.RegisterRequest;
import com.appescritorio.backend.model.AppUser;
import com.appescritorio.backend.model.UserRole;
import com.appescritorio.backend.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
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

        return new AuthResponse("usuario registrado", user.getUsername(), user.getRole().name());
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

        return new AuthResponse("login exitoso", user.getUsername(), user.getRole().name());
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

    private String sanitize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
