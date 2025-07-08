package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlternantDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String motDePasse;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    private String telephone;
    private String ville;
    private String lienLinkedin;
    private String lienPortfolio;
    private LocalDate dateNaissance;
}