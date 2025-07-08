package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "alternant_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlternantProfile {

    @Id
    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    private String nom;
    private String prenom;
    private String telephone;
    private String ville;

    @Column(name = "lien_linkedin")
    private String lienLinkedin;

    @Column(name = "lien_portfolio")
    private String lienPortfolio;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
}