package com.mycompany.sportshopweb.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

public class CloudinaryConfig {

    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            Dotenv dotenv = null;
            try {
                dotenv = Dotenv.configure()
                        .ignoreIfMissing()
                        .load();
            } catch (Exception ignored) {
            }

            String cloudName = getVal(dotenv, "CLOUDINARY_CLOUD_NAME");
            String apiKey = getVal(dotenv, "CLOUDINARY_API_KEY");
            String apiSecret = getVal(dotenv, "CLOUDINARY_API_SECRET");

            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true));
        }
        return cloudinary;
    }

    private static String getVal(Dotenv dotenv, String key) {
        String val = null;
        if (dotenv != null) {
            try {
                val = dotenv.get(key);
            } catch (Exception ignored) {
            }
        }
        if (val == null || val.isBlank()) {
            val = System.getenv(key);
        }
        return val == null ? "" : val.trim();
    }
}
