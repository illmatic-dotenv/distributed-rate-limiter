package com.ratelimiter.service;

import com.ratelimiter.model.RateLimitResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TokenBucketLimiter {

    private final RedisTemplate<String, String> redisTemplate;
    private final DefaultRedisScript<Long> script;

    public TokenBucketLimiter(RedisTemplate<String, String> redisTemplate,
                               @Qualifier("tokenBucketScript") DefaultRedisScript<Long> script) {
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

    public RateLimitResponse check(String clientId, int maxRequests, int windowSeconds) {
        String key = "rl:" + clientId + ":token_bucket";
        // refillRate = tokens per second
        double refillRate = (double) maxRequests / windowSeconds;
        long now = System.currentTimeMillis();

        Long remaining = redisTemplate.execute(script,
            List.of(key),
            String.valueOf(maxRequests),
            String.valueOf(refillRate),
            String.valueOf(now));

        if (remaining == null || remaining == -1) {
            return new RateLimitResponse(false, 0, windowSeconds, "Rate limit exceeded");
        }
        return new RateLimitResponse(true, remaining, windowSeconds, "Request allowed");
    }
}