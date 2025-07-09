package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Active CORS
            .csrf(csrf -> csrf.disable()) // Désactive CSRF pour les API REST
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Pas de session
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/utilisateurs/login").permitAll() // Permet l'accès au login
                .requestMatchers("/api/utilisateurs/refresh-token").permitAll() // Permet l'accès au refresh
                .requestMatchers("/api/utilisateurs/alternant").permitAll() // Permet l'inscription alternant
                .requestMatchers("/api/utilisateurs/ecole").permitAll() // Permet l'inscription école
                .requestMatchers("/api/utilisateurs/recruteur").permitAll() // Permet l'inscription recruteur
                .anyRequest().authenticated() // Autres routes nécessitent une authentification
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Ajoute le filtre JWT
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permettre les requêtes depuis le frontend Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        // Permettre toutes les méthodes HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Permettre tous les headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permettre l'envoi des credentials
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}
