package com.example.student_management.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Service to manage token blacklist
 * Uses Redis if available, falls back to In-Memory storage
 */
@Service
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final boolean redisAvailable;

    // Fallback: In-memory blacklist storage
    private final Map<String, Long> inMemoryBlacklist = new ConcurrentHashMap<>();

    private static final String BLACKLIST_PREFIX = "blacklist:";

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate,
                                  JwtTokenProvider jwtTokenProvider) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisAvailable = checkRedisConnection();

        if (redisAvailable) {
            log.info("✅ Using Redis for token blacklist");
        } else {
            log.warn("⚠️ Redis not available, using In-Memory storage for token blacklist");
        }
    }

    private boolean checkRedisConnection() {
        if (redisTemplate == null) {
            return false;
        }
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Add token to blacklist until it expires
     */
    public void blacklistToken(String token) {
        // Calculate remaining time until token expires
        long remainingTimeMs = jwtTokenProvider.getRemainingExpiration(token);

        if (remainingTimeMs <= 0) {
            log.info("Token already expired, no need to blacklist");
            return;
        }

        if (redisAvailable) {
            try {
                String key = BLACKLIST_PREFIX + token;
                redisTemplate.opsForValue().set(key, "blacklisted", remainingTimeMs, TimeUnit.MILLISECONDS);
                log.info("Token blacklisted in Redis (expires in {} seconds)", remainingTimeMs / 1000);
                return;
            } catch (Exception e) {
                log.warn("Redis error, falling back to In-Memory: {}", e.getMessage());
            }
        }

        // Fallback to In-Memory
        long expirationTime = System.currentTimeMillis() + remainingTimeMs;
        inMemoryBlacklist.put(token, expirationTime);
        log.info("Token blacklisted in Memory (expires in {} seconds)", remainingTimeMs / 1000);
        cleanupExpiredTokens();
    }

    /**
     * Check if token is blacklisted
     */
    public boolean isBlacklisted(String token) {
        if (redisAvailable) {
            try {
                String key = BLACKLIST_PREFIX + token;
                return Boolean.TRUE.equals(redisTemplate.hasKey(key));
            } catch (Exception e) {
                log.warn("Redis error, falling back to In-Memory: {}", e.getMessage());
            }
        }

        // Fallback to In-Memory
        Long expirationTime = inMemoryBlacklist.get(token);
        if (expirationTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > expirationTime) {
            inMemoryBlacklist.remove(token);
            return false;
        }

        return true;
    }

    private void cleanupExpiredTokens() {
        long currentTime = System.currentTimeMillis();
        inMemoryBlacklist.entrySet().removeIf(entry -> currentTime > entry.getValue());
    }
}
