package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "inscription")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inscription {

    @EmbeddedId
    private InscriptionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("alternantId")
    @JoinColumn(name = "alternant_id")
    private Utilisateur alternant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ecoleId")
    @JoinColumn(name = "ecole_id")
    private Utilisateur ecole;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    private String contrat;
}