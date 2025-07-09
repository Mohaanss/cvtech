package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvPublicDto {
    private Long id;
    private String nom;
    private String prenom;
    private String ville;
    private String telephone;
    private String lienLinkedin;
    private String cvNomFichier;
    private LocalDate cvDateUpload;
    private boolean hasCv;
} 