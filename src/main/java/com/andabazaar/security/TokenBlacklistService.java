package com.andabazaar.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklist(String token, long expirationTimeMs) {
        blacklistedTokens.put(token, expirationTimeMs);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}
