package com.sportshop.dao;

import com.sportshop.model.Product;
import com.sportshop.model.ProductImage;
import com.sportshop.model.ProductVariant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProductDAO — Data Access Object cho sản phẩm (kết nối JDBC MySQL).
 */
public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final ProductDAO INSTANCE = new ProductDAO();

    private ProductDAO() {
    }

    public static ProductDAO getInstance() {
        return INSTANCE;
    }

    /**
     * Lấy danh sách sản phẩm nổi bật (Featured Products)
     * Trả về danh sách rỗng nếu DB chưa có dữ liệu
     */
    public List<Product> findFeatured(int limit) {
        Connection conn = DBContext.getConnection();
        if (conn == null) return Collections.emptyList();

        String sql = "SELECT p.id, p.name, p.slug, p.category_id, p.brand_id, p.price, p.sale_price, " +
                     "p.thumbnail_url, p.description, p.is_featured, p.is_new, p.active, p.created_at, " +
                     "c.name AS category_name, b.name AS brand_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN brands b ON p.brand_id = b.id " +
                     "WHERE p.active = 1 AND p.is_featured = 1 " +
                     "ORDER BY p.id DESC LIMIT ?";

        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn ProductDAO.findFeatured: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            DBContext.close(conn);
        }
        return list;
    }

    /**
     * Lấy danh sách sản phẩm mới nhất (New Arrivals)
     */
    public List<Product> findNewArrivals(int limit) {
        Connection conn = DBContext.getConnection();
        if (conn == null) return Collections.emptyList();

        String sql = "SELECT p.id, p.name, p.slug, p.category_id, p.brand_id, p.price, p.sale_price, " +
                     "p.thumbnail_url, p.description, p.is_featured, p.is_new, p.active, p.created_at, " +
                     "c.name AS category_name, b.name AS brand_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN brands b ON p.brand_id = b.id " +
                     "WHERE p.active = 1 " +
                     "ORDER BY p.created_at DESC, p.id DESC LIMIT ?";

        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn ProductDAO.findNewArrivals: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            DBContext.close(conn);
        }
        return list;
    }

    /**
     * Tìm kiếm và lọc sản phẩm có phân trang
     */
    public List<Product> findFiltered(Long categoryId, Long brandId, String keyword, String sort, int offset, int limit) {
        Connection conn = DBContext.getConnection();
        if (conn == null) return Collections.emptyList();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name, p.slug, p.category_id, p.brand_id, p.price, p.sale_price, " +
                "p.thumbnail_url, p.description, p.is_featured, p.is_new, p.active, p.created_at, " +
                "c.name AS category_name, b.name AS brand_name " +
                "FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "LEFT JOIN brands b ON p.brand_id = b.id " +
                "WHERE p.active = 1 "
        );

        List<Object> params = new ArrayList<>();
        if (categoryId != null) {
            sql.append("AND (p.category_id = ? OR c.parent_id = ?) ");
            params.add(categoryId);
            params.add(categoryId);
        }
        if (brandId != null) {
            sql.append("AND p.brand_id = ? ");
            params.add(brandId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.description LIKE ?) ");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
        }

        // Sắp xếp
        if ("price_asc".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY COALESCE(p.sale_price, p.price) ASC ");
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY COALESCE(p.sale_price, p.price) DESC ");
        } else {
            sql.append("ORDER BY p.id DESC ");
        }

        sql.append("LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn ProductDAO.findFiltered: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            DBContext.close(conn);
        }
        return list;
    }

    /**
     * Đếm tổng số sản phẩm thỏa mãn điều kiện lọc
     */
    public long countFiltered(Long categoryId, Long brandId, String keyword) {
        Connection conn = DBContext.getConnection();
        if (conn == null) return 0;

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(p.id) FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "WHERE p.active = 1 "
        );

        List<Object> params = new ArrayList<>();
        if (categoryId != null) {
            sql.append("AND (p.category_id = ? OR c.parent_id = ?) ");
            params.add(categoryId);
            params.add(categoryId);
        }
        if (brandId != null) {
            sql.append("AND p.brand_id = ? ");
            params.add(brandId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.description LIKE ?) ");
            String kw = "%" + keyword.trim() + "%";
            params.add(kw);
            params.add(kw);
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi đếm ProductDAO.countFiltered: " + e.getMessage());
        } finally {
            DBContext.close(conn);
        }
        return 0;
    }

    /**
     * Tìm chi tiết sản phẩm theo ID kèm Variants và Images
     */
    public Product findById(Long id) {
        if (id == null) return null;
        Connection conn = DBContext.getConnection();
        if (conn == null) return null;

        String sql = "SELECT p.id, p.name, p.slug, p.category_id, p.brand_id, p.price, p.sale_price, " +
                     "p.thumbnail_url, p.description, p.is_featured, p.is_new, p.active, p.created_at, " +
                     "c.name AS category_name, b.name AS brand_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "LEFT JOIN brands b ON p.brand_id = b.id " +
                     "WHERE p.id = ? AND p.active = 1";

        Product product = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn ProductDAO.findById: " + e.getMessage());
            return null;
        }

        if (product != null) {
            product.setVariants(findVariantsByProductId(product.getId(), conn));
            product.setImages(findImagesByProductId(product.getId(), conn));
        }

        DBContext.close(conn);
        return product;
    }

    private List<ProductVariant> findVariantsByProductId(Long productId, Connection conn) {
        String sql = "SELECT id, product_id, sku, size, color, price, stock_quantity, active " +
                     "FROM product_variants WHERE product_id = ? AND active = 1";
        List<ProductVariant> variants = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductVariant v = new ProductVariant();
                    v.setId(rs.getLong("id"));
                    v.setProductId(rs.getLong("product_id"));
                    v.setSku(rs.getString("sku"));
                    v.setSize(rs.getString("size"));
                    v.setColor(rs.getString("color"));
                    v.setPrice(rs.getBigDecimal("price"));
                    v.setStockQuantity(rs.getInt("stock_quantity"));
                    v.setActive(rs.getBoolean("active"));
                    variants.add(v);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn ProductDAO.findVariantsByProductId: " + e.getMessage());
        }
        return variants;
    }

    private List<ProductImage> findImagesByProductId(Long productId, Connection conn) {
        String sql = "SELECT id, product_id, image_url, is_primary, display_order " +
                     "FROM product_images WHERE product_id = ? ORDER BY display_order ASC";
        List<ProductImage> images = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductImage img = new ProductImage();
                    img.setId(rs.getLong("id"));
                    img.setProductId(rs.getLong("product_id"));
                    img.setImageUrl(rs.getString("image_url"));
                    img.setIsPrimary(rs.getBoolean("is_primary"));
                    img.setDisplayOrder(rs.getInt("display_order"));
                    images.add(img);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn ProductDAO.findImagesByProductId: " + e.getMessage());
        }
        return images;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setSlug(rs.getString("slug"));
        p.setCategoryId(rs.getLong("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setBrandId(rs.getLong("brand_id"));
        p.setBrandName(rs.getString("brand_name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setSalePrice(rs.getBigDecimal("sale_price"));
        p.setThumbnailUrl(rs.getString("thumbnail_url"));
        p.setDescription(rs.getString("description"));
        p.setFeatured(rs.getBoolean("is_featured"));
        p.setIsNew(rs.getBoolean("is_new"));
        p.setActive(rs.getBoolean("active"));
        return p;
    }
}
