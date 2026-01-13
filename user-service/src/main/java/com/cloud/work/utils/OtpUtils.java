package com.cloud.work.utils;

import java.security.SecureRandom;

public class OtpUtils {

    public static String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        int number = secureRandom.nextInt(10000);
        return String.format("%04d", number);
    }
}
