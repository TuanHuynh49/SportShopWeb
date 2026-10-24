package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Payment;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

//Dùng JPA tương tác với entity Payment
public class PaymentDAO {

    // Tìm 1 Payment theo paymentid
    public Payment findById(int paymentId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            return em.find(Payment.class, paymentId);
        } finally {
            em.close();// Luôn đóng EntityManager, dù truy vấn được hay không
        }
    }

    // Tìm các Payment thuộc 1 đơn hàng
    public List<Payment> findByOrderId(int orderId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            // Truy vấn theo tên entity và attribute
            String jpql = "SELECT p " +
                    "FROM Payment p " +
                    "WHERE p.orderId = :orderId " +
                    "ORDER BY p.paymentTime DESC";

            // Tạo truy vấn có kiểu kết quả là Payment.
            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);

            query.setParameter("orderId", orderId);

            // Trả về danh sách kết quả; nếu không có kết quả,
            // JPA trả về danh sách rỗng thay vì null.
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Lấy toàn bộ danh sách Payment.
    public List<Payment> findAll() {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT p " +
                    "FROM Payment p " +
                    "ORDER BY p.paymentId DESC";

            TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    // Thêm một bản ghi Payment mới.
    public void insertPayment(Payment payment) {

        // Kiểm tra đầu vào trước khi mở transaction.
        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment must not be null.");
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // persist() đánh dấu entity mới để JPA lưu vào database
            // khi transaction được commit.
            em.persist(payment);

            // Xác nhận transaction; JPA thực hiện đồng bộ dữ liệu.
            transaction.commit();

        } catch (RuntimeException e) {

            // Chỉ rollback khi transaction vẫn đang hoạt động.
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    // Cập nhật một bản ghi Payment đã tồn tại.
    public void updatePayment(Payment payment) {

        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment must not be null.");
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            // merge() đồng bộ trạng thái của đối tượng truyền vào
            // với persistence context và trả về entity được quản lý.
            em.merge(payment);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public List<Payment> findByOrderId(
            EntityManager em, int orderId) {

        return em.createQuery(
                "SELECT p FROM Payment p "
                + "WHERE p.orderId = :orderId "
                + "ORDER BY p.paymentTime DESC",
                Payment.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public void insertPayment(
            EntityManager em, Payment payment) {

        if (em == null || payment == null) {
            throw new IllegalArgumentException(
                    "EntityManager và Payment không được null.");
        }

        em.persist(payment);
    }

}
