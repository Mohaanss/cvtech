package com.example.demo.repository;

import com.example.demo.domain.RecruteurProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruteurProfileRepository extends JpaRepository<RecruteurProfile, Long> {

    List<RecruteurProfile> findByEntrepriseContainingIgnoreCase(String entreprise);

    List<RecruteurProfile> findByServiceContainingIgnoreCase(String service);
}
