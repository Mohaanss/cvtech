package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    
    private String secret = "mySecretKey1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private long accessTokenExpiration = 3600000; // 1 heure (3600000 ms)
    private long refreshTokenExpiration = 2592000000L; // 30 jours (2592000000 ms)
    
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
    
    public void setAccessTokenExpiration(long accessTokenExpiration) {
        this.accessTokenExpiration = accessTokenExpiration;
    }
    
    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
    
    public void setRefreshTokenExpiration(long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }
} 