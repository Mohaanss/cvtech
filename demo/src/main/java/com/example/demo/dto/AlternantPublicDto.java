package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlternantPublicDto {
    private String nom;
    private String prenom;
    private String niveauEtude;
    private String ecole;
    private String categorieEtude;
    private String ville;
} 