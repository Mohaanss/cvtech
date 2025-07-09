package com.example.demo.service;

import com.example.demo.domain.AlternantProfile;
import com.example.demo.domain.Utilisateur;
import com.example.demo.dto.CvUploadDto;
import com.example.demo.dto.CvResponseDto;
import com.example.demo.repository.AlternantProfileRepository;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Base64;

@Service
@Transactional
public class AlternantProfileService {
    
    private final AlternantProfileRepository alternantProfileRepository;
    private final UtilisateurRepository utilisateurRepository;
    
    public AlternantProfileService(AlternantProfileRepository alternantProfileRepository,
                                 UtilisateurRepository utilisateurRepository) {
        this.alternantProfileRepository = alternantProfileRepository;
        this.utilisateurRepository = utilisateurRepository;
    }
    
    /**
     * Upload du CV d'un alternant
     */
    public CvResponseDto uploadCv(Long utilisateurId, CvUploadDto cvUploadDto) {
        // Vérifier que l'utilisateur existe et est un alternant
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
        if (utilisateur.getRole() != com.example.demo.domain.UserRole.ALTERNANT) {
            throw new RuntimeException("Seuls les alternants peuvent uploader un CV");
        }
        
        // Vérifier que le fichier est un PDF
        if (!"application/pdf".equals(cvUploadDto.getTypeFichier())) {
            throw new RuntimeException("Seuls les fichiers PDF sont acceptés");
        }
        
        // Vérifier la taille du fichier (max 5MB)
        if (cvUploadDto.getCvBase64() != null) {
            int sizeInBytes = cvUploadDto.getCvBase64().length() * 3 / 4; // Approximation
            if (sizeInBytes > 5 * 1024 * 1024) { // 5MB
                throw new RuntimeException("Le fichier est trop volumineux (max 5MB)");
            }
        }
        
        // Récupérer ou créer le profil alternant
        AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseGet(() -> {
                AlternantProfile newProfile = new AlternantProfile();
                newProfile.setUtilisateur(utilisateur);
                return newProfile;
            });
        
        // Mettre à jour le CV
        profile.setCvBase64(cvUploadDto.getCvBase64());
        profile.setCvNomFichier(cvUploadDto.getNomFichier());
        profile.setCvDateUpload(LocalDate.now());
        
        AlternantProfile savedProfile = alternantProfileRepository.save(profile);
        
        return new CvResponseDto(
            savedProfile.getId(),
            savedProfile.getCvNomFichier(),
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
            
        if (profile == null || profile.getCvBase64() == null) {
            return new CvResponseDto(null, null, null, null, false);
        }
        
        return new CvResponseDto(
            profile.getId(),
            profile.getCvNomFichier(),
            "application/pdf",
            profile.getCvDateUpload(),
            true
        );
    }
    
    /**
     * Télécharger le CV d'un alternant
     */
    public String downloadCv(Long utilisateurId) {
        AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseThrow(() -> new RuntimeException("CV non trouvé"));
            
        if (profile.getCvBase64() == null) {
            throw new RuntimeException("Aucun CV uploadé");
        }
        
        return profile.getCvBase64();
    }
    
    /**
     * Supprimer le CV d'un alternant
     */
    public void deleteCv(Long utilisateurId) {
        AlternantProfile profile = alternantProfileRepository.findByUtilisateurId(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Profil alternant non trouvé"));
            
        profile.setCvBase64(null);
        profile.setCvNomFichier(null);
        profile.setCvDateUpload(null);
        
        alternantProfileRepository.save(profile);
    }
} 