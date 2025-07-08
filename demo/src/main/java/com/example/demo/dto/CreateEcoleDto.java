package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEcoleDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String motDePasse;

    @NotBlank
    private String nomEcole;

    private String adresse;
    private String siteWeb;
}
