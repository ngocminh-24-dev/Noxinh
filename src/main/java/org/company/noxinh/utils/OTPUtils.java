package org.company.noxinh.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * The type Otp utils.
 */
@Component
public class OTPUtils {
    /**
     * Generate otp string.
     *
     * @return the string
     */
    private final Random random = new Random();

    /**
     * Generate otp string.
     *
     * @return the string
     */
    public String generateOTP() {
        int min = 100000;
        int max = 999999;
        int randomNumber = this.random.nextInt(max - min + 1) + min;
        return Integer.toString(randomNumber);
    }
}
