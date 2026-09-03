package com.sportshop.dao;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBContext — Quản lý kết nối JDBC linh hoạt đa môi trường (Local, Render, Railway, Aiven, Clever Cloud).
 * Tự động phân tích chuỗi DATABASE_URL / MYSQL_URL hoặc các biến môi trường DB_HOST, DB_USER, DB_PASSWORD.
 */
public class DBContext {

    private static final Logger LOGGER = Logger.getLogger(DBContext.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    static {
        // 1. Tải JDBC Driver
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Không tìm thấy MySQL JDBC Driver: " + DB_DRIVER, e);
        }

        // 2. Khởi tạo cấu hình kết nối từ biến môi trường
        initDatabaseConfig();
    }

    private static void initDatabaseConfig() {
        // Kiểm tra chuỗi kết nối tổng hợp (phổ biến trên Render, Railway, Clever Cloud, Heroku)
        String envUrl = getFirstEnv("DATABASE_URL", "MYSQL_URL", "MYSQL_PUBLIC_URL", "DB_URL");

        if (envUrl != null && !envUrl.trim().isEmpty()) {
            envUrl = envUrl.trim();
            try {
                // Xử lý URI dạng mysql://user:password@host:port/database
                if (envUrl.startsWith("mysql://")) {
                    URI uri = new URI(envUrl);
                    String host = uri.getHost();
                    int port = uri.getPort() == -1 ? 3306 : uri.getPort();
                    String path = uri.getPath(); // /dbname
                    String dbName = (path != null && path.length() > 1) ? path.substring(1) : "sportshop_db";

                    String userInfo = uri.getUserInfo();
                    if (userInfo != null && userInfo.contains(":")) {
                        String[] parts = userInfo.split(":", 2);
                        dbUser = parts[0];
                        dbPassword = parts[1];
                    }

                    dbUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
                            host, port, dbName);
                } else if (envUrl.startsWith("jdbc:mysql://")) {
                    dbUrl = envUrl;
                    if (!dbUrl.contains("allowPublicKeyRetrieval=")) {
                        String sep = dbUrl.contains("?") ? "&" : "?";
                        dbUrl += sep + "allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
                    }
                    dbUser = getFirstEnv("DB_USER", "MYSQLUSER", "MYSQL_USER", "DB_USERNAME");
                    dbPassword = getFirstEnv("DB_PASSWORD", "MYSQLPASSWORD", "MYSQL_PASSWORD", "DB_PASS");
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Lỗi phân tích cú pháp biến môi trường URL: " + e.getMessage(), e);
            }
        }

        // Nếu chưa có URL hoặc dùng các biến lẻ (DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD)
        if (dbUrl == null || dbUrl.isEmpty()) {
            String host = getFirstEnv("DB_HOST", "MYSQLHOST", "MYSQL_HOST");
            String port = getFirstEnv("DB_PORT", "MYSQLPORT", "MYSQL_PORT");
            String dbName = getFirstEnv("DB_NAME", "MYSQLDATABASE", "MYSQL_DATABASE");

            if (host == null || host.isEmpty()) host = "localhost";
            if (port == null || port.isEmpty()) port = "3306";
            if (dbName == null || dbName.isEmpty()) dbName = "sportshop_db";

            dbUrl = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
                    host, port, dbName);

            if (dbUser == null) {
                dbUser = getFirstEnv("DB_USER", "MYSQLUSER", "MYSQL_USER", "DB_USERNAME");
                if (dbUser == null) dbUser = "root";
            }
            if (dbPassword == null) {
                dbPassword = getFirstEnv("DB_PASSWORD", "MYSQLPASSWORD", "MYSQL_PASSWORD", "DB_PASS");
                if (dbPassword == null) dbPassword = "root";
            }
        }

        LOGGER.info(String.format("🔌 Cấu hình JDBC Database: URL=%s | User=%s", dbUrl, dbUser));
    }

    private static String getFirstEnv(String... keys) {
        for (String key : keys) {
            String val = System.getenv(key);
            if (val != null && !val.trim().isEmpty()) {
                return val.trim();
            }
        }
        return null;
    }

    /**
     * Lấy kết nối tới MySQL Database
     *
     * @return Connection hoặc null nếu không thể kết nối
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Không thể kết nối cơ sở dữ liệu (" + dbUrl + "): " + e.getMessage());
            return null;
        }
    }

    /**
     * Đóng kết nối an toàn
     */
    public static void close(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
