package com.example.demo.controller;

import com.example.demo.dto.CvUploadDto;
import com.example.demo.dto.CvResponseDto;
import com.example.demo.service.AlternantProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cv")
@CrossOrigin(origins = "http://localhost:4200")
public class CvController {
    
    private final AlternantProfileService alternantProfileService;
    
    public CvController(AlternantProfileService alternantProfileService) {
        this.alternantProfileService = alternantProfileService;
    }
    
    /**
     * Upload du CV d'un alternant
     */
    @PostMapping("/upload")
    public ResponseEntity<CvResponseDto> uploadCv(@RequestBody CvUploadDto cvUploadDto, 
                                                 HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            CvResponseDto response = alternantProfileService.uploadCv(userId, cvUploadDto);
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
            
            String cvBase64 = alternantProfileService.downloadCv(userId);
            byte[] cvBytes = java.util.Base64.getDecoder().decode(cvBase64);
            
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