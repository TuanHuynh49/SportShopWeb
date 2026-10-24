
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Brand;
import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductImage;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    public static final int PAGE_SIZE = 20;

    public static long countProducts() {
        return countProducts("", 0);
    }

    public static long countProducts(String keyword, int categoryId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT COUNT(p) FROM Product p WHERE 1 = 1";

            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql += " AND LOWER(p.productName) LIKE :keyword";
            }

            if (categoryId > 0) {
                jpql += " AND p.category.categoryId = :categoryId";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter(
                        "keyword",
                        "%" + keyword.trim().toLowerCase() + "%");
            }

            if (categoryId > 0) {
                query.setParameter("categoryId", categoryId);
            }

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public static List<Product> getProductsByPage(int page, int pageSize) {
        return getProductsByPage(page, pageSize, "", 0);
    }

    public static List<Product> getProductsByPage(
            int page,
            int pageSize,
            String keyword,
            int categoryId) {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            page = Math.max(page, 1);
            pageSize = Math.max(pageSize, 1);

            String jpql = "SELECT p.productId FROM Product p WHERE 1 = 1";

            if (keyword != null && !keyword.trim().isEmpty()) {
                jpql += " AND LOWER(p.productName) LIKE :keyword";
            }

            if (categoryId > 0) {
                jpql += " AND p.category.categoryId = :categoryId";
            }

            jpql += " ORDER BY p.productId DESC";

            TypedQuery<Integer> idQuery = em.createQuery(jpql, Integer.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                idQuery.setParameter(
                        "keyword",
                        "%" + keyword.trim().toLowerCase() + "%");
            }

            if (categoryId > 0) {
                idQuery.setParameter("categoryId", categoryId);
            }

            List<Integer> ids = idQuery
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

            if (ids.isEmpty()) {
                return new ArrayList<>();
            }

            return em.createQuery(
                    "SELECT DISTINCT p FROM Product p "
                            + "LEFT JOIN FETCH p.images "
                            + "LEFT JOIN FETCH p.brand "
                            + "LEFT JOIN FETCH p.category "
                            + "WHERE p.productId IN :ids "
                            + "ORDER BY p.productId DESC",
                    Product.class)
                    .setParameter("ids", ids)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    /**
     * Tạo Product cùng một transaction.
     * Giữ lại để tương thích với luồng tạo Product cũ.
     */
    public void insert(Product product, int brandId, int categoryId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            product.setBrand(em.getReference(Brand.class, brandId));
            product.setCategory(em.getReference(Category.class, categoryId));

            em.persist(product);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Tạo Product và ProductVariant trong cùng transaction.
     * Nếu lưu variant thất bại, việc tạo Product cũng bị rollback.
     */
    public int insertWithVariant(
            Product product,
            int brandId,
            int categoryId,
            String variantName,
            String size,
            String color,
            long price,
            int quantity) {

        if (product == null) {
            throw new IllegalArgumentException("Product không được null.");
        }

        if (brandId <= 0 || categoryId <= 0) {
            throw new IllegalArgumentException(
                    "Brand và Category phải hợp lệ.");
        }

        if (price < 0 || quantity < 0) {
            throw new IllegalArgumentException(
                    "Giá và tồn kho không được âm.");
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            product.setBrand(em.getReference(Brand.class, brandId));
            product.setCategory(em.getReference(Category.class, categoryId));

            em.persist(product);
            em.flush();

            if (product.getImages() != null && !product.getImages().isEmpty()) {
                for (ProductImage img : product.getImages()) {
                    img.setProduct(product);
                    em.persist(img);
                }
            }
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setVariantName(variantName);
            variant.setSize(size);
            variant.setColor(color);
            variant.setPrice(price);
            variant.setQuantity(quantity);

            em.persist(variant);

            tx.commit();

            try {
                em.getEntityManagerFactory().getCache().evict(Product.class);
                em.getEntityManagerFactory().getCache().evict(ProductImage.class);
                em.getEntityManagerFactory().getCache().evict(ProductVariant.class);
            } catch (Exception ignored) {
            }

            return product.getProductId();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Cập nhật thông tin cơ bản của Product.
     * Tồn kho được quản lý riêng ở ProductVariant.
     */
    public boolean updateProduct(
            int productId,
            String productName,
            String description,
            int brandId,
            int categoryId) {
        return updateProduct(productId, productName, description, brandId, categoryId, null, null);
    }

    public boolean updateProduct(
            int productId,
            String productName,
            String description,
            int brandId,
            int categoryId,
            List<ProductImage> newImages,
            List<Integer> deleteImageIds) {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Product product = em.find(Product.class, productId);

            if (product == null) {
                tx.rollback();
                return false;
            }

            product.setProductName(productName);
            product.setDescription(description);
            product.setBrand(em.getReference(Brand.class, brandId));
            product.setCategory(em.getReference(Category.class, categoryId));

            // Đảm bảo list images của product được load vào memory
            if (product.getImages() == null) {
                product.setImages(new ArrayList<>());
            } else {
                product.getImages().size(); // force lazy load
            }

            // 1. Xóa các ảnh được chỉ định
            if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
                // Xóa khỏi danh sách in-memory của product trước
                product.getImages().removeIf(img -> deleteImageIds.contains(img.getProductImageId()));

                // Xóa trực tiếp trong database bằng JPQL
                em.createQuery(
                        "DELETE FROM ProductImage pi WHERE pi.productImageId IN :delIds AND pi.product.productId = :pid")
                        .setParameter("delIds", deleteImageIds)
                        .setParameter("pid", productId)
                        .executeUpdate();
            }

            // 2. Thêm các ảnh mới được tải lên
            if (newImages != null && !newImages.isEmpty()) {
                int currentOrder = product.getImages().size();
                boolean hasExistingImages = !product.getImages().isEmpty();

                for (int i = 0; i < newImages.size(); i++) {
                    ProductImage img = newImages.get(i);
                    img.setProduct(product);
                    img.setSortOrder(currentOrder + i);
                    if (!hasExistingImages && i == 0) {
                        img.setPrimary(true);
                    } else {
                        img.setPrimary(false);
                    }
                    em.persist(img);
                    product.getImages().add(img);
                }
            }

            // 3. Đảm bảo luôn có 1 ảnh chính nếu sản phẩm còn ảnh
            if (!product.getImages().isEmpty()) {
                boolean hasPrimary = false;
                for (ProductImage img : product.getImages()) {
                    if (img.isPrimary()) {
                        hasPrimary = true;
                        break;
                    }
                }
                if (!hasPrimary) {
                    product.getImages().get(0).setPrimary(true);
                }
            }

            tx.commit();

            // Xóa cache EclipseLink để đảm bảo các query tiếp theo load dữ liệu mới nhất
            try {
                em.getEntityManagerFactory().getCache().evict(Product.class, productId);
                em.getEntityManagerFactory().getCache().evict(ProductImage.class);
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

    /**
     * Đặt tồn kho tất cả variant của Product về 0.
     * Không xóa Product khỏi database.
     */
    public boolean softDeleteProduct(int productId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Product product = em.find(Product.class, productId);

            if (product == null) {
                tx.rollback();
                return false;
            }

            em.createQuery(
                    "UPDATE ProductVariant v "
                            + "SET v.quantity = 0 "
                            + "WHERE v.product.productId = :productId")
                    .setParameter("productId", productId)
                    .executeUpdate();

            tx.commit();

            try {
                em.getEntityManagerFactory().getCache().evict(Product.class, productId);
                em.getEntityManagerFactory().getCache().evict(ProductVariant.class);
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

    public List<Product> getAllProducts() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT p FROM Product p "
                            + "LEFT JOIN FETCH p.images "
                            + "LEFT JOIN FETCH p.brand "
                            + "LEFT JOIN FETCH p.category",
                    Product.class)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public static Product getProductById(int id) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            List<Product> products = em.createQuery(
                    "SELECT DISTINCT p FROM Product p "
                            + "LEFT JOIN FETCH p.images "
                            + "LEFT JOIN FETCH p.brand "
                            + "LEFT JOIN FETCH p.category "
                            + "WHERE p.productId = :id",
                    Product.class)
                    .setParameter("id", id)
                    .getResultList();

            return products.isEmpty() ? null : products.get(0);

        } finally {
            em.close();
        }
    }

    public static List<ProductVariant> getVariantsByProductId(int productId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            return em.createQuery(
                    "SELECT v FROM ProductVariant v "
                            + "JOIN FETCH v.product "
                            + "WHERE v.product.productId = :id "
                            + "ORDER BY v.price, v.size",
                    ProductVariant.class)
                    .setParameter("id", productId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public static ProductVariant getVariantById(int variantId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            List<ProductVariant> variants = em.createQuery(
                    "SELECT v FROM ProductVariant v "
                            + "JOIN FETCH v.product "
                            + "WHERE v.variantId = :variantId",
                    ProductVariant.class)
                    .setParameter("variantId", variantId)
                    .getResultList();

            return variants.isEmpty() ? null : variants.get(0);

        } finally {
            em.close();
        }
    }

    public static long countFiltered(
            int brandId,
            long catId,
            long minPrice,
            long maxPrice,
            String keyword) {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            Long total = em.createQuery(
                    "SELECT COUNT(DISTINCT v.product.productId) "
                            + "FROM ProductVariant v "
                            + "WHERE (:brandId = 0 "
                            + "OR v.product.brand.brandId = :brandId) "
                            + "AND (:categoryId = 0 "
                            + "OR v.product.category.categoryId = :categoryId) "
                            + "AND v.price >= :minPrice "
                            + "AND v.price <= :maxPrice "
                            + "AND (:keyword IS NULL OR :keyword = '' "
                            + "OR LOWER(v.product.productName) "
                            + "LIKE LOWER(CONCAT('%', :keyword, '%')))",
                    Long.class)
                    .setParameter("brandId", brandId)
                    .setParameter("categoryId", catId)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .setParameter(
                            "keyword",
                            keyword != null ? keyword.trim() : "")
                    .getSingleResult();

            return total != null ? total : 0L;

        } finally {
            em.close();
        }
    }

    public static List<Product> getFiltered(
            int brandId,
            long catId,
            long minPrice,
            long maxPrice,
            String sort,
            int page,
            int pageSize,
            String keyword) {

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            page = Math.max(page, 1);
            pageSize = Math.max(pageSize, 1);

            int offset = (page - 1) * pageSize;

            String jpqlId = "SELECT v.product.productId FROM ProductVariant v "
                    + "WHERE (:brandId = 0 "
                    + "OR v.product.brand.brandId = :brandId) "
                    + "AND (:categoryId = 0 "
                    + "OR v.product.category.categoryId = :categoryId) "
                    + "AND v.price >= :minPrice "
                    + "AND v.price <= :maxPrice "
                    + "AND (:keyword IS NULL OR :keyword = '' "
                    + "OR LOWER(v.product.productName) "
                    + "LIKE LOWER(CONCAT('%', :keyword, '%'))) "
                    + "GROUP BY v.product.productId, v.product.productName ";

            if ("id_desc".equals(sort)) {
                jpqlId += "ORDER BY v.product.productId DESC";
            } else if ("price_asc".equals(sort)) {
                jpqlId += "ORDER BY MIN(v.price) ASC";
            } else if ("price_desc".equals(sort)) {
                jpqlId += "ORDER BY MAX(v.price) DESC";
            } else if ("name_asc".equals(sort)) {
                jpqlId += "ORDER BY v.product.productName ASC";
            } else {
                jpqlId += "ORDER BY v.product.productId ASC";
            }

            TypedQuery<Integer> queryId = em.createQuery(jpqlId, Integer.class)
                    .setParameter("brandId", brandId)
                    .setParameter("categoryId", catId)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .setParameter(
                            "keyword",
                            keyword != null ? keyword.trim() : "")
                    .setFirstResult(offset)
                    .setMaxResults(pageSize);

            List<Integer> productIds = queryId.getResultList();

            if (productIds.isEmpty()) {
                return new ArrayList<>();
            }

            List<Product> unorderedProducts = em.createQuery(
                    "SELECT DISTINCT p FROM Product p "
                            + "LEFT JOIN FETCH p.images "
                            + "LEFT JOIN FETCH p.brand "
                            + "LEFT JOIN FETCH p.category "
                            + "WHERE p.productId IN :productIds",
                    Product.class)
                    .setParameter("productIds", productIds)
                    .getResultList();

            Map<Integer, Product> productMap = new HashMap<>();

            for (Product product : unorderedProducts) {
                productMap.put(product.getProductId(), product);
            }

            List<Product> orderedProducts = new ArrayList<>();

            for (Integer id : productIds) {
                Product product = productMap.get(id);

                if (product != null) {
                    orderedProducts.add(product);
                }
            }

            return orderedProducts;

        } finally {
            em.close();
        }
    }
}
