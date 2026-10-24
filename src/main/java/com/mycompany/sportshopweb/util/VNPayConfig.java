
package com.mycompany.sportshopweb.util;

public final class VNPayConfig {

    private VNPayConfig() {
    }

    public static final String PAYMENT_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

        public static String getTmnCode() {
        return "IPVWELXW";
    }

    public static String getHashSecret() {
        return "SRNZKXGZXESCMEKWZHETRSCSBISGOPFK";
    }

    public static String getReturnUrl() {
        return "http://localhost:8082/SportShopWeb/vnpay-return";
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
