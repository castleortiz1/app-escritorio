package com.appescritorio.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private final Jwt jwt = new Jwt();
    private final Admin admin = new Admin();
    private final Cors cors = new Cors();
    private final RateLimit rateLimit = new RateLimit();

    public Jwt getJwt() {
        return jwt;
    }

    public Admin getAdmin() {
        return admin;
    }

    public Cors getCors() {
        return cors;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public static class Jwt {
        private String accessSecret;
        private String refreshSecret;
        private long accessExpirationMs = 900000;
        private long refreshExpirationMs = 604800000;

        public String getAccessSecret() {
            return accessSecret;
        }

        public void setAccessSecret(String accessSecret) {
            this.accessSecret = accessSecret;
        }

        public String getRefreshSecret() {
            return refreshSecret;
        }

        public void setRefreshSecret(String refreshSecret) {
            this.refreshSecret = refreshSecret;
        }

        public long getAccessExpirationMs() {
            return accessExpirationMs;
        }

        public void setAccessExpirationMs(long accessExpirationMs) {
            this.accessExpirationMs = accessExpirationMs;
        }

        public long getRefreshExpirationMs() {
            return refreshExpirationMs;
        }

        public void setRefreshExpirationMs(long refreshExpirationMs) {
            this.refreshExpirationMs = refreshExpirationMs;
        }
    }

    public static class Admin {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Cors {
        private String allowedOrigins = "http://localhost:3000";
        private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";
        private String allowedHeaders = "Authorization,Content-Type";

        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public String getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public String getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }
    }

    public static class RateLimit {
        private int loginMaxAttempts = 5;
        private long loginWindowMs = 60000;

        public int getLoginMaxAttempts() {
            return loginMaxAttempts;
        }

        public void setLoginMaxAttempts(int loginMaxAttempts) {
            this.loginMaxAttempts = loginMaxAttempts;
        }

        public long getLoginWindowMs() {
            return loginWindowMs;
        }

        public void setLoginWindowMs(long loginWindowMs) {
            this.loginWindowMs = loginWindowMs;
        }
    }
}
