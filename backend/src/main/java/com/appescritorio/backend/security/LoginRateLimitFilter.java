package com.appescritorio.backend.security;

import com.appescritorio.backend.config.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;
    private final Map<String, AttemptsWindow> attemptsByIp = new ConcurrentHashMap<>();

    public LoginRateLimitFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"POST".equalsIgnoreCase(request.getMethod()) || !"/api/auth/login".equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String key = resolveClientIp(request);
        long now = System.currentTimeMillis();
        int maxAttempts = securityProperties.getRateLimit().getLoginMaxAttempts();
        long window = securityProperties.getRateLimit().getLoginWindowMs();

        AttemptsWindow attemptsWindow = attemptsByIp.computeIfAbsent(key, ignored -> new AttemptsWindow());
        synchronized (attemptsWindow) {
            if (now - attemptsWindow.windowStart > window) {
                attemptsWindow.windowStart = now;
                attemptsWindow.attempts = 0;
            }

            attemptsWindow.attempts++;
            if (attemptsWindow.attempts > maxAttempts) {
                response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"error\":\"demasiados intentos de login, intenta más tarde\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static final class AttemptsWindow {
        private long windowStart = System.currentTimeMillis();
        private int attempts = 0;
    }
}
