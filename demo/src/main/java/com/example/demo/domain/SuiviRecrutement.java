package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "suivi_recrutement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuiviRecrutement {

    @EmbeddedId
    private SuiviRecrutementId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("alternantId")
    @JoinColumn(name = "alternant_id")
    private Utilisateur alternant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recruteurId")
    @JoinColumn(name = "recruteur_id")
    private Utilisateur recruteur;

    @Column(name = "date_premier_contact")
    private LocalDate datePremierContact;

    @Column(name = "statut_process")
    private String statutProcess;
}

