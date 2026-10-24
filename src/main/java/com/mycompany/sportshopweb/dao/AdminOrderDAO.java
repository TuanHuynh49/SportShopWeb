package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Data Access Object phục vụ riêng cho các chức năng quản lý đơn hàng của Admin.
 */
public class AdminOrderDAO {

    /**
     * Lấy toàn bộ đơn hàng kèm thông tin Customer, User và OrderItems
     * để hiển thị tại trang danh sách đơn hàng Admin.
     *
     * @return danh sách tất cả đơn hàng, sắp xếp giảm dần theo orderId
     */
    public List<Order> getAllOrdersWithDetails() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "JOIN FETCH o.customer c " +
                    "JOIN FETCH c.user " +
                    "LEFT JOIN FETCH o.orderItems " +
                    "ORDER BY o.orderId DESC";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy thông tin chi tiết của một đơn hàng theo orderId kèm Customer, User,
     * OrderItems, ProductVariant và Product.
     *
     * @param orderId ID của đơn hàng cần lấy
     * @return Order hoặc null nếu không tìm thấy
     */
    public Order getOrderDetailById(int orderId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            List<Order> results = em.createQuery(
                    "SELECT DISTINCT o FROM Order o " +
                            "JOIN FETCH o.customer c " +
                            "JOIN FETCH c.user " +
                            "LEFT JOIN FETCH o.orderItems oi " +
                            "LEFT JOIN FETCH oi.productVariant pv " +
                            "LEFT JOIN FETCH pv.product " +
                            "WHERE o.orderId = :orderId",
                    Order.class)
                    .setParameter("orderId", orderId)
                    .getResultList();

            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
}
