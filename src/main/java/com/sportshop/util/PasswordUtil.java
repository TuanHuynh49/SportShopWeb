package com.sportshop.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * PasswordUtil — Tiện ích băm và xác thực mật khẩu an toàn với thuật toán SHA-256 + Salt.
 */
public final class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_BYTES = 16;

    private PasswordUtil() {
        // Utility class
    }

    /**
     * Sinh chuỗi salt ngẫu nhiên bảo mật (Base64)
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Băm mật khẩu thô kèm salt theo định dạng: salt$hash
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống.");
        }
        String salt = generateSalt();
        String hash = hashWithSalt(rawPassword, salt);
        return salt + "$" + hash;
    }

    /**
     * Băm mật khẩu với salt cho trước
     */
    public static String hashWithSalt(String rawPassword, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashedBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Thuật toán mã hóa " + ALGORITHM + " không khả dụng.", e);
        }
    }

    /**
     * Kiểm tra mật khẩu thô người dùng nhập so với chuỗi hash đã lưu (salt$hash)
     */
    public static boolean verifyPassword(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        String[] parts = storedHash.split("\\$");
        if (parts.length != 2) {
            return false;
        }
        String salt = parts[0];
        String expectedHash = parts[1];
        String calculatedHash = hashWithSalt(rawPassword, salt);
        return expectedHash.equals(calculatedHash);
    }
}
