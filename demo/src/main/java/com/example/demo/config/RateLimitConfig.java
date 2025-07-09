package com.example.demo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    /**
     * Configuration du rate limiter global
     * - 100 requêtes par minute par IP
     * - 1000 requêtes par heure par IP
     */
    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1))))
            .addLimit(Bandwidth.classic(1000, Refill.intervally(1000, Duration.ofHours(1))))
            .build();
    }

    /**
     * Configuration du rate limiter pour les endpoints sensibles (login, upload)
     * - 10 requêtes par minute par IP
     * - 100 requêtes par heure par IP
     */
    @Bean
    public BucketConfiguration sensitiveEndpointsBucketConfiguration() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
            .addLimit(Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1))))
            .build();
    }

    /**
     * Configuration du rate limiter pour les endpoints d'upload de CV
     * - 5 uploads par heure par utilisateur
     */
    @Bean
    public BucketConfiguration cvUploadBucketConfiguration() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1))))
            .build();
    }
} 