package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Désactive CSRF pour les API REST
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/utilisateurs/login").permitAll() // Permet l'accès au login
                .requestMatchers("/api/**").permitAll() // Permet l'accès libre aux routes API
                .anyRequest().authenticated() // Autres routes nécessitent une authentification
            );
        
        return http.build();
    }
}
