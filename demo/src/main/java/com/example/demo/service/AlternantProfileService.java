package com.example.demo.service;

import com.example.demo.domain.AlternantProfile;
import com.example.demo.domain.Utilisateur;
import com.example.demo.dto.CvFileUploadDto;
import com.example.demo.dto.CvResponseDto;
import com.example.demo.repository.AlternantProfileRepository;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@Transactional
public class AlternantProfileService {
    
    private final AlternantProfileRepository alternantProfileRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FileStorageService fileStorageService;
    
    public AlternantProfileService(AlternantProfileRepository alternantProfileRepository,
                                 UtilisateurRepository utilisateurRepository,
                                 FileStorageService fileStorageService) {
        this.alternantProfileRepository = alternantProfileRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.fileStorageService = fileStorageService;
    }
    
    /**
     * Upload du CV d'un alternant
     */
    public CvResponseDto uploadCv(Long utilisateurId, MultipartFile file) {
        // Vérifier que l'utilisateur existe et est un alternant
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
        if (utilisateur.getRole() != com.example.demo.domain.UserRole.ALTERNANT) {
            throw new RuntimeException("Seuls les alternants peuvent uploader un CV");
        }
        
        // Stocker le fichier sur le disque
        String storedFileName = fileStorageService.storeFile(file);
        
        // Récupérer ou créer le profil alternant
        AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseGet(() -> {
                AlternantProfile newProfile = new AlternantProfile();
                newProfile.setUtilisateur(utilisateur);
                return newProfile;
            });
        
        // Supprimer l'ancien fichier s'il existe
        if (profile.getCvNomFichier() != null) {
            fileStorageService.deleteFile(profile.getCvNomFichier());
        }
        
        // Mettre à jour le CV
        profile.setCvNomFichier(storedFileName);
        profile.setCvOriginalName(file.getOriginalFilename());
        profile.setCvDateUpload(LocalDate.now());
        
        AlternantProfile savedProfile = alternantProfileRepository.save(profile);
        
        return new CvResponseDto(
            savedProfile.getId(),
            savedProfile.getCvOriginalName(),
            "application/pdf",
            savedProfile.getCvDateUpload(),
            true
        );
    }
    
    /**
     * Récupérer les informations du CV d'un alternant
     */
    public CvResponseDto getCvInfo(Long utilisateurId) {
        AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElse(null);
            
        if (profile == null || profile.getCvNomFichier() == null) {
            return new CvResponseDto(null, null, null, null, false);
        }
        
        return new CvResponseDto(
            profile.getId(),
            profile.getCvOriginalName(),
            "application/pdf",
            profile.getCvDateUpload(),
            true
        );
    }
    
    /**
     * Télécharger le CV d'un alternant
     */
    public byte[] downloadCv(Long utilisateurId) {
        AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseThrow(() -> new RuntimeException("CV non trouvé"));
            
        if (profile.getCvNomFichier() == null) {
            throw new RuntimeException("Aucun CV uploadé");
        }
        
        return fileStorageService.loadFileAsBytes(profile.getCvNomFichier());
    }
    
    /**
     * Supprimer le CV d'un alternant
     */
    public void deleteCv(Long utilisateurId) {
        AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Profil alternant non trouvé"));
            
        // Supprimer le fichier du disque
        if (profile.getCvNomFichier() != null) {
            fileStorageService.deleteFile(profile.getCvNomFichier());
        }
        
        // Nettoyer les champs en base
        profile.setCvNomFichier(null);
        profile.setCvOriginalName(null);
        profile.setCvDateUpload(null);
        
        alternantProfileRepository.save(profile);
    }

    /**
     * Récupérer le profil d'un alternant par son ID utilisateur
     */
    public AlternantProfile getProfileByUserId(Long utilisateurId) {
        return alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Profil alternant non trouvé"));
    }

    /**
     * Mettre à jour le profil d'un alternant
     */
    public AlternantProfile updateProfile(Long utilisateurId, AlternantProfile updatedProfile) {
        AlternantProfile existingProfile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Profil alternant non trouvé"));
        
        // Mettre à jour les champs modifiables
        existingProfile.setNom(updatedProfile.getNom());
        existingProfile.setPrenom(updatedProfile.getPrenom());
        existingProfile.setTelephone(updatedProfile.getTelephone());
        existingProfile.setVille(updatedProfile.getVille());
        existingProfile.setLienLinkedin(updatedProfile.getLienLinkedin());
        existingProfile.setLienPortfolio(updatedProfile.getLienPortfolio());
        existingProfile.setDateNaissance(updatedProfile.getDateNaissance());
        
        return alternantProfileRepository.save(existingProfile);
    }
} 