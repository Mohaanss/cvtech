package com.example.demo.repository;

import com.example.demo.domain.EcoleProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcoleProfileRepository extends JpaRepository<EcoleProfile, Long> {

    List<EcoleProfile> findByNomEcoleContainingIgnoreCase(String nomEcole);

    List<EcoleProfile> findByAdresseContainingIgnoreCase(String adresse);
}
