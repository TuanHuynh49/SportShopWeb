package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Admin;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Data Access Object chuyên trách xác thực và kiểm tra quyền hạn của Admin.
 */
public class AdminAuthDAO {

    /**
     * Kiểm tra xem người dùng có quyền quản trị viên (Admin) hay không.
     *
     * @param userId ID của tài khoản User
     * @return true nếu tài khoản tồn tại trong bảng Admins và có role là "Admin"
     */
    public boolean isAdmin(int userId) {
        EntityManager em = null;

        try {
            em = DBUtil.getEmFactory().createEntityManager();

            List<Admin> admins = em.createQuery(
                    "SELECT a FROM Admin a " +
                            "JOIN FETCH a.user " +
                            "JOIN FETCH a.role " +
                            "WHERE a.user.userId = :userId",
                    Admin.class)
                    .setParameter("userId", userId)
                    .getResultList();

            if (!admins.isEmpty()) {
                Admin admin = admins.get(0);
                return admin.getRole() != null
                        && admin.getRole().getName() != null
                        && "Admin".equalsIgnoreCase(admin.getRole().getName().trim());
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
