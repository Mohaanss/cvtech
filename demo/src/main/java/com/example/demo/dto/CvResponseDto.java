package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvResponseDto {
    private Long id;
    private String nomFichier;
    private String typeFichier;
    private LocalDate dateUpload;
    private boolean hasCv;
} 