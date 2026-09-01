package com.sportshop.controller;

import com.sportshop.dao.CategoryDAO;
import com.sportshop.model.Category;
import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * AdminCategoryServlet — Controller Quản lý Danh Mục & Thương Hiệu (REQ-A01).
 */
@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/category", "/admin/brand"})
public class AdminCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String CATEGORY_VIEW = "/WEB-INF/views/admin/category-list.jsp";
    private static final String BRAND_VIEW = "/WEB-INF/views/admin/brand-list.jsp";

    private final CategoryDAO categoryDAO = CategoryDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/brand".equals(path)) {
            request.setAttribute("brands", Collections.emptyList());
            request.setAttribute("currentTab", "category");
            request.setAttribute(AttributeNames.PAGE_TITLE, "Quản Lý Thương Hiệu");
            request.setAttribute(AttributeNames.VIEW_PATH, BRAND_VIEW);
        } else {
            List<Category> categories = categoryDAO.findAllTree();
            request.setAttribute(AttributeNames.CATEGORIES, categories);
            request.setAttribute("currentTab", "category");
            request.setAttribute(AttributeNames.PAGE_TITLE, "Quản Lý Danh Mục");
            request.setAttribute(AttributeNames.VIEW_PATH, CATEGORY_VIEW);
        }

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
