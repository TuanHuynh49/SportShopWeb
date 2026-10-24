package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Adress;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class AdressDAO {

    // Lấy các địa chỉ chưa bị xóa của khách hàng (ưu tiên địa chỉ mặc định lên đầu)
    public List<Adress> findByCustomerId(int customerId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT a FROM Adress a "
                    + "WHERE a.customer.customerId = :customerId "
                    + "AND a.deleted = false "
                    + "ORDER BY a.isDefault DESC";

            var query = em.createQuery(jpql, Adress.class);
            query.setParameter("customerId", customerId);

            List<Adress> addresses = query.getResultList();
            return addresses;
        } finally {
            em.close();
        }
    }

    // Lấy địa chỉ mặc định chưa bị xóa của khách hàng
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

    // Tìm địa chỉ theo ID và ID khách hàng (chỉ lấy địa chỉ chưa bị xóa)
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

    // Thêm địa chỉ mới
    public boolean insert(Adress address) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            int customerId = address.getCustomer().getCustomerId();
            address.setDeleted(false);

            // Đếm số địa chỉ đang hoạt động
            String countJpql = "SELECT COUNT(a) FROM Adress a "
                    + "WHERE a.customer.customerId = :customerId "
                    + "AND a.deleted = false";

            var countQuery = em.createQuery(countJpql, Long.class);
            countQuery.setParameter("customerId", customerId);

            Long addressCount = countQuery.getSingleResult();

            // Nếu là địa chỉ hoạt động đầu tiên, tự động đặt làm mặc định
            if (addressCount == 0) {
                address.setDefault(true);
            }

            // Nếu đặt địa chỉ mới làm mặc định, hủy mặc định của các địa chỉ khác
            if (address.getDefaultAddress()) {
                resetDefaultAddress(em, customerId);
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

    // Cập nhật thông tin địa chỉ chưa bị xóa
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
                resetDefaultAddress(em, customerId);
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
// Đặt một địa chỉ làm mặc định
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
            resetDefaultAddress(em, customerId);
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

    // Xóa mềm địa chỉ (đánh dấu deleted = true, không xóa vật lý khỏi cơ sở dữ liệu)
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

            // Đánh dấu đã xóa mềm và gỡ cờ mặc định
            address.setDeleted(true);
            address.setDefault(false);

            // Đồng bộ trạng thái vào EntityManager trước khi truy vấn địa chỉ tiếp theo
            em.flush();

            // Nếu xóa địa chỉ mặc định, tự động chọn 1 địa chỉ còn hoạt động khác làm mặc định thay thế
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

    // Đặt tất cả địa chỉ của khách hàng về không mặc định (Áp dụng DRY)
    private void resetDefaultAddress(EntityManager em, int customerId) {
        String updateJpql = "UPDATE Adress a "
                + "SET a.isDefault = false "
                + "WHERE a.customer.customerId = :customerId "
                + "AND a.deleted = false";

        var updateQuery = em.createQuery(updateJpql);
        updateQuery.setParameter("customerId", customerId);
        updateQuery.executeUpdate();
    }
}