package com.mycompany.sportshopweb.dao;

import com.mycompany.sportshopweb.model.Album;
import com.mycompany.sportshopweb.util.DBUtil;

/**
 * Data Access Object hỗ trợ các tác vụ quản trị Album cho Admin.
 */
public class AdminAlbumDAO {

    /**
     * Xóa bộ nhớ cache L2 của Album để đảm bảo dữ liệu mới nhất được tải lại.
     */
    public static void evictCache() {
        try {
            DBUtil.getEmFactory().getCache().evict(Album.class);
        } catch (Exception ignored) {
        }
    }
}
