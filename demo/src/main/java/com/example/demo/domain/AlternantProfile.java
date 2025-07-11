package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "alternant_profile")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlternantProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "ville")
    private String ville;
    
    @Column(name = "lien_linkedin")
    private String lienLinkedin;
    
    @Column(name = "lien_portfolio")
    private String lienPortfolio;
    
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
    
    @Column(name = "cv_nom_fichier")
    private String cvNomFichier;
    
    @Column(name = "cv_original_name")
    private String cvOriginalName;
    
    @Column(name = "cv_date_upload")
    private LocalDate cvDateUpload;

    // Nouveaux champs
    private String ecole;
    private String niveauEtude;
    private String categorieEtude;
}