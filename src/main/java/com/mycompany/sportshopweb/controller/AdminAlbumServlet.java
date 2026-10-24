package com.mycompany.sportshopweb.controller;

import com.cloudinary.utils.ObjectUtils;
import com.mycompany.sportshopweb.dao.AlbumDAO;
import com.mycompany.sportshopweb.dao.ProductVariantDAO;
import com.mycompany.sportshopweb.model.Album;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.util.CloudinaryConfig;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminAlbumServlet", urlPatterns = { "/admin/albums" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 10 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class AdminAlbumServlet extends HttpServlet {

    private final ProductVariantDAO variantDAO = new ProductVariantDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            List<ProductVariant> variantList = variantDAO.getAllVariantsWithProducts();
            request.setAttribute("variantList", variantList);
            request.setAttribute("album", new Album());
            request.setAttribute("editMode", false);
            request.getRequestDispatcher("/views/admin/albums-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(action)) {
            int albumId = parseInt(request.getParameter("albumId"), 0);
            if (albumId <= 0) {
                response.sendRedirect(request.getContextPath() + "/admin/albums");
                return;
            }

            Album album = AlbumDAO.getAlbumById(albumId);
            if (album == null) {
                response.sendRedirect(request.getContextPath() + "/admin/albums?error=notfound");
                return;
            }

            List<ProductVariant> variantList = variantDAO.getAllVariantsWithProducts();
            request.setAttribute("variantList", variantList);
            request.setAttribute("album", album);
            request.setAttribute("editMode", true);
            request.getRequestDispatcher("/views/admin/albums-form.jsp").forward(request, response);
            return;
        }

        // Mặc định: danh sách Album
        List<Album> albums = AlbumDAO.getAllAlbums();
        request.setAttribute("albums", albums);
        request.getRequestDispatcher("/views/admin/albums.jsp").forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            handleCreate(request, response);
            return;
        }

        if ("update".equals(action)) {
            handleUpdate(request, response);
            return;
        }

        if ("delete".equals(action)) {
            handleDelete(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/admin/albums");
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] variantIdParams = request.getParameterValues("variantIds");

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên Album / Lookbook không được để trống.");
            request.setAttribute("variantList", variantDAO.getAllVariantsWithProducts());
            request.setAttribute("album", new Album());
            request.setAttribute("editMode", false);
            request.getRequestDispatcher("/views/admin/albums-form.jsp").forward(request, response);
            return;
        }

        // Upload ảnh bìa lên Cloudinary (thư mục "Album")
        String uploadedUrl = uploadImageToCloudinary(request.getPart("imageFile"));

        Album album = new Album();
        album.setName(name.trim());
        album.setDescription(description != null ? description.trim() : "");
        album.setImageUrl(uploadedUrl != null ? uploadedUrl : "");

        List<Integer> variantIds = parseIntegerList(variantIdParams);

        boolean success = AlbumDAO.insertWithVariants(album, variantIds);

        if (success) {
            refreshAppContextAlbums();
            response.sendRedirect(request.getContextPath() + "/admin/albums?createSuccess=1");
        } else {
            request.setAttribute("error", "Thêm Album thất bại. Vui lòng thử lại.");
            request.setAttribute("variantList", variantDAO.getAllVariantsWithProducts());
            request.setAttribute("album", album);
            request.setAttribute("editMode", false);
            request.getRequestDispatcher("/views/admin/albums-form.jsp").forward(request, response);
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int albumId = parseInt(request.getParameter("albumId"), 0);
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String existingImageUrl = request.getParameter("existingImageUrl");
        String[] variantIdParams = request.getParameterValues("variantIds");

        if (albumId <= 0 || name == null || name.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/albums?error=invalid");
            return;
        }

        // Nếu người dùng chọn tải lên ảnh bìa mới
        String newUploadedUrl = uploadImageToCloudinary(request.getPart("imageFile"));
        String finalImageUrl = (newUploadedUrl != null && !newUploadedUrl.isBlank())
                ? newUploadedUrl
                : existingImageUrl;

        List<Integer> variantIds = parseIntegerList(variantIdParams);

        boolean success = AlbumDAO.updateWithVariants(
                albumId,
                name.trim(),
                description != null ? description.trim() : "",
                finalImageUrl,
                variantIds);

        if (success) {
            refreshAppContextAlbums();
            response.sendRedirect(request.getContextPath() + "/admin/albums?updateSuccess=1");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/albums?error=updateFailed");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int albumId = parseInt(request.getParameter("albumId"), 0);
        if (albumId > 0) {
            boolean deleted = AlbumDAO.delete(albumId);
            if (deleted) {
                try {
                    DBUtil.getEmFactory().getCache().evict(Album.class);
                } catch (Exception ignored) {
                }
                refreshAppContextAlbums();
                response.sendRedirect(request.getContextPath() + "/admin/albums?deleteSuccess=1");
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/albums?deleteError=1");
    }

    private void refreshAppContextAlbums() {
        try {
            DBUtil.getEmFactory().getCache().evict(Album.class);
            List<Album> refreshedList = AlbumDAO.getAllAlbums();
            getServletContext().setAttribute("albumList", refreshedList);
            getServletContext().setAttribute("globalAlbumList", refreshedList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String uploadImageToCloudinary(Part part) {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        String contentType = part.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            return null;
        }

        try {
            byte[] fileBytes = part.getInputStream().readAllBytes();
            if (fileBytes.length == 0) {
                return null;
            }
            Map<?, ?> uploadResult = CloudinaryConfig.getInstance().uploader().upload(
                    fileBytes,
                    ObjectUtils.asMap(
                            "folder", "Album",
                            "resource_type", "image"
                    )
            );
            if (uploadResult != null && uploadResult.get("secure_url") != null) {
                return uploadResult.get("secure_url").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int parseInt(String val, int defaultVal) {
        if (val == null) return defaultVal;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private List<Integer> parseIntegerList(String[] array) {
        List<Integer> list = new ArrayList<>();
        if (array != null) {
            for (String s : array) {
                int id = parseInt(s, 0);
                if (id > 0) {
                    list.add(id);
                }
            }
        }
        return list;
    }
}
