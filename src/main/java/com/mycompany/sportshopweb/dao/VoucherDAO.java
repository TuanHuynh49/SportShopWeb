
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Voucher;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;
import java.util.List;

public class VoucherDAO {

    public Voucher findByCode(String code) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql =
                "SELECT v FROM Voucher v WHERE v.code = :code";

            TypedQuery<Voucher> query =
                em.createQuery(jpql, Voucher.class);

            query.setParameter("code", code.trim());

            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public void insertVoucher(Voucher voucher) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            em.persist(voucher);

            trans.commit();

        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
        } finally {
            em.close();
        }
    }
    
    public void updateVoucher(int voucherId, double discount, boolean active) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        var trans = em.getTransaction();

        try {
            trans.begin();

            Voucher voucher = em.find(Voucher.class, voucherId);

            if (voucher == null) {
                throw new IllegalArgumentException("Voucher not found!");
            }

            voucher.setDiscount(discount);
            voucher.setActive(active);

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

    public List<Voucher> findActiveVouchers() {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();

    try {
        String jpql = "SELECT v FROM Voucher v "
                    + "WHERE v.active = true "
                    + "ORDER BY v.discount DESC";

        return em.createQuery(jpql, Voucher.class)
                 .getResultList();
    } finally {
        em.close();
    }
}

}
