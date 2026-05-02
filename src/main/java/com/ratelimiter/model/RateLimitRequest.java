package com.ratelimiter.model;

import lombok.Data;

@Data  // Lombok: auto-generates getters, setters, toString
public class RateLimitRequest {
    private String clientId;    // e.g. "user_123" or "api_key_abc"
    private String algorithm;   // "TOKEN_BUCKET", "SLIDING_WINDOW", "FIXED_WINDOW"
    private int maxRequests;    // e.g. 100
    private int windowSeconds;  // e.g. 60 (per minute)
}