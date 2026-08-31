package com.sportshop.controller;

import com.sportshop.dao.CategoryDAO;
import com.sportshop.dao.ProductDAO;
import com.sportshop.model.Category;
import com.sportshop.model.Product;
import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * ProductListServlet — Controller hiển thị danh sách sản phẩm, lọc & phân trang (MVC).
 */
@WebServlet(name = "ProductListServlet", urlPatterns = {"/product/list", "/products"})
public class ProductListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LAYOUT_VIEW = "/WEB-INF/views/common/layout-customer.jsp";
    private static final String PRODUCT_LIST_VIEW = "/WEB-INF/views/customer/product-list.jsp";
    private static final int PAGE_SIZE = 12;

    private final ProductDAO productDAO = ProductDAO.getInstance();
    private final CategoryDAO categoryDAO = CategoryDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Đọc tham số lọc
        String catParam = request.getParameter("categoryId");
        Long categoryId = null;
        if (catParam != null && !catParam.trim().isEmpty()) {
            try {
                categoryId = Long.parseLong(catParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        String brandParam = request.getParameter("brandId");
        Long brandId = null;
        if (brandParam != null && !brandParam.trim().isEmpty()) {
            try {
                brandId = Long.parseLong(brandParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        String keyword = request.getParameter("keyword");
        String sort = request.getParameter("sort");

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageParam.trim()));
            } catch (NumberFormatException ignored) {}
        }

        int offset = (page - 1) * PAGE_SIZE;

        // 2. Truy vấn dữ liệu động từ DAO
        List<Product> productList = productDAO.findFiltered(categoryId, brandId, keyword, sort, offset, PAGE_SIZE);
        long totalCount = productDAO.countFiltered(categoryId, brandId, keyword);
        int totalPage = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        List<Category> categories = categoryDAO.findAllTree();

        // 3. Set Request Attributes
        request.setAttribute(AttributeNames.PRODUCT_LIST, productList);
        request.setAttribute(AttributeNames.TOTAL_COUNT, totalCount);
        request.setAttribute(AttributeNames.CURRENT_PAGE, page);
        request.setAttribute(AttributeNames.TOTAL_PAGE, totalPage);
        request.setAttribute(AttributeNames.CATEGORIES, categories);

        request.setAttribute(AttributeNames.PAGE_TITLE, "Danh Sách Sản Phẩm");
        request.setAttribute(AttributeNames.VIEW_PATH, PRODUCT_LIST_VIEW);

        // 4. Forward tới Layout chung
        request.getRequestDispatcher(LAYOUT_VIEW).forward(request, response);
    }
}
