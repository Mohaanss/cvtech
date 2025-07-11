package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.dto.CreateAlternantDto;
import com.example.demo.dto.CreateEcoleDto;
import com.example.demo.dto.CreateRecruteurDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.dto.RefreshTokenRequestDto;
import com.example.demo.dto.RefreshTokenResponseDto;
import com.example.demo.dto.UtilisateurResponseDto;
import com.example.demo.dto.AlternantPublicDto;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.UtilisateurNotFoundException;
import com.example.demo.repository.AlternantProfileRepository;
import com.example.demo.repository.EcoleProfileRepository;
import com.example.demo.repository.RecruteurProfileRepository;
import com.example.demo.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final AlternantProfileRepository alternantProfileRepository;
    private final EcoleProfileRepository ecoleProfileRepository;
    private final RecruteurProfileRepository recruteurProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public List<UtilisateurResponseDto> getAllUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UtilisateurResponseDto getUtilisateurById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        return convertToDto(utilisateur);
    }

    public UtilisateurResponseDto getUtilisateurByEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'email : " + email));
        return convertToDto(utilisateur);
    }

    public List<UtilisateurResponseDto> getUtilisateursByRole(UserRole role) {
        return utilisateurRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UtilisateurResponseDto createAlternant(CreateAlternantDto dto) {
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Un utilisateur avec cet email existe déjà");
        }

        Utilisateur utilisateur = Utilisateur.builder()
                .email(dto.getEmail())
                .motDePasseHash(passwordEncoder.encode(dto.getMotDePasse()))
                .role(UserRole.ALTERNANT)
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);

        AlternantProfile profile = AlternantProfile.builder()
                .utilisateur(utilisateur)
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .telephone(dto.getTelephone())
                .ville(dto.getVille())
                .lienLinkedin(dto.getLienLinkedin())
                .lienPortfolio(dto.getLienPortfolio())
                .dateNaissance(dto.getDateNaissance() != null && !dto.getDateNaissance().isEmpty() ? java.time.LocalDate.parse(dto.getDateNaissance()) : null)
                .ecole(dto.getEcole())
                .niveauEtude(dto.getNiveauEtude())
                .categorieEtude(dto.getCategorieEtude())
                .build();

        alternantProfileRepository.save(profile);

        return convertToDto(utilisateur);
    }

    public UtilisateurResponseDto createEcole(CreateEcoleDto dto) {
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Un utilisateur avec cet email existe déjà");
        }

        // Créer l'utilisateur
        Utilisateur utilisateur = Utilisateur.builder()
                .email(dto.getEmail())
                .motDePasseHash(passwordEncoder.encode(dto.getMotDePasse()))
                .role(UserRole.ECOLE)
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);

        // Créer le profil école
        EcoleProfile profile = EcoleProfile.builder()
                .utilisateur(utilisateur)
                .nomEcole(dto.getNomEcole())
                .adresse(dto.getAdresse())
                .siteWeb(dto.getSiteWeb())
                .build();

        ecoleProfileRepository.save(profile);

        return convertToDto(utilisateur);
    }

    public UtilisateurResponseDto createRecruteur(CreateRecruteurDto dto) {
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Un utilisateur avec cet email existe déjà");
        }

        // Créer l'utilisateur
        Utilisateur utilisateur = Utilisateur.builder()
                .email(dto.getEmail())
                .motDePasseHash(passwordEncoder.encode(dto.getMotDePasse()))
                .role(UserRole.RECRUTEUR)
                .build();

        utilisateur = utilisateurRepository.save(utilisateur);

        // Créer le profil recruteur
        RecruteurProfile profile = RecruteurProfile.builder()
                .utilisateur(utilisateur)
                .entreprise(dto.getEntreprise())
                .service(dto.getService())
                .telephone(dto.getTelephone())
                .siteWeb(dto.getSiteWeb())
                .build();

        recruteurProfileRepository.save(profile);

        return convertToDto(utilisateur);
    }

    public LoginResponseDto login(LoginDto loginDto) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(loginDto.getEmail());
        
        if (utilisateurOpt.isEmpty()) {
            return new LoginResponseDto("Email ou mot de passe incorrect");
        }
        
        Utilisateur utilisateur = utilisateurOpt.get();
        
        // Vérifier le mot de passe
        if (!passwordEncoder.matches(loginDto.getMotDePasse(), utilisateur.getMotDePasseHash())) {
            return new LoginResponseDto("Email ou mot de passe incorrect");
        }
        
        // Générer les tokens JWT
        String accessToken = jwtService.generateAccessToken(
            utilisateur.getId(), 
            utilisateur.getEmail(), 
            utilisateur.getRole()
        );
        String refreshToken = jwtService.generateRefreshToken(
            utilisateur.getId(), 
            utilisateur.getEmail()
        );
        
        // Connexion réussie avec tokens
        return new LoginResponseDto(
            utilisateur.getId(),
            utilisateur.getEmail(),
            utilisateur.getRole(),
            utilisateur.getDateCreation(),
            accessToken,
            refreshToken,
            3600000L // 1 heure en millisecondes
        );
    }

    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request) {
        try {
            String refreshToken = request.getRefreshToken();
            
            // Vérifier que c'est un refresh token valide
            if (!jwtService.isRefreshToken(refreshToken) || jwtService.isTokenExpired(refreshToken)) {
                return new RefreshTokenResponseDto("Token de rafraîchissement invalide ou expiré");
            }
            
            // Extraire les informations du refresh token
            String email = jwtService.extractEmail(refreshToken);
            Long userId = jwtService.extractUserId(refreshToken);
            
            // Vérifier que l'utilisateur existe toujours
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
            if (utilisateurOpt.isEmpty()) {
                return new RefreshTokenResponseDto("Utilisateur non trouvé");
            }
            
            Utilisateur utilisateur = utilisateurOpt.get();
            
            // Générer de nouveaux tokens
            String newAccessToken = jwtService.generateAccessToken(
                userId, 
                email, 
                utilisateur.getRole()
            );
            String newRefreshToken = jwtService.generateRefreshToken(userId, email);
            
            return new RefreshTokenResponseDto(newAccessToken, newRefreshToken, 3600000L);
            
        } catch (Exception e) {
            return new RefreshTokenResponseDto("Erreur lors du rafraîchissement du token");
        }
    }

    public void deleteUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
        utilisateurRepository.deleteById(id);
    }

    /**
     * Réinitialiser le mot de passe d'un utilisateur
     */
    public boolean resetPassword(String email, String newPassword) {
        try {
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
            if (utilisateurOpt.isEmpty()) {
                return false;
            }
            
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.setMotDePasseHash(passwordEncoder.encode(newPassword));
            utilisateurRepository.save(utilisateur);
            
            System.out.println("✅ Mot de passe réinitialisé pour: " + email);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la réinitialisation du mot de passe: " + e.getMessage());
            return false;
        }
    }

    public List<AlternantPublicDto> getPublicAlternants() {
        return alternantProfileRepository.findAll().stream()
            .map(profile -> new AlternantPublicDto(
                profile.getNom(),
                profile.getPrenom(),
                profile.getNiveauEtude(),
                profile.getEcole(),
                profile.getCategorieEtude(),
                profile.getVille()
            ))
            .toList();
    }

    private UtilisateurResponseDto convertToDto(Utilisateur utilisateur) {
        UtilisateurResponseDto dto = new UtilisateurResponseDto();
        dto.setId(utilisateur.getId());
        dto.setEmail(utilisateur.getEmail());
        dto.setRole(utilisateur.getRole());
        dto.setDateCreation(utilisateur.getDateCreation());

        // Si alternant, ajouter les infos du profil
        if (utilisateur.getRole() == UserRole.ALTERNANT) {
            AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateur.getId()).orElse(null);
            if (profile != null) {
                dto.setNom(profile.getNom());
                dto.setPrenom(profile.getPrenom());
                dto.setTelephone(profile.getTelephone());
                dto.setVille(profile.getVille());
                dto.setLienLinkedin(profile.getLienLinkedin());
                dto.setLienPortfolio(profile.getLienPortfolio());
                dto.setDateNaissance(profile.getDateNaissance() != null ? profile.getDateNaissance().toString() : null);
                dto.setEcole(profile.getEcole());
                dto.setNiveauEtude(profile.getNiveauEtude());
                dto.setCategorieEtude(profile.getCategorieEtude());
            }
        }
        return dto;
    }
}
