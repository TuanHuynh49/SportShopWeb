
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderItem;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class OrderItemDAO {

    // Hàm cũ: tự tạo EntityManager và transaction
    public void insertOrderItem(int orderId, int variantId, int quantity) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            insertOrderItem(em, orderId, variantId, quantity);

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

    // Hàm mới: sử dụng EntityManager và transaction do bên gọi quản lý
    public void insertOrderItem(
            EntityManager em,
            int orderId,
            int variantId,
            int quantity) {

        if (em == null) {
            throw new IllegalArgumentException(
                    "EntityManager không được null!");
        }

        if (orderId <= 0) {
            throw new IllegalArgumentException(
                    "Mã đơn hàng không hợp lệ!");
        }

        if (variantId <= 0) {
            throw new IllegalArgumentException(
                    "Mã biến thể sản phẩm không hợp lệ!");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Số lượng phải lớn hơn 0!");
        }

        Order order = em.find(Order.class, orderId);

        if (order == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy đơn hàng!");
        }

        ProductVariant variant = em.find(
                ProductVariant.class, variantId);

        if (variant == null) {
            throw new IllegalArgumentException(
                    "Không tìm thấy biến thể sản phẩm!");
        }

        OrderItem orderItem = new OrderItem(variant, quantity);
        orderItem.setOrder(order);

        em.persist(orderItem);
    }
}
