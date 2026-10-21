package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.model.User;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Payment;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AdminDashboardDAO {

    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public long getTodayRevenue() {
        LocalDate today = LocalDate.now(ZONE);

        return getRevenue(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay());
    }

    public long getMonthRevenue() {
        LocalDate today = LocalDate.now(ZONE);
        LocalDate firstDay = today.withDayOfMonth(1);

        return getRevenue(
                firstDay.atStartOfDay(),
                firstDay.plusMonths(1).atStartOfDay());
    }

    private long getRevenue(
            LocalDateTime start,
            LocalDateTime end) {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT COALESCE(SUM(p.amount), 0) " +
                    "FROM Payment p, Order o " +
                    "WHERE p.orderId = o.orderId " +
                    "AND o.status = :status " +
                    "AND p.paymentTime >= :start " +
                    "AND p.paymentTime < :end";

            TypedQuery<Number> query = em.createQuery(jpql, Number.class);

            query.setParameter("status", OrderStatus.COMPLETED);
            query.setParameter("start", start);
            query.setParameter("end", end);

            Number result = query.getSingleResult();
            return result == null ? 0L : result.longValue();

        } finally {
            em.close();
        }
    }

    public long getTodayOrderCount() {
        LocalDate today = LocalDate.now(ZONE);

        Date start = Date.from(
                today.atStartOfDay(ZONE).toInstant());

        Date end = Date.from(
                today.plusDays(1).atStartOfDay(ZONE).toInstant());

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT COUNT(o) FROM Order o " +
                    "WHERE o.orderDate >= :start " +
                    "AND o.orderDate < :end";

            TypedQuery<Number> query = em.createQuery(jpql, Number.class);

            query.setParameter("start", start);
            query.setParameter("end", end);

            Number result = query.getSingleResult();
            return result == null ? 0L : result.longValue();

        } finally {
            em.close();
        }
    }

    public long getTodayCustomerCount() {
        LocalDate today = LocalDate.now(ZONE);

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT COUNT(DISTINCT u.userId) " +
                    "FROM User u, Customer c " +
                    "WHERE c.user = u " +
                    "AND u.createdAt >= :start " +
                    "AND u.createdAt < :end";

            TypedQuery<Number> query = em.createQuery(jpql, Number.class);

            query.setParameter("start", start);
            query.setParameter("end", end);

            Number result = query.getSingleResult();
            return result == null ? 0L : result.longValue();

        } finally {
            em.close();
        }
    }

    public List<Order> getRecentOrders() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "LEFT JOIN FETCH o.customer c " +
                    "LEFT JOIN FETCH o.orderItems oi " +
                    "ORDER BY o.orderId DESC";

            return em.createQuery(jpql, Order.class)
                    .setMaxResults(5)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}
