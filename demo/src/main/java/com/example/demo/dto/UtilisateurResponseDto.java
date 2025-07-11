package com.example.demo.dto;

import com.example.demo.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponseDto {
    private Long id;
    private String email;
    private UserRole role;
    private ZonedDateTime dateCreation;
    private String ville;
    private String lienLinkedin;
    private String lienPortfolio;
    private String dateNaissance;

    // Nouveaux champs
    private String ecole;
    private String niveauEtude;
    private String categorieEtude;
    private String nom;
    private String prenom;
    private String telephone;
}
