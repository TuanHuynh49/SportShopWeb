package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Data Access Object phục vụ riêng cho các chức năng quản lý khách hàng của Admin.
 */
public class AdminCustomerDAO {

    /**
     * Lấy danh sách toàn bộ khách hàng kèm User và MembershipTier
     * để hiển thị bảng dữ liệu khách hàng.
     *
     * @return danh sách khách hàng sắp xếp giảm dần theo customerId
     */
    public List<Customer> getAllCustomersWithDetails() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Customer c " +
                            "JOIN FETCH c.user " +
                            "LEFT JOIN FETCH c.membershipTier " +
                            "ORDER BY c.customerId DESC",
                    Customer.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy danh sách ID các khách hàng đã từng phát sinh đơn hàng
     * phục vụ tính toán các chỉ số thống kê trên trang Admin.
     *
     * @return danh sách customerId duy nhất
     */
    public List<Integer> getCustomerIdsWithOrders() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT o.customer.customerId FROM Order o",
                    Integer.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Tìm thông tin chi tiết một khách hàng theo ID kèm User và MembershipTier.
     *
     * @param customerId ID khách hàng cần tìm
     * @return Customer hoặc null nếu không tìm thấy
     */
    public Customer findCustomerById(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT c FROM Customer c " +
                    "LEFT JOIN FETCH c.user " +
                    "LEFT JOIN FETCH c.membershipTier " +
                    "WHERE c.customerId = :customerId";

            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setParameter("customerId", customerId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Lấy danh sách lịch sử đơn hàng của một khách hàng kèm các OrderItems
     * để hiển thị tại bảng chi tiết thông tin khách hàng.
     *
     * @param customerId ID của khách hàng
     * @return danh sách đơn hàng sắp xếp theo orderId giảm dần
     */
    public List<Order> getOrdersByCustomerId(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT o FROM Order o " +
                            "LEFT JOIN FETCH o.orderItems " +
                            "WHERE o.customer.customerId = :id " +
                            "ORDER BY o.orderId DESC",
                    Order.class)
                    .setParameter("id", customerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
