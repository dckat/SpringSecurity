package com.meerkat.ss.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class GenerateCodeUtil {

    private GenerateCodeUtil() {}

    public static String generateCode() {
        String code;

        try {
            SecureRandom random = SecureRandom.getInstanceStrong();

            int c = random.nextInt(9000) + 1000;    // 1000 - 9999 사이의 값

            code = String.valueOf(c);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Otp 생성 중 문제 발생");
        }
        return code;
    }
}
