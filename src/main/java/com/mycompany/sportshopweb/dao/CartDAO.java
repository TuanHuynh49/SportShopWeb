
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class CartDAO {

   
public Cart findByCustomerId(int customerId) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();

    try {
        String jpql =
            "SELECT DISTINCT c " +
            "FROM Cart c " +
            "LEFT JOIN FETCH c.cartItems ci " +
            "LEFT JOIN FETCH ci.productVariant pv " +
            "LEFT JOIN FETCH pv.product p " +
            "WHERE c.customerId = :customerId "+ 
             "ORDER BY pv.variantId ASC";

        TypedQuery<Cart> query =
                em.createQuery(jpql, Cart.class);

        query.setParameter("customerId", customerId);

        // Yêu cầu JPA làm mới dữ liệu từ database
        query.setHint(
            "jakarta.persistence.cache.retrieveMode",
            jakarta.persistence.CacheRetrieveMode.BYPASS
        );

        query.setHint(
            "jakarta.persistence.cache.storeMode",
            jakarta.persistence.CacheStoreMode.REFRESH
        );

        Cart cart = query.getSingleResult();
        return cart;

    } catch (NoResultException e) {
        return null;
    } finally {
        em.close();
    }
}

    
public void insert(Cart cart) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    EntityTransaction trans = em.getTransaction();

    try {
        trans.begin();

        em.persist(cart);

        trans.commit();

    } catch (Exception e) {
        if (trans.isActive()) {
            trans.rollback();
        }

        e.printStackTrace(); // In lỗi thật ra console NetBeans
        throw new RuntimeException("Không lưu được Cart", e);

    } finally {
        em.close();
    }
}
}
