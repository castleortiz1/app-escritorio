package com.appescritorio.backend.security;

import com.appescritorio.backend.config.SecurityProperties;
import com.appescritorio.backend.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecurityProperties securityProperties;

    public JwtService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String generateAccessToken(String username, UserRole role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + securityProperties.getJwt().getAccessExpirationMs());

        return Jwts.builder()
                .subject(username)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(accessKey())
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + securityProperties.getJwt().getRefreshExpirationMs());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(refreshKey())
                .compact();
    }

    public String extractUsernameFromAccessToken(String token) {
        return parseAccessClaims(token).getSubject();
    }

    public String extractUsernameFromRefreshToken(String token) {
        return parseRefreshClaims(token).getSubject();
    }

    public String extractRoleFromAccessToken(String token) {
        return parseAccessClaims(token).get("role", String.class);
    }

    public boolean isAccessTokenValid(String token) {
        parseAccessClaims(token);
        return true;
    }

    public boolean isRefreshTokenValid(String token) {
        parseRefreshClaims(token);
        return true;
    }

    private Claims parseAccessClaims(String token) {
        return Jwts.parser()
                .verifyWith(accessKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims parseRefreshClaims(String token) {
        return Jwts.parser()
                .verifyWith(refreshKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey accessKey() {
        return Keys.hmacShaKeyFor(securityProperties.getJwt().getAccessSecret().getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey refreshKey() {
        return Keys.hmacShaKeyFor(securityProperties.getJwt().getRefreshSecret().getBytes(StandardCharsets.UTF_8));
    }
}
