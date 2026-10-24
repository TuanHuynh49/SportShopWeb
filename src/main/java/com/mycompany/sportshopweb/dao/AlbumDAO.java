/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Album;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Album (Lookbook / Bộ sưu tập phối đồ)
 * @author Anh Tuan
 */
public class AlbumDAO {

    /**
     * Lấy danh sách tất cả các Album Lookbook
     */
    public static List<Album> getAllAlbums() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            TypedQuery<Album> query = em.createQuery(
                "SELECT a FROM Album a ORDER BY a.albumId ASC",
                Album.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    /**
     * Lấy chi tiết 1 Album theo ID kèm danh sách ProductVariant, Product và hình ảnh
     */
    public static Album getAlbumById(int albumId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            TypedQuery<Album> query = em.createQuery(
                "SELECT DISTINCT a FROM Album a LEFT JOIN FETCH a.items WHERE a.albumId = :id",
                Album.class
            );
            query.setParameter("id", albumId);
            List<Album> list = query.getResultList();
            if (list.isEmpty()) {
                return null;
            }
            Album album = list.get(0);
            if (album.getItems() != null) {
                for (ProductVariant v : album.getItems()) {
                    if (v.getProduct() != null && v.getProduct().getImages() != null) {
                        v.getProduct().getImages().size();
                    }
                }
            }
            return album;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Thêm mới Album
     */
    public static boolean insert(Album album) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(album);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Cập nhật Album
     */
    public static boolean update(Album album) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(album);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Xóa Album theo ID
     */
    public static boolean delete(int albumId) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Album album = em.find(Album.class, albumId);
            if (album != null) {
                em.remove(album);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Thêm mới Album kèm danh sách các biến thể trong set
     */
    public static boolean insertWithVariants(Album album, List<Integer> variantIds) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            album.setItems(loadVariants(em, variantIds));
            em.persist(album);
            tx.commit();

            evictAlbumCache(em);
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Cập nhật Album kèm danh sách các biến thể trong set
     */
    public static boolean updateWithVariants(int albumId, String name, String description, String imageUrl, List<Integer> variantIds) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Album existing = em.find(Album.class, albumId);
            if (existing == null) {
                tx.rollback();
                return false;
            }

            existing.setName(name);
            existing.setDescription(description);
            if (imageUrl != null && !imageUrl.isBlank()) {
                existing.setImageUrl(imageUrl);
            }

            existing.setItems(loadVariants(em, variantIds));
            tx.commit();

            evictAlbumCache(em);
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // Nạp danh sách ProductVariant từ cơ sở dữ liệu dựa trên danh sách ID (Áp dụng DRY)
    private static List<ProductVariant> loadVariants(EntityManager em, List<Integer> variantIds) {
        List<ProductVariant> variants = new ArrayList<>();
        if (variantIds != null && !variantIds.isEmpty()) {
            for (Integer vid : variantIds) {
                if (vid != null && vid > 0) {
                    ProductVariant v = em.find(ProductVariant.class, vid);
                    if (v != null) {
                        variants.add(v);
                    }
                }
            }
        }
        return variants;
    }

    // Xóa bộ nhớ cache của Album để làm mới dữ liệu
    private static void evictAlbumCache(EntityManager em) {
        try {
            em.getEntityManagerFactory().getCache().evict(Album.class);
        } catch (Exception ignored) {
        }
    }
}
