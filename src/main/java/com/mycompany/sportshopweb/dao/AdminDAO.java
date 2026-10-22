
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Admin;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class AdminDAO {

    public Admin findByUserId(int userId) {
        EntityManager em = null;

        try {
            em = DBUtil.getEmFactory().createEntityManager();

            TypedQuery<Admin> query = em.createQuery(
                    "SELECT a FROM Admin a WHERE a.user.userId = :userId",
                    Admin.class);

            query.setParameter("userId", userId); // nhét vào

            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}