package com.example.demo.exception;

public class RateLimitExceededException extends RuntimeException {
    
    private final long waitTimeSeconds;
    
    public RateLimitExceededException(String message, long waitTimeSeconds) {
        super(message);
        this.waitTimeSeconds = waitTimeSeconds;
    }
    
    public RateLimitExceededException(String message) {
        super(message);
        this.waitTimeSeconds = 0;
    }
    
    public long getWaitTimeSeconds() {
        return waitTimeSeconds;
    }
} 