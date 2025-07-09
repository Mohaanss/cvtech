package com.example.demo.dto;

import com.example.demo.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private Long id;
    private String email;
    private UserRole role;
    private ZonedDateTime dateCreation;
    private String message;
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiresIn;

    // Constructeur pour succès avec tokens
    public LoginResponseDto(Long id, String email, UserRole role, ZonedDateTime dateCreation, 
                           String accessToken, String refreshToken, long accessTokenExpiresIn) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.dateCreation = dateCreation;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.success = true;
        this.message = "Connexion réussie";
    }

    // Constructeur pour échec
    public LoginResponseDto(String message) {
        this.success = false;
        this.message = message;
    }
} 