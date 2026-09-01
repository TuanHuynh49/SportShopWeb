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
 * AdminProductServlet — Controller Quản lý sản phẩm cho Quản trị viên (REQ-A02).
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/product", "/admin/product/new", "/admin/product/edit"})
public class AdminProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String PRODUCT_LIST_VIEW = "/WEB-INF/views/admin/product-list.jsp";
    private static final String PRODUCT_FORM_VIEW = "/WEB-INF/views/admin/product-form.jsp";

    private final ProductDAO productDAO = ProductDAO.getInstance();
    private final CategoryDAO categoryDAO = CategoryDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/product/new".equals(path) || "/admin/product/edit".equals(path)) {
            // Form thêm/sửa sản phẩm
            List<Category> categories = categoryDAO.findAllTree();
            request.setAttribute(AttributeNames.CATEGORIES, categories);
            request.setAttribute("currentTab", "product");
            request.setAttribute(AttributeNames.PAGE_TITLE, "/admin/product/new".equals(path) ? "Thêm Sản Phẩm Mới" : "Chỉnh Sửa Sản Phẩm");
            request.setAttribute(AttributeNames.VIEW_PATH, PRODUCT_FORM_VIEW);
            request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
            return;
        }

        // Danh sách sản phẩm quản trị
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try { page = Math.max(1, Integer.parseInt(pageParam.trim())); } catch (NumberFormatException ignored) {}
        }

        String keyword = request.getParameter("keyword");
        List<Product> products = productDAO.findFiltered(null, null, keyword, null, (page - 1) * 15, 15);
        long totalCount = productDAO.countFiltered(null, null, keyword);

        request.setAttribute("products", products);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPage", page);
        request.setAttribute("currentTab", "product");
        request.setAttribute(AttributeNames.PAGE_TITLE, "Quản Lý Sản Phẩm");
        request.setAttribute(AttributeNames.VIEW_PATH, PRODUCT_LIST_VIEW);

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
