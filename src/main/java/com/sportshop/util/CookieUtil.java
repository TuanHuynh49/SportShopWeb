package com.sportshop.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CookieUtil — Tiện ích đọc, ghi và xóa HTTP Cookie an toàn (Remember Me, Session).
 */
public final class CookieUtil {

    private CookieUtil() {
        // Utility class
    }

    /**
     * Tạo hoặc cập nhật Cookie
     *
     * @param response HttpServletResponse
     * @param name     Tên cookie
     * @param value    Giá trị cookie
     * @param maxAgeSeconds Thời gian sống (giây). Nếu <= 0 cookie sẽ bị xoá
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAgeSeconds);
        response.addCookie(cookie);
    }

    /**
     * Lấy giá trị cookie theo tên
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request == null || name == null) return null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Xóa cookie
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        setCookie(response, name, "", 0);
    }
}
