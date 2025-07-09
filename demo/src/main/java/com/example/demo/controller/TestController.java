package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    /**
     * Endpoint de test pour vérifier le rate limiting
     */
    @GetMapping("/rate-limit")
    public ResponseEntity<Map<String, Object>> testRateLimit() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rate limit test successful");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "OK");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de test pour les endpoints sensibles
     */
    @PostMapping("/sensitive")
    public ResponseEntity<Map<String, Object>> testSensitiveEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sensitive endpoint test successful");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "OK");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de test pour vérifier les headers de rate limiting
     */
    @GetMapping("/headers")
    public ResponseEntity<Map<String, Object>> testHeaders() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Headers test");
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok()
            .header("X-RateLimit-Test", "true")
            .body(response);
    }
} 