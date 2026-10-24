
package com.mycompany.sportshopweb.controller;

import com.cloudinary.utils.ObjectUtils;
import com.mycompany.sportshopweb.dao.BrandDAO;
import com.mycompany.sportshopweb.dao.ProductDAO;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductImage;
import com.mycompany.sportshopweb.util.CategoryDB;
import com.mycompany.sportshopweb.util.CloudinaryConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductActionServlet", urlPatterns = { "/ProductActionServlet" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class ProductActionServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
        getServletContext().setAttribute("ProductDAO", productDAO);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            String idStr = request.getParameter("productId");

            int productId;

            try {
                productId = Integer.parseInt(idStr);

                if (productId <= 0) {
                    throw new NumberFormatException("Invalid product ID");
                }
            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid product ID");
                return;
            }

            Product product = ProductDAO.getProductById(productId);

            if (product == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Product not found");
                return;
            }

            request.setAttribute("product", product);
            request.setAttribute("editMode", true);

        } else {
            request.setAttribute("product", new Product());
            request.setAttribute("editMode", false);
        }

        prepareFormData(request);

        request.getRequestDispatcher(
                "/views/admin/products-form.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        boolean isUpdate = "update".equals(action);
        boolean isCreate = "create".equals(action);

        if (!isUpdate && !isCreate) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product action");
            return;
        }

        String name = request.getParameter("productName");
        String description = request.getParameter("description");
        String brandStr = request.getParameter("brandId");
        String categoryStr = request.getParameter("categoryId");

        int productId = 0;
        int brandId;
        int categoryId;

        if (isUpdate) {
            try {
                productId = Integer.parseInt(
                        request.getParameter("productId"));

                if (productId <= 0) {
                    throw new NumberFormatException("Invalid product ID");
                }
            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid product ID");
                return;
            }
        }

        if (name == null || name.isBlank()
                || brandStr == null || brandStr.isBlank()
                || categoryStr == null || categoryStr.isBlank()) {

            request.setAttribute(
                    "error",
                    "Vui lòng nhập đầy đủ tên sản phẩm, thương hiệu và danh mục.");

            showFormAfterError(request, response, isUpdate, productId);
            return;
        }

        try {
            brandId = Integer.parseInt(brandStr.trim());
            categoryId = Integer.parseInt(categoryStr.trim());

            if (brandId <= 0 || categoryId <= 0) {
                throw new NumberFormatException(
                        "Invalid brand/category ID");
            }

        } catch (NumberFormatException e) {
            request.setAttribute(
                    "error",
                    "Thương hiệu hoặc danh mục không hợp lệ.");

            showFormAfterError(request, response, isUpdate, productId);
            return;
        }

        try {
            if (isUpdate) {
                // Đọc danh sách ID ảnh cần xóa (nếu admin chọn)
                String[] deleteIdsArr = request.getParameterValues("deleteImageIds");
                List<Integer> deleteImageIds = new ArrayList<>();
                if (deleteIdsArr != null) {
                    for (String idStr : deleteIdsArr) {
                        try {
                            int id = Integer.parseInt(idStr.trim());
                            if (id > 0) {
                                deleteImageIds.add(id);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }

                Product productRef = new Product();
                productRef.setProductId(productId);
                List<ProductImage> newImages = uploadImages(request, productRef, false);

                boolean updated = productDAO.updateProduct(
                        productId,
                        name.trim(),
                        description,
                        brandId,
                        categoryId,
                        newImages,
                        deleteImageIds);

                if (!updated) {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Product not found");
                    return;
                }

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/products?updateSuccess=1");
                return;
            }

            // Các trường biến thể bắt buộc khi tạo Product.
            String variantName = request.getParameter("variantName");
            String size = request.getParameter("size");
            String color = request.getParameter("color");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");

            if (variantName == null || variantName.isBlank()
                    || size == null || size.isBlank()
                    || color == null || color.isBlank()
                    || priceStr == null || priceStr.isBlank()
                    || quantityStr == null || quantityStr.isBlank()) {

                request.setAttribute(
                        "error",
                        "Vui lòng nhập đầy đủ tên biến thể, size, màu sắc, giá và tồn kho.");

                showFormAfterError(request, response, false, 0);
                return;
            }

            long price;
            int quantity;

            try {
                price = Long.parseLong(priceStr.trim());
                quantity = Integer.parseInt(quantityStr.trim());

                if (price < 0 || quantity < 0) {
                    throw new NumberFormatException(
                            "Price and quantity must be non-negative");
                }
            } catch (NumberFormatException e) {
                request.setAttribute(
                        "error",
                        "Giá và tồn kho phải là số hợp lệ, không được âm.");

                showFormAfterError(request, response, false, 0);
                return;
            }

            Product product = new Product();
            product.setProductName(name.trim());
            product.setDescription(description);

            // Tải lên các file ảnh (nếu có)
            List<ProductImage> images = uploadImages(request, product, true);
            product.setImages(images);

            // Lưu Product và ProductVariant trong cùng transaction.
            productDAO.insertWithVariant(
                    product,
                    brandId,
                    categoryId,
                    variantName.trim(),
                    size.trim(),
                    color.trim(),
                    price,
                    quantity);

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/products?createSuccess=1");

        } catch (Exception e) {
            log("Unable to process product action: " + action, e);

            request.setAttribute(
                    "error",
                    isUpdate
                            ? "Cập nhật sản phẩm thất bại. Vui lòng kiểm tra lại dữ liệu."
                            : "Thêm sản phẩm hoặc biến thể thất bại. Vui lòng kiểm tra lại dữ liệu.");

            showFormAfterError(request, response, isUpdate, productId);
        }
    }

    /**
     * Chuẩn bị danh sách thương hiệu và danh mục cho form.
     */
    private void prepareFormData(HttpServletRequest request) {
        BrandDAO brandDAO = new BrandDAO();
        CategoryDB categoryDAO = new CategoryDB();

        request.setAttribute("brands", brandDAO.getAllBrand());
        request.setAttribute("categories", categoryDAO.getAllCategory());
    }

    private void showFormAfterError(
            HttpServletRequest request,
            HttpServletResponse response,
            boolean isUpdate,
            int productId)
            throws ServletException, IOException {

        Product product;

        if (isUpdate) {
            product = ProductDAO.getProductById(productId);

            if (product == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Product not found");
                return;
            }

            request.setAttribute("editMode", true);
        } else {
            product = new Product();
            request.setAttribute("editMode", false);
        }

        // Giữ lại dữ liệu người dùng vừa nhập.
        String name = request.getParameter("productName");
        String description = request.getParameter("description");

        if (name != null) {
            product.setProductName(name);
        }

        if (description != null) {
            product.setDescription(description);
        }

        request.setAttribute("product", product);
        prepareFormData(request);

        request.getRequestDispatcher(
                "/views/admin/products-form.jsp")
                .forward(request, response);
    }

    private List<ProductImage> uploadImages(HttpServletRequest request, Product product, boolean isCreate)
            throws IOException, ServletException {

        List<ProductImage> images = new ArrayList<>();
        int order = 0;

        for (Part part : request.getParts()) {
            if (!"images".equals(part.getName()) || part.getSize() == 0) {
                continue;
            }

            String contentType = part.getContentType();
            if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
                continue;
            }

            Map<?, ?> result = CloudinaryConfig.getInstance()
                    .uploader()
                    .upload(
                            part.getInputStream().readAllBytes(),
                            ObjectUtils.asMap("folder", "Product"));

            String imageUrl = (String) result.get("secure_url");
            if (imageUrl == null || imageUrl.isBlank()) {
                throw new IOException("Cloudinary did not return a secure image URL.");
            }

            ProductImage image = new ProductImage();
            image.setImageUrl(imageUrl);
            image.setPrimary(isCreate && (order == 0));
            image.setSortOrder(order++);
            image.setCreatedAt(LocalDateTime.now());
            image.setProduct(product);

            images.add(image);
        }

        return images;
    }
}
