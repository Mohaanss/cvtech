package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvUploadDto {
    private String cvBase64;
    private String nomFichier;
    private String typeFichier;
} 