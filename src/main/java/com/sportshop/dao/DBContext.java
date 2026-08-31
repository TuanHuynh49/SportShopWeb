package com.sportshop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBContext — Quản lý kết nối JDBC tới cơ sở dữ liệu MySQL (sportshop_db).
 */
public class DBContext {

    private static final Logger LOGGER = Logger.getLogger(DBContext.class.getName());

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:mysql://localhost:3306/sportshop_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String DB_USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "root";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "root";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Không tìm thấy MySQL JDBC Driver: " + DB_DRIVER, e);
        }
    }

    /**
     * Lấy kết nối tới MySQL Database
     *
     * @return Connection hoặc null nếu không thể kết nối
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Không thể kết nối cơ sở dữ liệu (" + DB_URL + "): " + e.getMessage());
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
