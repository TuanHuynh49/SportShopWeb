
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.CartItem;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CartItemDAO {

    public void insertCartItem(int customerId, int variantId, int quantity) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            Cart cart = em.find(Cart.class, customerId);
            ProductVariant variant = em.find(ProductVariant.class, variantId);

            if (cart == null || variant == null) {
                throw new IllegalArgumentException(
                    "Không tìm thấy giỏ hàng hoặc sản phẩm."
                );
            }

            TypedQuery<CartItem> query = em.createQuery(
                "SELECT ci FROM CartItem ci "
                + "WHERE ci.cart.customerId = :customerId "
                + "AND ci.productVariant.variantId = :variantId",
                CartItem.class
            );

            query.setParameter("customerId", customerId);
            query.setParameter("variantId", variantId);

            List<CartItem> items = query.getResultList();

            if (!items.isEmpty()) {
                CartItem cartItem = items.get(0);
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProductVariant(variant);
                cartItem.setQuantity(quantity);

                em.persist(cartItem);
            }

            trans.commit();

        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }

            throw new RuntimeException(
                "Không thể thêm sản phẩm vào giỏ hàng.", e
            );

        } finally {
            em.close();
        }
    }

    public void updateCartItem(int customerId, int variantId, int quantity) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            TypedQuery<CartItem> query = em.createQuery(
                "SELECT ci FROM CartItem ci "
                + "WHERE ci.cart.customerId = :customerId "
                + "AND ci.productVariant.variantId = :variantId",
                CartItem.class
            );

            query.setParameter("customerId", customerId);
            query.setParameter("variantId", variantId);

            List<CartItem> items = query.getResultList();

            if (items.isEmpty()) {
                throw new IllegalArgumentException(
                    "Không tìm thấy sản phẩm trong giỏ hàng."
                );
            }

            items.get(0).setQuantity(quantity);

            trans.commit();

        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }

            throw new RuntimeException(
                "Không thể cập nhật sản phẩm trong giỏ hàng.", e
            );

        } finally {
            em.close();
        }
    }

    
    public void deleteCartItem(int customerId, int variantId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            List<CartItem> items = em.createQuery(
                "SELECT ci FROM CartItem ci "
                + "WHERE ci.cart.customerId = :customerId "
                + "AND ci.productVariant.variantId = :variantId",
                CartItem.class
            )
            .setParameter("customerId", customerId)
            .setParameter("variantId", variantId)
            .getResultList();

            if (items.isEmpty()) {
                throw new IllegalArgumentException(
                    "Không tìm thấy sản phẩm trong giỏ hàng. "
                    + "customerId=" + customerId
                    + ", variantId=" + variantId
                );
            }

            em.remove(items.get(0));
            trans.commit();

        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw new RuntimeException(
                "Không thể xóa sản phẩm khỏi giỏ hàng.", e
            );
        } finally {
            em.close();
        }
    }
}
