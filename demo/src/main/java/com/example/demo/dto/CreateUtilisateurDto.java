package com.example.demo.dto;

import com.example.demo.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUtilisateurDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String motDePasse;

    @NotNull
    private UserRole role;
}
