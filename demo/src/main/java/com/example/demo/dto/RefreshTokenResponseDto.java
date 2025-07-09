package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponseDto {
    
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresIn;
    private boolean success;
    private String message;
    
    // Constructeur pour succès
    public RefreshTokenResponseDto(String accessToken, String refreshToken, long accessTokenExpiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.success = true;
        this.message = "Token rafraîchi avec succès";
    }
    
    // Constructeur pour échec
    public RefreshTokenResponseDto(String message) {
        this.success = false;
        this.message = message;
    }
} 