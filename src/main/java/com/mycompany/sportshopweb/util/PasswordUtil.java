/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 *
 * @author Anh Tuan
 */
public class PasswordUtil {

    // Băm mật khẩu sang chuỗi mã hóa SHA-256
    public static String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Chuyển mảng byte sang chuỗi Hex
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Kiểm tra mật khẩu nhập vào có khớp với mật khẩu đã lưu không
    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) {
            return false;
        }
        // Trường hợp mật khẩu trong DB đã được băm SHA-256
        String inputHashed = hashPassword(inputPassword);
        if (inputHashed.equalsIgnoreCase(storedPassword)) {
            return true;
        }
        // Mật khẩu không trùng khớp -> Từ chối xác thực
        return false;
    }

    // Kiểm tra độ mạnh mật khẩu theo chuẩn Chapter 17 (Slide 18 & 19)
    public static String checkPasswordStrength(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty.";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        // Bắt buộc chứa ít nhất một chữ hoa
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter (A-Z).";
        }
        // Bắt buộc chứa ít nhất một chữ số
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number (0-9).";
        }
        // Bắt buộc chứa ít nhất một ký tự đặc biệt
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>_].*")) {
            return "Password must contain at least one special character (!@#$%^&*...).";
        }
        return null;
    }
}
