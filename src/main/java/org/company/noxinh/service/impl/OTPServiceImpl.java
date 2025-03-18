package org.company.noxinh.service.impl;

import org.company.noxinh.dto.authen.request.OTPRedisCache;
import org.company.noxinh.entity.Account;
import org.company.noxinh.service.OTPService;

/*
 * The type Otp service.
 */
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The type Otp service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OTPServiceImpl implements OTPService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_RESET_REQUESTS = 4;
    private static final long RESET_WINDOW_MINUTES = 10;

    private static final String COUNT_RESET = "resetpw_req_count_";

    @Override
    public void saveAccountRedis(UUID id, String otp, Account account, LocalDateTime otpGenerate) {
        OTPRedisCache redisCache = new OTPRedisCache(id, otp, account, otpGenerate, 10);
        redisTemplate.opsForValue().set(id + "_otp", redisCache, 10, TimeUnit.MINUTES);
    }

    public void incrementResetRequestCount(String email) {
        String key = COUNT_RESET + email;
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        if (count == null) {
            count = 0;
        }
        redisTemplate.opsForValue().set(key, count + 1, RESET_WINDOW_MINUTES, TimeUnit.MINUTES);
    }

    public boolean isRateLimitExceeded(String email) {
        String key = COUNT_RESET + email;
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        return count != null && count >= MAX_RESET_REQUESTS;
    }

    @Override
    public OTPRedisCache getAccountRedis(String uuid) {
        log.info("Retrieving OTP: {}", uuid);
        OTPRedisCache account = (OTPRedisCache) redisTemplate.opsForValue().get(uuid + "_otp");
        if (account == null) {
            log.warn("No account found: {}", uuid);
            return null;
        }
        log.info("Retrieved account: {}", account);
        return account;
    }

    @Override
    public void deleteAccountRedis(String uuid) {
        redisTemplate.delete(uuid + "_otp");
    }

    @Override
    public void deleteResetRequestCount(String email) {
        redisTemplate.delete(COUNT_RESET + email);
    }
}
