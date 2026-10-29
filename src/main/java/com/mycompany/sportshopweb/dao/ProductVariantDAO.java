package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductVariantDAO {

    public List<ProductVariant> getVariantsByProductId(int productId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            TypedQuery<ProductVariant> query = em.createQuery(
                    "SELECT v FROM ProductVariant v "
                            + "WHERE v.product.productId = :productId "
                            + "ORDER BY v.variantId",
                    ProductVariant.class);

            query.setParameter("productId", productId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ProductVariant getVariantById(int variantId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            return em.find(ProductVariant.class, variantId);
        } finally {
            em.close();
        }
    }

    public boolean insertVariant(
            int productId,
            String variantName,
            String size,
            String color,
            long price,
            int quantity) {

        if (productId <= 0 || price < 0 || quantity < 0) {
            return false;
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Product product = em.find(Product.class, productId);

            if (product == null) {
                return false;
            }

            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setVariantName(variantName);
            variant.setSize(size);
            variant.setColor(color);
            variant.setPrice(price);
            variant.setQuantity(quantity);

            tx.begin();
            em.persist(variant);
            tx.commit();

            try {
                em.getEntityManagerFactory().getCache().evict(ProductVariant.class);
                em.getEntityManagerFactory().getCache().evict(Product.class, productId);
            } catch (Exception ignored) {
            }

            return true;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean updateVariant(
            int variantId,
            String variantName,
            String size,
            String color,
            long price,
            int quantity) {

        if (variantId <= 0 || price < 0 || quantity < 0) {
            return false;
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            ProductVariant variant = em.find(ProductVariant.class, variantId);

            if (variant == null) {
                return false;
            }

            tx.begin();

            variant.setVariantName(variantName);
            variant.setSize(size);
            variant.setColor(color);
            variant.setPrice(price);
            variant.setQuantity(quantity);

            em.merge(variant);
            tx.commit();

            try {
                em.getEntityManagerFactory().getCache().evict(ProductVariant.class);
                if (variant.getProduct() != null) {
                    em.getEntityManagerFactory().getCache().evict(Product.class, variant.getProduct().getProductId());
                }
            } catch (Exception ignored) {
            }

            return true;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean deleteVariant(int variantId) {
        if (variantId <= 0) {
            return false;
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            ProductVariant variant = em.find(ProductVariant.class, variantId);

            if (variant == null) {
                return false;
            }

            tx.begin();
            int prodId = variant.getProduct() != null ? variant.getProduct().getProductId() : 0;
            em.remove(variant);
            tx.commit();

            try {
                em.getEntityManagerFactory().getCache().evict(ProductVariant.class);
                if (prodId > 0) {
                    em.getEntityManagerFactory().getCache().evict(Product.class, prodId);
                }
            } catch (Exception ignored) {
            }

            return true;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<ProductVariant> getAllVariantsWithProducts() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT v FROM ProductVariant v "
                            + "JOIN FETCH v.product p "
                            + "LEFT JOIN FETCH p.images "
                            + "ORDER BY p.productId DESC, v.variantId ASC",
                    ProductVariant.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // =========================================================================
    // CÁC HÀM XỬ LÝ KHÓA GIỮ CHỖ & TRỪ TỒN KHO CHO LUỒNG THANH TOÁN (PHƯƠNG ÁN A)
    // =========================================================================

    /**
     * 1. Khóa giữ chỗ tồn kho (Soft Lock / Inventory Reservation):
     * Tăng reservedQuantity lên một lượng bằng quantityToReserve,
     * điều kiện tiên quyết: (quantity - reservedQuantity) >= quantityToReserve.
     * 
     * @param em                EntityManager hiện tại (để phối hợp cùng transaction
     *                          của caller)
     * @param variantId         ID biến thể cần giữ chỗ
     * @param quantityToReserve Số lượng cần giữ chỗ
     * @return true nếu giữ chỗ thành công, false nếu không đủ hàng khả dụng
     */
    public boolean deductStock(
            EntityManager em, int variantId, int quantity) {

        if (em == null || variantId <= 0 || quantity <= 0) {
            return false;
        }

        int updatedCount = em.createQuery(
                "UPDATE ProductVariant v "
                        + "SET v.quantity = v.quantity - :qty "
                        + "WHERE v.variantId = :variantId "
                        + "AND v.quantity >= :qty ")
                .setParameter("qty", quantity)
                .setParameter("variantId", variantId)
                .executeUpdate();

        return updatedCount == 1;
    }

}