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

    // Constructeur pour succès
    public LoginResponseDto(Long id, String email, UserRole role, ZonedDateTime dateCreation) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.dateCreation = dateCreation;
        this.success = true;
        this.message = "Connexion réussie";
    }

    // Constructeur pour échec
    public LoginResponseDto(String message) {
        this.success = false;
        this.message = message;
    }
} 