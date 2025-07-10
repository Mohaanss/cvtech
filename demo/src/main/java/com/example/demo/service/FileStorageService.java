package com.example.demo.service;

import com.example.demo.config.FileStorageConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    
    private final FileStorageConfig fileStorageConfig;
    private final Path fileStorageLocation;
    
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de créer le répertoire de stockage des fichiers", ex);
        }
    }
    
    /**
     * Sauvegarde un fichier sur le disque
     */
    public String storeFile(MultipartFile file) {
        // Vérifier la taille du fichier
        if (file.getSize() > fileStorageConfig.getMaxFileSize()) {
            throw new RuntimeException("Le fichier est trop volumineux. Taille max: " + fileStorageConfig.getMaxFileSize() + " bytes");
        }
        
        // Vérifier le type de fichier
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new RuntimeException("Seuls les fichiers PDF sont acceptés");
        }
        
        // Générer un nom de fichier unique
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        if (originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        String fileName = UUID.randomUUID().toString() + fileExtension;
        
        try {
            // Copier le fichier vers la destination
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de stocker le fichier " + fileName, ex);
        }
    }
    
    /**
     * Récupère un fichier depuis le disque
     */
    public byte[] loadFileAsBytes(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de lire le fichier " + fileName, ex);
        }
    }
    
    /**
     * Supprime un fichier du disque
     */
    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de supprimer le fichier " + fileName, ex);
        }
    }
    
    /**
     * Vérifie si un fichier existe
     */
    public boolean fileExists(String fileName) {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        return Files.exists(filePath);
    }
    
    /**
     * Obtient le chemin complet du fichier
     */
    public String getFilePath(String fileName) {
        return this.fileStorageLocation.resolve(fileName).toString();
    }
} 