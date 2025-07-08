package com.example.demo.repository;

import com.example.demo.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.domain.Utilisateur;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Utilisateur> findByRole(UserRole role);

    @Query("SELECT u FROM Utilisateur u WHERE u.role = :role ORDER BY u.dateCreation DESC")
    List<Utilisateur> findByRoleOrderByDateCreationDesc(@Param("role") UserRole role);
}
