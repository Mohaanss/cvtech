package com.example.demo.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bucket> sensitiveBuckets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bucket> cvUploadBuckets = new ConcurrentHashMap<>();

    /**
     * Vérifier et consommer un token pour l'IP donnée (rate limiter global)
     * - 100 requêtes par minute
     * - 1000 requêtes par heure
     */
    public boolean tryConsume(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, key -> {
            return Bucket.builder()
                .addLimit(Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1))))
                .addLimit(Bandwidth.classic(1000, Refill.intervally(1000, Duration.ofHours(1))))
                .build();
        });
        return bucket.tryConsume(1);
    }

    /**
     * Vérifier et consommer un token pour l'IP donnée (endpoints sensibles)
     * - 10 requêtes par minute
     * - 100 requêtes par heure
     */
    public boolean tryConsumeSensitive(String ipAddress) {
        Bucket bucket = sensitiveBuckets.computeIfAbsent(ipAddress, key -> {
            return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .addLimit(Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1))))
                .build();
        });
        return bucket.tryConsume(1);
    }

    /**
     * Vérifier et consommer un token pour l'utilisateur donné (upload CV)
     * - 5 uploads par heure
     */
    public boolean tryConsumeCvUpload(String userId) {
        Bucket bucket = cvUploadBuckets.computeIfAbsent(userId, key -> {
            return Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofHours(1))))
                .build();
        });
        return bucket.tryConsume(1);
    }

    /**
     * Obtenir le temps d'attente avant la prochaine tentative
     */
    public long getWaitTime(String ipAddress) {
        Bucket bucket = buckets.get(ipAddress);
        if (bucket != null) {
            return bucket.getAvailableTokens();
        }
        return 0;
    }

    /**
     * Obtenir le temps d'attente pour les endpoints sensibles
     */
    public long getWaitTimeSensitive(String ipAddress) {
        Bucket bucket = sensitiveBuckets.get(ipAddress);
        if (bucket != null) {
            return bucket.getAvailableTokens();
        }
        return 0;
    }

    /**
     * Obtenir le temps d'attente pour l'upload de CV
     */
    public long getWaitTimeCvUpload(String userId) {
        Bucket bucket = cvUploadBuckets.get(userId);
        if (bucket != null) {
            return bucket.getAvailableTokens();
        }
        return 0;
    }
} 