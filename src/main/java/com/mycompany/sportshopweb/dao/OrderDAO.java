
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrderDAO {

    // =========================================================
    // 1. TÌM ĐƠN HÀNG THEO KHÁCH HÀNG
    // =========================================================

    public List<Order> findByCustomerId(int customerId) {
        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql =
                    "SELECT DISTINCT o "
                    + "FROM Order o "
                    + "LEFT JOIN FETCH o.orderItems oi "
                    + "LEFT JOIN FETCH oi.productVariant pv "
                    + "LEFT JOIN FETCH pv.product p "
                    + "JOIN FETCH o.customer c "
                    + "JOIN FETCH c.user "
                    + "LEFT JOIN FETCH o.address a "
                    + "WHERE c.customerId = :customerId "
                    + "ORDER BY o.orderId DESC";

            TypedQuery<Order> query =
                    em.createQuery(jpql, Order.class);

            query.setParameter("customerId", customerId);

            query.setHint(
                    "jakarta.persistence.cache.retrieveMode",
                    CacheRetrieveMode.BYPASS
            );

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    // =========================================================
    // 2. THÊM ĐƠN HÀNG - PHIÊN BẢN TỰ QUẢN LÝ TRANSACTION
    // =========================================================

    public void insertOrder(Order order) {
        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            insertOrder(em, order);

            trans.commit();

        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // =========================================================
    // 3. THÊM ĐƠN HÀNG - DÙNG TRANSACTION CỦA BÊN GỌI
    // =========================================================

    public void insertOrder(EntityManager em, Order order) {
        if (em == null || order == null) {
            throw new IllegalArgumentException(
                    "EntityManager và Order không được null!");
        }

        em.persist(order);
    }

    // =========================================================
    // 4. CẬP NHẬT ĐƠN HÀNG
    // =========================================================

    public void updateOrder(Order order) {
        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            em.merge(order);

            trans.commit();

        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // =========================================================
    // 5. TÌM ĐƠN HÀNG VÀ KHÓA ĐỂ XỬ LÝ THANH TOÁN
    // =========================================================

    public Order findByIdForUpdate(
            EntityManager em,
            int orderId) {

        if (em == null || orderId <= 0) {
            return null;
        }

        return em.find(
                Order.class,
                orderId,
                LockModeType.PESSIMISTIC_WRITE
        );
    }

    // =========================================================
    // 6. CẬP NHẬT TRẠNG THÁI - DÙNG TRANSACTION HIỆN CÓ
    // =========================================================

    public boolean updateOrderStatus(
            EntityManager em,
            int orderId,
            OrderStatus status) {

        if (em == null || orderId <= 0 || status == null) {
            return false;
        }

        Order order = em.find(Order.class, orderId);

        if (order == null) {
            return false;
        }

        order.setOrderStatus(status);

        return true;
    }

    // =========================================================
    // 7. CẬP NHẬT TRẠNG THÁI - TỰ QUẢN LÝ TRANSACTION
    // =========================================================

    public boolean updateOrderStatus(
            int orderId,
            OrderStatus status) {

        if (orderId <= 0 || status == null) {
            return false;
        }

        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            boolean updated =
                    updateOrderStatus(em, orderId, status);

            if (!updated) {
                trans.rollback();
                return false;
            }

            trans.commit();

            return true;

        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // =========================================================
    // 8. HỦY ĐƠN HÀNG
    // =========================================================

    public void cancelOrder(int orderId) {
        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            Order order = em.find(
                    Order.class,
                    orderId,
                    LockModeType.PESSIMISTIC_WRITE
            );

            if (order != null
                    && order.getOrderStatus() == OrderStatus.CONFIRMED) {

                order.setOrderStatus(OrderStatus.CANCELLED);
            }

            trans.commit();

        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // =========================================================
    // 9. KIỂM TRA ĐƠN HÀNG ĐÃ HẾT HẠN THANH TOÁN CHƯA
    // =========================================================

    public boolean isPaymentExpired(Order order) {
        if (order == null
                || order.getOrderStatus() != OrderStatus.CONFIRMED
                || order.getOrderDate() == null) {

            return false;
        }

        long elapsed = System.currentTimeMillis()
                - order.getOrderDate().getTime();

        long fifteenMinutes = 15 * 60 * 1000L;

        return elapsed >= fifteenMinutes;
    }

    // =========================================================
    // 10. HỦY ĐƠN HẾT HẠN THANH TOÁN
    // =========================================================

    public void cancelExpiredOrder(int orderId) {
        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            Order order = em.find(
                    Order.class,
                    orderId,
                    LockModeType.PESSIMISTIC_WRITE
            );

            if (isPaymentExpired(order)) {
                order.setOrderStatus(OrderStatus.CANCELLED);
            }

            trans.commit();

        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }
}
