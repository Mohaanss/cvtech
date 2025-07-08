package com.example.demo.repository;

import com.example.demo.domain.Inscription;
import com.example.demo.domain.InscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, InscriptionId> {

    List<Inscription> findByAlternantId(Long alternantId);

    List<Inscription> findByEcoleId(Long ecoleId);
}
