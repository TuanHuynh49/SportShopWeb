
package com.mycompany.sportshopweb.util;

import io.github.cdimascio.dotenv.Dotenv;

public final class VNPayConfig {

    private static Dotenv dotenv;

    static {
        try {
            dotenv = Dotenv.configure().ignoreIfMissing().load();
        } catch (Exception ignored) {
        }
    }

    private VNPayConfig() {
    }

    public static final String PAYMENT_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public static String getTmnCode() {
        return getRequiredEnv("VNPAY_TMN_CODE");
    }

    public static String getHashSecret() {
        return getRequiredEnv("VNPAY_HASH_SECRET");
    }

    public static String getReturnUrl() {
        return getRequiredEnv("VNPAY_RETURN_URL");
    }

    private static String getRequiredEnv(String name) {
        String value = null;
        if (dotenv != null) {
            try {
                value = dotenv.get(name);
            } catch (Exception ignored) {
            }
        }
        if (value == null || value.isBlank()) {
            value = System.getenv(name);
        }

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing environment variable: " + name);
        }

        return value.trim();
    }
}
