package com.sportshop.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AdminDAO — Truy vấn thống kê tổng hợp (KPIs, Doanh thu, Đơn hàng, Tồn kho)
 * Theo đúng chuẩn REQ-A08 trong tài liệu OR-Mapping.
 */
public class AdminDAO {

    private static final Logger LOGGER = Logger.getLogger(AdminDAO.class.getName());
    private static final AdminDAO INSTANCE = new AdminDAO();

    private AdminDAO() {
    }

    public static AdminDAO getInstance() {
        return INSTANCE;
    }

    /**
     * Lấy dữ liệu tổng hợp Dashboard (Doanh thu, Tổng đơn hàng, Tổng sản phẩm, Khách hàng)
     * Trả về các giá trị 0 an toàn khi Database chưa có dữ liệu hoặc chưa kết nối
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", BigDecimal.ZERO);
        stats.put("totalOrders", 0L);
        stats.put("pendingOrders", 0L);
        stats.put("totalProducts", 0L);
        stats.put("totalCustomers", 0L);

        Connection conn = DBContext.getConnection();
        if (conn == null) {
            return stats;
        }

        try {
            // 1. Tổng doanh thu từ bảng payment (hoặc orders đã thanh toán)
            String sqlRevenue = "SELECT COALESCE(SUM(amount), 0) FROM payment WHERE payment_status = 'PAID'";
            try (PreparedStatement ps = conn.prepareStatement(sqlRevenue);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalRevenue", rs.getBigDecimal(1));
                }
            }

            // 2. Tổng số đơn hàng & đơn chờ xử lý
            String sqlOrders = "SELECT COUNT(order_id), " +
                               "COUNT(CASE WHEN status = 'PENDING' THEN 1 END) " +
                               "FROM orders";
            try (PreparedStatement ps = conn.prepareStatement(sqlOrders);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalOrders", rs.getLong(1));
                    stats.put("pendingOrders", rs.getLong(2));
                }
            }

            // 3. Tổng số sản phẩm
            String sqlProducts = "SELECT COUNT(product_id) FROM product WHERE status = 1";
            try (PreparedStatement ps = conn.prepareStatement(sqlProducts);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalProducts", rs.getLong(1));
                }
            }

            // 4. Tổng số khách hàng
            String sqlCustomers = "SELECT COUNT(customer_id) FROM customer WHERE role = 'CUSTOMER'";
            try (PreparedStatement ps = conn.prepareStatement(sqlCustomers);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalCustomers", rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn AdminDAO.getDashboardStats: " + e.getMessage());
        } finally {
            DBContext.close(conn);
        }

        return stats;
    }
}
