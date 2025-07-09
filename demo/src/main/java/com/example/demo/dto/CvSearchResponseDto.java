package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvSearchResponseDto {
    private List<CvPublicDto> cvs;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
} 