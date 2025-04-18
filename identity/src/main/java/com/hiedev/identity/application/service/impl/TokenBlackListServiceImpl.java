package com.hiedev.identity.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlackListServiceImpl {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "bl:jti:";

    public void blackListToken(String jti, Instant expiration) {
        long ttl = expiration.getEpochSecond() - Instant.now().getEpochSecond();
        if (ttl <= 0) {
            log.warn("Token is already expired, cannot be blacklisted");
            return;
        }
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + jti, "blacklisted", ttl, TimeUnit.SECONDS);
        log.info("Token with jti {} has been blacklisted for {} seconds", jti, ttl);
    }

    public boolean isTokenBlackListed(String jti) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
        } catch (Exception e) {
            log.error("Failed to check blacklist in Redis, jti: {}, error: {}", jti, e.getMessage());
            return false;
        }
    }
}
