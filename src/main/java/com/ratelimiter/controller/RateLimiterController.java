package com.ratelimiter.controller;

import com.ratelimiter.model.RateLimitRequest;
import com.ratelimiter.model.RateLimitResponse;
import com.ratelimiter.service.RateLimiterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RateLimiterController {

    private final RateLimiterService service;

    public RateLimiterController(RateLimiterService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<RateLimitResponse> check(@RequestBody RateLimitRequest request) {
        RateLimitResponse response = service.check(request);
        
        // Return 429 (Too Many Requests) if blocked — this is the real HTTP status for rate limiting
        HttpStatus status = response.isAllowed() ? HttpStatus.OK : HttpStatus.TOO_MANY_REQUESTS;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/health")
    public String health() {
        return "Rate Limiter Service is running";
    }
}