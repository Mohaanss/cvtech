package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruteur_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruteurProfile {

    @Id
    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    private String entreprise;
    private String service;
    private String telephone;

    @Column(name = "site_web")
    private String siteWeb;
}
