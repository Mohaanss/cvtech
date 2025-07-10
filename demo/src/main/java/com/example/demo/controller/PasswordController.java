package com.example.demo.controller;

import com.example.demo.dto.ForgotPasswordDto;
import com.example.demo.dto.ResetPasswordDto;
import com.example.demo.service.EmailService;
import com.example.demo.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordController {
    
    private final EmailService emailService;
    private final UtilisateurService utilisateurService;
    
    public PasswordController(EmailService emailService, UtilisateurService utilisateurService) {
        this.emailService = emailService;
        this.utilisateurService = utilisateurService;
    }
    
    /**
     * Demande de réinitialisation de mot de passe
     */
    @PostMapping("/forgot")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean emailSent = emailService.sendPasswordResetEmail(forgotPasswordDto.getEmail());
            
            if (emailSent) {
                response.put("success", true);
                response.put("message", "Si un compte existe avec cet email, un lien de réinitialisation a été envoyé.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Aucun compte trouvé avec cet email.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de l'envoi de l'email de réinitialisation.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Réinitialisation de mot de passe avec token
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Vérifier que les mots de passe correspondent
            if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
                response.put("success", false);
                response.put("message", "Les mots de passe ne correspondent pas.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Valider le token
            String email = emailService.validateResetToken(resetPasswordDto.getToken());
            if (email == null) {
                response.put("success", false);
                response.put("message", "Token de réinitialisation invalide ou expiré.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Réinitialiser le mot de passe
            boolean passwordReset = utilisateurService.resetPassword(email, resetPasswordDto.getNewPassword());
            
            if (passwordReset) {
                // Supprimer le token après utilisation
                emailService.removeResetToken(resetPasswordDto.getToken());
                
                // Envoyer un email de confirmation
                emailService.sendPasswordChangedEmail(email);
                
                response.put("success", true);
                response.put("message", "Mot de passe réinitialisé avec succès.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Erreur lors de la réinitialisation du mot de passe.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la réinitialisation du mot de passe.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Valider un token de réinitialisation
     */
    @GetMapping("/validate-token/{token}")
    public ResponseEntity<Map<String, Object>> validateToken(@PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        
        String email = emailService.validateResetToken(token);
        
        if (email != null) {
            response.put("valid", true);
            response.put("email", email);
            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);
            response.put("message", "Token invalide ou expiré.");
            return ResponseEntity.badRequest().body(response);
        }
    }
} 