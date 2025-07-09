package com.example.demo.controller;

import com.example.demo.domain.UserRole;
import com.example.demo.dto.CreateAlternantDto;
import com.example.demo.dto.CreateEcoleDto;
import com.example.demo.dto.CreateRecruteurDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.RefreshTokenRequestDto;
import com.example.demo.dto.RefreshTokenResponseDto;
import com.example.demo.dto.UtilisateurResponseDto;
import com.example.demo.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "http://localhost:4200")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurResponseDto>> getAllUtilisateurs() {
        List<UtilisateurResponseDto> utilisateurs = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDto> getUtilisateurById(@PathVariable Long id) {
        UtilisateurResponseDto utilisateur = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(utilisateur);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UtilisateurResponseDto> getUtilisateurByEmail(@PathVariable String email) {
        UtilisateurResponseDto utilisateur = utilisateurService.getUtilisateurByEmail(email);
        return ResponseEntity.ok(utilisateur);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UtilisateurResponseDto>> getUtilisateursByRole(@PathVariable UserRole role) {
        List<UtilisateurResponseDto> utilisateurs = utilisateurService.getUtilisateursByRole(role);
        return ResponseEntity.ok(utilisateurs);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        LoginResponseDto response = utilisateurService.login(loginDto);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        RefreshTokenResponseDto response = utilisateurService.refreshToken(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/alternant")
    public ResponseEntity<UtilisateurResponseDto> createAlternant(@Valid @RequestBody CreateAlternantDto dto) {
        UtilisateurResponseDto utilisateur = utilisateurService.createAlternant(dto);
        System.out.println(utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
    }

    @PostMapping("/ecole")
    public ResponseEntity<UtilisateurResponseDto> createEcole(@Valid @RequestBody CreateEcoleDto dto) {
        UtilisateurResponseDto utilisateur = utilisateurService.createEcole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
    }

    @PostMapping("/recruteur")
    public ResponseEntity<UtilisateurResponseDto> createRecruteur(@Valid @RequestBody CreateRecruteurDto dto) {
        UtilisateurResponseDto utilisateur = utilisateurService.createRecruteur(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurResponseDto> getCurrentUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("🔍 Endpoint /me appelé - userId: " + userId);
        
        if (userId != null) {
            try {
                UtilisateurResponseDto utilisateur = utilisateurService.getUtilisateurById(userId);
                System.out.println("✅ Utilisateur trouvé: " + utilisateur);
                return ResponseEntity.ok(utilisateur);
            } catch (Exception e) {
                System.out.println("❌ Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            System.out.println("❌ userId est null - pas d'authentification");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.noContent().build();
    }
}
