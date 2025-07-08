package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ecole_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcoleProfile {

    @Id
    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "nom_ecole")
    private String nomEcole;

    private String adresse;

    @Column(name = "site_web")
    private String siteWeb;
}
