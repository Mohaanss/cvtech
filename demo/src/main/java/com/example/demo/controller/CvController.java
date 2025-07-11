package com.example.demo.controller;

import com.example.demo.dto.CvResponseDto;
import com.example.demo.service.AlternantProfileService;
import com.example.demo.service.RateLimitService;
import com.example.demo.domain.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/cv")
@CrossOrigin(origins = "http://localhost:4200")
public class CvController {
    
    private final AlternantProfileService alternantProfileService;
    private final RateLimitService rateLimitService;
    
    public CvController(AlternantProfileService alternantProfileService, RateLimitService rateLimitService) {
        this.alternantProfileService = alternantProfileService;
        this.rateLimitService = rateLimitService;
    }
    
    /**
     * Upload du CV d'un alternant
     */
    @PostMapping("/upload")
    public ResponseEntity<CvResponseDto> uploadCv(@RequestParam("file") MultipartFile file, 
                                                 HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Rate limiting spécifique pour l'upload de CV
            if (!rateLimitService.tryConsumeCvUpload(userId.toString())) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new CvResponseDto(null, null, null, null, false));
            }
            
            CvResponseDto response = alternantProfileService.uploadCv(userId, file);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Récupérer les informations du CV d'un alternant
     */
    @GetMapping("/info")
    public ResponseEntity<CvResponseDto> getCvInfo(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            CvResponseDto response = alternantProfileService.getCvInfo(userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Télécharger le CV d'un alternant
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadCv(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            byte[] cvBytes = alternantProfileService.downloadCv(userId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "cv.pdf");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(cvBytes);
                
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Visualiser le CV d'un alternant (pour iframe)
     * Accessible par l'alternant lui-même ou par les recruteurs
     */
    @GetMapping("/view/{alternantId}")
    public ResponseEntity<byte[]> viewCv(@PathVariable Long alternantId, HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            UserRole userRole = (UserRole) request.getAttribute("userRole");
            
            if (userId == null || userRole == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Vérifier les autorisations :
            // - L'alternant peut voir son propre CV
            // - Les recruteurs peuvent voir tous les CVs
            if (!userId.equals(alternantId) && userRole != UserRole.RECRUTEUR) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            byte[] cvBytes = alternantProfileService.downloadCv(alternantId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set("Content-Disposition", "inline; filename=cv.pdf");
            headers.set("X-Frame-Options", "SAMEORIGIN"); // Permettre l'affichage en iframe
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(cvBytes);
                
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Supprimer le CV d'un alternant
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCv(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            alternantProfileService.deleteCv(userId);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 