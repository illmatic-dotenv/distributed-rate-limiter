package com.ratelimiter.service;

import com.ratelimiter.model.RateLimitResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FixedWindowLimiter {

    private final RedisTemplate<String, String> redisTemplate;
    private final DefaultRedisScript<Long> script;

    public FixedWindowLimiter(RedisTemplate<String, String> redisTemplate,
                               @Qualifier("fixedWindowScript") DefaultRedisScript<Long> script) {
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

    public RateLimitResponse check(String clientId, int maxRequests, int windowSeconds) {
        String key = "rl:" + clientId + ":fixed";

        Long remaining = redisTemplate.execute(script,
            List.of(key),
            String.valueOf(maxRequests),
            String.valueOf(windowSeconds));

        if (remaining == null || remaining == -1) {
            return new RateLimitResponse(false, 0, windowSeconds, "Rate limit exceeded");
        }
        return new RateLimitResponse(true, remaining, windowSeconds, "Request allowed");
    }
}