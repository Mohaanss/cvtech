package com.example.demo.security;

import com.example.demo.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    
    private final RateLimitService rateLimitService;
    
    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String clientIp = getClientIpAddress(request);
        String requestUri = request.getRequestURI();
        
        // Appliquer le rate limiting selon le type d'endpoint
        boolean allowed = true;
        
        if (isSensitiveEndpoint(requestUri)) {
            // Endpoints sensibles (login, upload, etc.)
            allowed = rateLimitService.tryConsumeSensitive(clientIp);
            if (!allowed) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Rate limit exceeded for sensitive endpoints\",\"message\":\"Too many requests. Please try again later.\"}");
                return;
            }
        } else {
            // Rate limiting global
            allowed = rateLimitService.tryConsume(clientIp);
            if (!allowed) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Rate limit exceeded\",\"message\":\"Too many requests. Please try again later.\"}");
                return;
            }
        }
        
        // Ajouter les headers de rate limiting
        response.setHeader("X-RateLimit-Remaining", String.valueOf(rateLimitService.getWaitTime(clientIp)));
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Déterminer si l'endpoint est sensible
     */
    private boolean isSensitiveEndpoint(String uri) {
        return uri.contains("/login") || 
               uri.contains("/upload") || 
               uri.contains("/register") ||
               uri.contains("/refresh-token") ||
               uri.contains("/cv/upload");
    }
    
    /**
     * Obtenir l'adresse IP du client
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 