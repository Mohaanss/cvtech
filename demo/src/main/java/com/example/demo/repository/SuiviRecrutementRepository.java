package com.example.demo.repository;

import com.example.demo.domain.SuiviRecrutement;
import com.example.demo.domain.SuiviRecrutementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuiviRecrutementRepository extends JpaRepository<SuiviRecrutement, SuiviRecrutementId> {

    List<SuiviRecrutement> findByAlternantId(Long alternantId);

    List<SuiviRecrutement> findByRecruteurId(Long recruteurId);

    List<SuiviRecrutement> findByStatutProcess(String statutProcess);
}