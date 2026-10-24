package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.ProductDAO;
import com.mycompany.sportshopweb.model.Brand;
import com.mycompany.sportshopweb.model.Category;
import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductVariant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "AdminProductServlet", urlPatterns = { "/admin/products" })
public class AdminProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    private static final int PAGE_SIZE = 10;

    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // Nhận tham số lọc và phân trang
            String keyword = request.getParameter("keyword");
            String brandIdParam = request.getParameter("brandId");
            String categoryIdParam = request.getParameter("categoryId");
            String pageParam = request.getParameter("page");

            int brandId = parseInt(brandIdParam, 0);
            int categoryId = parseInt(categoryIdParam, 0);
            int requestedPage = Math.max(1, parseInt(pageParam, 1));

            // Lấy danh sách sản phẩm từ database
            List<Product> allProducts = productDAO.getAllProducts();

            if (allProducts == null) {
                allProducts = new ArrayList<>();
            }

            // Lọc theo tên, thương hiệu và danh mục
            List<Product> filteredProducts = new ArrayList<>();

            String normalizedKeyword = keyword == null
                    ? ""
                    : keyword.trim().toLowerCase(Locale.ROOT);

            List<Brand> brands = new ArrayList<>();
            List<Category> categories = new ArrayList<>();

            for (Product product : allProducts) {
                if (product == null) {
                    continue;
                }

                String productName = product.getProductName() == null
                        ? ""
                        : product.getProductName().toLowerCase(Locale.ROOT);

                boolean matchesKeyword = normalizedKeyword.isEmpty()
                        || productName.contains(normalizedKeyword);

                int productBrandId = product.getBrand() == null
                        ? 0
                        : product.getBrand().getBrandId();

                int productCategoryId = product.getCategory() == null
                        ? 0
                        : product.getCategory().getCategoryId();

                boolean matchesBrand = brandId <= 0
                        || productBrandId == brandId;

                boolean matchesCategory = categoryId <= 0
                        || productCategoryId == categoryId;

                if (matchesKeyword && matchesBrand && matchesCategory) {
                    filteredProducts.add(product);
                }

                // Thu thập thương hiệu và danh mục để hiển thị bộ lọc
                if (product.getBrand() != null
                        && !containsBrandId(
                                brands, product.getBrand().getBrandId())) {
                    brands.add(product.getBrand());
                }

                if (product.getCategory() != null
                        && !containsCategoryId(
                                categories, product.getCategory().getCategoryId())) {
                    categories.add(product.getCategory());
                }
            }

            // Phân trang danh sách sản phẩm
            int totalProducts = filteredProducts.size();

            int totalPages = Math.max(
                    1,
                    (int) Math.ceil((double) totalProducts / PAGE_SIZE));

            int currentPage = Math.min(requestedPage, totalPages);

            int fromIndex = (currentPage - 1) * PAGE_SIZE;
            int toIndex = Math.min(fromIndex + PAGE_SIZE, totalProducts);

            List<Product> products = new ArrayList<>(
                    filteredProducts.subList(fromIndex, toIndex));

            // Thiết lập dữ liệu thuộc tính cho JSP
            request.setAttribute("products", products);
            request.setAttribute("keyword", keyword == null ? "" : keyword);
            request.setAttribute("selectedBrandId", brandId);
            request.setAttribute("selectedCategoryId", categoryId);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("brands", brands);
            request.setAttribute("categories", categories);

            // Xác định sản phẩm đang được quản lý biến thể
            int manageVariantsId = parseInt(
                    request.getParameter("manageVariants"), 0);

            request.setAttribute("manageVariants", manageVariantsId);
            request.setAttribute(
                    "variantMessage",
                    request.getParameter("variantMessage"));

            if (manageVariantsId > 0) {
                boolean productExists = false;

                for (Product product : allProducts) {
                    if (product != null
                            && product.getProductId() == manageVariantsId) {
                        productExists = true;
                        break;
                    }
                }

                if (productExists) {
                    List<ProductVariant> managedVariants = ProductDAO.getVariantsByProductId(manageVariantsId);

                    request.setAttribute(
                            "managedVariants",
                            managedVariants == null
                                     ? new ArrayList<ProductVariant>()
                                    : managedVariants);
                } else {
                    request.setAttribute(
                            "managedVariants",
                            new ArrayList<ProductVariant>());
                    request.setAttribute("variantMessage", "invalidProduct");
                }
            }

            // Chuyển tiếp đến trang quản lý sản phẩm
            request.getRequestDispatcher("/views/admin/products.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                    "Unable to load admin product page.", e);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int productId = parseInt(
                    request.getParameter("productId"), -1);

            if (productId <= 0) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/products?deleteError=1");
                return;
            }

            try {
                boolean success = productDAO.softDeleteProduct(productId);

                if (success) {
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/admin/products?deleteSuccess=1");
                } else {
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/admin/products?deleteError=1");
                }

            } catch (Exception e) {
                throw new ServletException(
                        "Unable to delete product.", e);
            }

            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/products");
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean containsBrandId(List<Brand> brands, int id) {
        for (Brand brand : brands) {
            if (brand.getBrandId() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean containsCategoryId(List<Category> categories, int id) {
        for (Category category : categories) {
            if (category.getCategoryId() == id) {
                return true;
            }
        }
        return false;
    }
}
