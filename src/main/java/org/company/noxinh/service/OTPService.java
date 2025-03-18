package org.company.noxinh.service;

import org.company.noxinh.dto.authen.request.OTPRedisCache;
import org.company.noxinh.entity.Account;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The interface Otp service.
 */
public interface OTPService {
    /**
     * Save account reset password.
     *
     * @param id          the id
     * @param otp         the otp
     * @param account     the account
     * @param otpGenerate the otp generate
     */
    void saveAccountRedis(UUID id, String otp, Account account, LocalDateTime otpGenerate);

    /**
     * Increment reset request count.
     *
     * @param email the email
     */
    void incrementResetRequestCount(String email);

    /**
     * Is rate limit exceeded boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean isRateLimitExceeded(String email);

    /**
     * Gets otp.
     *
     * @param uuid the uuid
     * @return the otp
     */
    OTPRedisCache getAccountRedis(String uuid);

    /**
     * Delete otp.
     *
     * @param uuid the uuid
     */
    void deleteAccountRedis(String uuid);

    /**
     * Delete reset request count.
     *
     * @param email the email
     */
    void deleteResetRequestCount(String email);
}
