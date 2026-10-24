
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderItem;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class OrderItemDAO {

    public void insertOrderItem(int orderId, int variantId, int quantity) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            Order order = em.find(Order.class, orderId);
            ProductVariant variant = em.find(ProductVariant.class, variantId);

            if (order == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng!");
            }

            if (variant == null) {
                throw new IllegalArgumentException("Không tìm thấy biến thể sản phẩm!");
            }

            if (quantity <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0!");
            }

            OrderItem orderItem = new OrderItem(variant, quantity);
            orderItem.setOrder(order);

            em.persist(orderItem);

            trans.commit();

        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
