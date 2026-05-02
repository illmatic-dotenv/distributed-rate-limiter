package com.ratelimiter.service;

import com.ratelimiter.model.RateLimitRequest;
import com.ratelimiter.model.RateLimitResponse;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final FixedWindowLimiter fixedWindow;
    private final TokenBucketLimiter tokenBucket;
    private final SlidingWindowLimiter slidingWindow;

    public RateLimiterService(FixedWindowLimiter fixedWindow,
                               TokenBucketLimiter tokenBucket,
                               SlidingWindowLimiter slidingWindow) {
        this.fixedWindow = fixedWindow;
        this.tokenBucket = tokenBucket;
        this.slidingWindow = slidingWindow;
    }

    public RateLimitResponse check(RateLimitRequest request) {
        return switch (request.getAlgorithm().toUpperCase()) {
            case "TOKEN_BUCKET"    -> tokenBucket.check(
                request.getClientId(), request.getMaxRequests(), request.getWindowSeconds());
            case "SLIDING_WINDOW"  -> slidingWindow.check(
                request.getClientId(), request.getMaxRequests(), request.getWindowSeconds());
            case "FIXED_WINDOW"    -> fixedWindow.check(
                request.getClientId(), request.getMaxRequests(), request.getWindowSeconds());
            default -> throw new IllegalArgumentException(
                "Unknown algorithm: " + request.getAlgorithm());
        };
    }
}