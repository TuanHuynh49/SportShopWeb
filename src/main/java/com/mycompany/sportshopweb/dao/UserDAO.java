/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.User;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author Anh Tuan
 */
public class UserDAO {

    // Thêm mới người dùng
    public boolean insert(User user) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(user);
            trans.commit();
            return true;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Cập nhật thông tin người dùng
    public boolean update(User user) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(user);
            trans.commit();
            return true;
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Tìm người dùng theo email hoặc số điện thoại (Đăng nhập)
    public User findByIdentifier(String identifier) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        String jpql = "SELECT u FROM User u WHERE u.email = :identifier OR u.phone = :identifier"; // đánh dấu chỗ truyền vào
        try {
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("identifier", identifier); // xử lý dưới dạng PrepareStatement
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Tìm người dùng theo ID
    public User findById(int userId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            return em.find(User.class, userId);
        } finally {
            em.close();
        }
    }

    // Kiểm tra email đã tồn tại
    public boolean existsByEmail(String email) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        try {
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    // Kiểm tra số điện thoại đã tồn tại
    public boolean existsByPhone(String phone) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.phone = :phone";
        try {
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("phone", phone);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}
