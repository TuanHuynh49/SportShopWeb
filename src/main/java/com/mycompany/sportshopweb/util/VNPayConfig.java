
package com.mycompany.sportshopweb.util;

public final class VNPayConfig {

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
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing environment variable: " + name);
        }

        return value;
    }
}
