package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionId implements Serializable {

    @Column(name = "alternant_id")
    private Long alternantId;

    @Column(name = "ecole_id")
    private Long ecoleId;
}
