
package com.mycompany.sportshopweb.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class EmailService {

    private static final String API_URL =
            "https://api.brevo.com/v3/smtp/email";

    private static final HttpClient HTTP_CLIENT =
            HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    .build();

    private static String getEnv(String name) {
        String value = System.getenv(name);
        return value == null ? "" : value.trim();
    }

    // Escape chuỗi để đưa vào JSON an toàn
    private static String jsonEscape(String value) {
        if (value == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (char c : value.toCharArray()) {
            switch (c) {
                case '"' -> result.append("\\\"");
                case '\\' -> result.append("\\\\");
                case '\b' -> result.append("\\b");
                case '\f' -> result.append("\\f");
                case '\n' -> result.append("\\n");
                case '\r' -> result.append("\\r");
                case '\t' -> result.append("\\t");
                default -> {
                    if (c < 0x20) {
                        result.append(String.format("\\u%04x", (int) c));
                    } else {
                        result.append(c);
                    }
                }
            }
        }

        return result.toString();
    }

    public static void sendEmailSync(
            String to,
            String subject,
            String body
    ) throws Exception {

        String apiKey = getEnv("BREVO_API_KEY");
        String senderEmail = getEnv("BREVO_SENDER_EMAIL");
        String senderName = getEnv("BREVO_SENDER_NAME");

        if (apiKey.isEmpty()
                || senderEmail.isEmpty()) {
            throw new IllegalStateException(
                    "Missing Brevo API environment variables"
            );
        }

        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipient email must not be empty"
            );
        }

        String safeSenderName =
                senderName.isEmpty() ? "SportShop" : senderName;

        String json = """
                {
                  "sender": {
                    "name": "%s",
                    "email": "%s"
                  },
                  "to": [
                    {
                      "email": "%s"
                    }
                  ],
                  "subject": "%s",
                  "htmlContent": "%s"
                }
                """.formatted(
                    jsonEscape(safeSenderName),
                    jsonEscape(senderEmail),
                    jsonEscape(to),
                    jsonEscape(subject),
                    jsonEscape(body)
                );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(30))
                .header("accept", "application/json")
                .header("api-key", apiKey)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8)
        );

        int status = response.statusCode();

        if (status < 200 || status >= 300) {
            throw new IOException(
                    "Brevo API error HTTP " + status
                    + ": " + response.body()
            );
        }

        System.out.println(
                "[EmailService] Brevo API gửi email thành công tới: "
                + to + " | HTTP " + status
        );
    }

    public static void sendEmailAsync(
            String to,
            String subject,
            String body
    ) {
        Thread thread = new Thread(() -> {
            try {
                sendEmailSync(to, subject, body);
            } catch (Exception e) {
                System.err.println(
                        "[EmailService] Không gửi được email tới "
                        + to + ": " + e.getMessage()
                );
                e.printStackTrace();
            }
        });

        thread.setName("sportshop-email-sender");
        thread.setDaemon(false);
        thread.start();
    }

    public static void sendOtpEmail(
            String to,
            String otp,
            String purpose
    ) {
        String subject = "SportShop - Ma xac thuc OTP";

        String body = """
                <html>
                <body>
                    <h2>SportShop - Xac thuc tai khoan</h2>
                    <p>Ma OTP cua ban la:</p>
                    <h1 style="color:blue;">%s</h1>
                    <p>Muc dich: %s</p>
                    <p>Vui long khong chia se ma OTP nay voi nguoi khac.</p>
                </body>
                </html>
                """.formatted(
                    jsonEscape(otp),
                    jsonEscape(purpose)
                );

        sendEmailAsync(to, subject, body);
    }

    public static void sendWelcomeEmail(
            String to,
            String name
    ) {
        String subject = "Chao mung ban den voi SportShop!";

        String safeName = name == null || name.isBlank()
                ? "ban"
                : name;

        String body = """
                <html>
                <body>
                    <h2>Chao mung den voi SportShop!</h2>
                    <p>Xin chao %s,</p>
                    <p>Tai khoan cua ban da duoc xac minh thanh cong.</p>
                    <p>Cam on ban da tham gia SportShop!</p>
                </body>
                </html>
                """.formatted(escapeHtml(safeName));

        sendEmailAsync(to, subject, body);
    }

    private static String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
