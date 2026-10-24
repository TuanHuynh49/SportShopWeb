
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Adress;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class AdressDAO {

    // 1. Lấy tất cả địa chỉ chưa bị xóa của khách hàng
    public List<Adress> findByCustomerId(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT a FROM Adress a "
                    + "WHERE a.customer.customerId = :customerId "
                    + "AND a.deleted = false "
                    + "ORDER BY a.isDefault DESC";

            var query = em.createQuery(jpql, Adress.class);
            query.setParameter("customerId", customerId);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // 2. Lấy địa chỉ mặc định chưa bị xóa
    public Adress findDefaultByCustomerId(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT a FROM Adress a "
                    + "WHERE a.customer.customerId = :customerId "
                    + "AND a.isDefault = true "
                    + "AND a.deleted = false";

            var query = em.createQuery(jpql, Adress.class);
            query.setParameter("customerId", customerId);
            query.setMaxResults(1);

            List<Adress> addresses = query.getResultList();

            if (addresses.isEmpty()) {
                return null;
            }

            return addresses.get(0);
        } finally {
            em.close();
        }
    }

    // 3. Tìm địa chỉ chưa bị xóa theo ID và ID khách hàng
    public Adress findByIdAndCustomerId(int addressId, int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT a FROM Adress a "
                    + "WHERE a.addressId = :addressId "
                    + "AND a.customer.customerId = :customerId "
                    + "AND a.deleted = false";

            var query = em.createQuery(jpql, Adress.class);
            query.setParameter("addressId", addressId);
            query.setParameter("customerId", customerId);
            query.setMaxResults(1);

            List<Adress> addresses = query.getResultList();

            if (addresses.isEmpty()) {
                return null;
            }

            return addresses.get(0);
        } finally {
            em.close();
        }
    }

    // 4. Thêm địa chỉ mới
    public boolean insert(Adress address) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            int customerId = address.getCustomer().getCustomerId();

            // Địa chỉ mới chưa bị xóa
            address.setDeleted(false);

            // Đếm các địa chỉ còn hoạt động
            String countJpql = "SELECT COUNT(a) FROM Adress a "
                    + "WHERE a.customer.customerId = :customerId "
                    + "AND a.deleted = false";

            var countQuery = em.createQuery(countJpql, Long.class);
            countQuery.setParameter("customerId", customerId);

            Long addressCount = countQuery.getSingleResult();

            // Nếu là địa chỉ hoạt động đầu tiên thì đặt mặc định
            if (addressCount == 0) {
                address.setDefault(true);
            }

            // Nếu đặt địa chỉ mới làm mặc định
            if (address.getDefaultAddress()) {
                String updateJpql = "UPDATE Adress a "
                        + "SET a.isDefault = false "
                        + "WHERE a.customer.customerId = :customerId "
                        + "AND a.deleted = false";

                var updateQuery = em.createQuery(updateJpql);
                updateQuery.setParameter("customerId", customerId);
                updateQuery.executeUpdate();
            }

            em.persist(address);
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

    // 5. Cập nhật địa chỉ chưa bị xóa
    public boolean update(Adress address, int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            String jpql = "SELECT a FROM Adress a "
                    + "WHERE a.addressId = :addressId "
                    + "AND a.customer.customerId = :customerId "
                    + "AND a.deleted = false";

            var query = em.createQuery(jpql, Adress.class);
            query.setParameter("addressId", address.getAddressId());
            query.setParameter("customerId", customerId);
            query.setMaxResults(1);

            List<Adress> addresses = query.getResultList();

            if (addresses.isEmpty()) {
                trans.rollback();
                return false;
            }

            Adress existing = addresses.get(0);

            existing.setProvince(address.getProvince());
            existing.setWard(address.getWard());
            existing.setDetailAddress(address.getDetailAddress());

            if (address.getDefaultAddress()) {
                String updateJpql = "UPDATE Adress a "
                        + "SET a.isDefault = false "
                        + "WHERE a.customer.customerId = :customerId "
                        + "AND a.deleted = false";

                var updateQuery = em.createQuery(updateJpql);
                updateQuery.setParameter("customerId", customerId);
                updateQuery.executeUpdate();

                existing.setDefault(true);
            }

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

    // 6. Đặt địa chỉ chưa bị xóa làm mặc định
    public boolean setDefault(int addressId, int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            String jpql = "SELECT a FROM Adress a "
                    + "WHERE a.addressId = :addressId "
                    + "AND a.customer.customerId = :customerId "
                    + "AND a.deleted = false";

            var query = em.createQuery(jpql, Adress.class);
            query.setParameter("addressId", addressId);
            query.setParameter("customerId", customerId);
            query.setMaxResults(1);

            List<Adress> addresses = query.getResultList();

            if (addresses.isEmpty()) {
                trans.rollback();
                return false;
            }

            Adress address = addresses.get(0);

            String updateJpql = "UPDATE Adress a "
                    + "SET a.isDefault = false "
                    + "WHERE a.customer.customerId = :customerId "
                    + "AND a.deleted = false";

            var updateQuery = em.createQuery(updateJpql);
            updateQuery.setParameter("customerId", customerId);
            updateQuery.executeUpdate();

            address.setDefault(true);

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

    // 7. Xóa mềm địa chỉ
    public boolean softDelete(int addressId, int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            String jpql = "SELECT a FROM Adress a "
                    + "WHERE a.addressId = :addressId "
                    + "AND a.customer.customerId = :customerId "
                    + "AND a.deleted = false";

            var query = em.createQuery(jpql, Adress.class);
            query.setParameter("addressId", addressId);
            query.setParameter("customerId", customerId);
            query.setMaxResults(1);

            List<Adress> addresses = query.getResultList();

            if (addresses.isEmpty()) {
                trans.rollback();
                return false;
            }

            Adress address = addresses.get(0);
            boolean wasDefault = address.getDefaultAddress();

            // Đánh dấu đã xóa, không gọi em.remove()
            address.setDeleted(true);
            address.setDefault(false);

            // Nếu xóa địa chỉ mặc định, chọn địa chỉ khác còn hoạt động
            if (wasDefault) {
                String nextJpql = "SELECT a FROM Adress a "
                        + "WHERE a.customer.customerId = :customerId "
                        + "AND a.deleted = false "
                        + "ORDER BY a.addressId ASC";

                var nextQuery = em.createQuery(nextJpql, Adress.class);
                nextQuery.setParameter("customerId", customerId);
                nextQuery.setMaxResults(1);

                List<Adress> remainingAddresses = nextQuery.getResultList();

                if (!remainingAddresses.isEmpty()) {
                    remainingAddresses.get(0).setDefault(true);
                }
            }

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
}
