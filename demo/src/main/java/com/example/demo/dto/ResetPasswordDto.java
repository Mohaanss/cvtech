package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    
    @NotBlank
    private String token;
    
    @NotBlank
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String newPassword;
    
    @NotBlank
    private String confirmPassword;
} 