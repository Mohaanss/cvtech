package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvFileUploadDto {
    
    private String originalFileName;
    private String storedFileName;
    private long fileSize;
    private String contentType;
} 