package com.example.demo.security;

import com.example.demo.domain.UserRole;
import com.example.demo.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        System.out.println("🔐 Filtre JWT - URL: " + request.getRequestURI() + " - Auth header: " + (authHeader != null ? "présent" : "absent"));
        
        // Vérifier si le header Authorization existe et commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Pas de token Bearer trouvé");
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extraire le token (enlever "Bearer ")
        jwt = authHeader.substring(7);
        
        try {
            // Extraire l'email du token
            userEmail = jwtService.extractEmail(jwt);
            System.out.println("🔐 Email extrait du token: " + userEmail);
            
            // Si l'email existe et qu'aucune authentification n'est encore définie
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Vérifier que c'est bien un access token
                if (jwtService.isAccessToken(jwt) && !jwtService.isTokenExpired(jwt)) {
                    
                    // Extraire les informations utilisateur du token
                    Long userId = jwtService.extractUserId(jwt);
                    UserRole role = jwtService.extractRole(jwt);
                    
                    System.out.println("🔐 Token valide - userId: " + userId + ", role: " + role);
                    
                    // Créer les authorities basées sur le rôle
                    List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + role.name())
                    );
                    
                    // Créer l'objet d'authentification
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userEmail, null, authorities);
                    
                    // Ajouter les détails de la requête
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Ajouter l'ID utilisateur comme attribut de la requête pour un accès facile
                    request.setAttribute("userId", userId);
                    request.setAttribute("userRole", role);
                    
                    // Définir l'authentification dans le contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    System.out.println("✅ Authentification JWT réussie pour l'utilisateur: " + userEmail);
                } else {
                    System.out.println("❌ Token invalide ou expiré");
                }
            } else {
                System.out.println("❌ Email null ou authentification déjà définie");
            }
            
        } catch (JwtException e) {
            // En cas d'erreur JWT, on ne fait rien et on laisse passer la requête
            // Spring Security se chargera de rejeter les requêtes non authentifiées
            logger.debug("Erreur lors de la validation du token JWT: " + e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
} 