package com.example.demo.controller;

import com.example.demo.domain.AlternantProfile;
import com.example.demo.service.AlternantProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/alternant")
public class AlternantController {

    private final AlternantProfileService alternantProfileService;

    public AlternantController(AlternantProfileService alternantProfileService) {
        this.alternantProfileService = alternantProfileService;
    }

    /**
     * Récupérer le profil de l'alternant connecté
     */
    @GetMapping("/profile")
    public ResponseEntity<AlternantProfile> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        AlternantProfile profile = alternantProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    /**
     * Mettre à jour le profil de l'alternant connecté
     */
    @PutMapping("/profile")
    public ResponseEntity<AlternantProfile> updateProfile(@RequestBody AlternantProfile profile, 
                                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        AlternantProfile updatedProfile = alternantProfileService.updateProfile(userId, profile);
        return ResponseEntity.ok(updatedProfile);
    }
} 