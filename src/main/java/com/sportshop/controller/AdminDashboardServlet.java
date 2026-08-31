package com.sportshop.controller;

import com.sportshop.dao.AdminDAO;
import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * AdminDashboardServlet — Controller Dashboard Quản Trị Hệ Thống (MVC Architecture).
 * Truy vấn thống kê thời gian thực từ cơ sở dữ liệu.
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard", "/admin"})
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String DASHBOARD_VIEW = "/WEB-INF/views/admin/dashboard.jsp";

    private final AdminDAO adminDAO = AdminDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Truy vấn thống kê KPIs từ Database
        Map<String, Object> stats = adminDAO.getDashboardStats();
        request.setAttribute("stats", stats);
        request.setAttribute("recentOrders", Collections.emptyList()); // Đọc từ DB khi có đơn
        request.setAttribute("lowStockItems", Collections.emptyList());

        // 2. Thiết lập view attributes
        request.setAttribute(AttributeNames.PAGE_TITLE, "Dashboard Thống Kê");
        request.setAttribute(AttributeNames.VIEW_PATH, DASHBOARD_VIEW);

        // 3. Forward tới Layout Admin chung
        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
