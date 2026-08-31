package com.sportshop.dao;

import com.sportshop.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CategoryDAO — Data Access Object cho danh mục sản phẩm (kết nối JDBC MySQL).
 */
public class CategoryDAO {

    private static final Logger LOGGER = Logger.getLogger(CategoryDAO.class.getName());
    private static final CategoryDAO INSTANCE = new CategoryDAO();

    private CategoryDAO() {
    }

    public static CategoryDAO getInstance() {
        return INSTANCE;
    }

    /**
     * Lấy toàn bộ danh mục dạng cây phân cấp (Parent & Subcategories)
     * Trả về danh sách rỗng nếu DB chưa có dữ liệu hoặc chưa kết nối
     */
    public List<Category> findAllTree() {
        Connection conn = DBContext.getConnection();
        if (conn == null) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, name, slug, icon, description, parent_id, display_order, active " +
                     "FROM categories WHERE active = 1 ORDER BY display_order ASC, name ASC";

        List<Category> rootCategories = new ArrayList<>();
        Map<Long, Category> categoryMap = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Category> allList = new ArrayList<>();
            while (rs.next()) {
                Category cat = new Category();
                cat.setId(rs.getLong("id"));
                cat.setName(rs.getString("name"));
                cat.setSlug(rs.getString("slug"));
                cat.setIcon(rs.getString("icon"));
                cat.setDescription(rs.getString("description"));
                long pId = rs.getLong("parent_id");
                cat.setParentId(rs.wasNull() ? null : pId);
                cat.setDisplayOrder(rs.getInt("display_order"));
                cat.setActive(rs.getBoolean("active"));

                categoryMap.put(cat.getId(), cat);
                allList.add(cat);
            }

            // Xây dựng cây phân cấp
            for (Category cat : allList) {
                if (cat.getParentId() == null) {
                    rootCategories.add(cat);
                } else {
                    Category parent = categoryMap.get(cat.getParentId());
                    if (parent != null) {
                        parent.addSubCategory(cat);
                    } else {
                        rootCategories.add(cat);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn CategoryDAO.findAllTree: " + e.getMessage());
            return Collections.emptyList();
        } finally {
            DBContext.close(conn);
        }

        return rootCategories;
    }

    /**
     * Tìm danh mục theo ID
     */
    public Category findById(Long id) {
        if (id == null) return null;
        Connection conn = DBContext.getConnection();
        if (conn == null) return null;

        String sql = "SELECT id, name, slug, icon, description, parent_id, display_order, active " +
                     "FROM categories WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category cat = new Category();
                    cat.setId(rs.getLong("id"));
                    cat.setName(rs.getString("name"));
                    cat.setSlug(rs.getString("slug"));
                    cat.setIcon(rs.getString("icon"));
                    cat.setDescription(rs.getString("description"));
                    long pId = rs.getLong("parent_id");
                    cat.setParentId(rs.wasNull() ? null : pId);
                    cat.setDisplayOrder(rs.getInt("display_order"));
                    cat.setActive(rs.getBoolean("active"));
                    return cat;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Lỗi truy vấn CategoryDAO.findById: " + e.getMessage());
        } finally {
            DBContext.close(conn);
        }
        return null;
    }
}
