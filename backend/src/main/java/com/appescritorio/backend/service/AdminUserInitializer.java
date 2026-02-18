package com.appescritorio.backend.service;

import com.appescritorio.backend.config.SecurityProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final AuthService authService;
    private final SecurityProperties securityProperties;
    private final Environment environment;

    public AdminUserInitializer(AuthService authService, SecurityProperties securityProperties, Environment environment) {
        this.authService = authService;
        this.securityProperties = securityProperties;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        validateSecrets();

        if (isProdProfile()) {
            return;
        }

        authService.ensureAdminUser(
                securityProperties.getAdmin().getUsername(),
                securityProperties.getAdmin().getPassword());
    }

    private void validateSecrets() {
        String accessSecret = trim(securityProperties.getJwt().getAccessSecret());
        String refreshSecret = trim(securityProperties.getJwt().getRefreshSecret());

        if (accessSecret == null || accessSecret.length() < 32) {
            throw new IllegalStateException("APP_JWT_ACCESS_SECRET debe existir y tener al menos 32 caracteres");
        }

        if (refreshSecret == null || refreshSecret.length() < 32) {
            throw new IllegalStateException("APP_JWT_REFRESH_SECRET debe existir y tener al menos 32 caracteres");
        }

        if (isProdProfile()) {
            String adminUsername = trim(securityProperties.getAdmin().getUsername());
            String adminPassword = trim(securityProperties.getAdmin().getPassword());
            if (adminUsername == null || adminPassword == null) {
                throw new IllegalStateException("APP_ADMIN_USERNAME y APP_ADMIN_PASSWORD son obligatorios fuera de desarrollo");
            }
        }
    }

    private boolean isProdProfile() {
        for (String profile : environment.getActiveProfiles()) {
            if ("prod".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
