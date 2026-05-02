package com.ratelimiter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateLimitResponse {
    private boolean allowed;
    private long remainingRequests;
    private long resetAfterSeconds;
    private String message;
}