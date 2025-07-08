package com.example.demo.repository;

import com.example.demo.domain.AlternantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AlternantProfileRepository extends JpaRepository<AlternantProfile, Long> {

    List<AlternantProfile> findByVilleContainingIgnoreCase(String ville);

    @Query("SELECT a FROM AlternantProfile a WHERE a.nom LIKE %:nom% OR a.prenom LIKE %:prenom%")
    List<AlternantProfile> findByNomOrPrenomContaining(@Param("nom") String nom, @Param("prenom") String prenom);
}
