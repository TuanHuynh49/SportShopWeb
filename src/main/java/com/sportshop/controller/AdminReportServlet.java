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
 * AdminReportServlet — Controller Báo cáo & Thống kê doanh thu (REQ-A08).
 */
@WebServlet(name = "AdminReportServlet", urlPatterns = {"/admin/report"})
public class AdminReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String REPORT_VIEW = "/WEB-INF/views/admin/report.jsp";

    private final AdminDAO adminDAO = AdminDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> stats = adminDAO.getDashboardStats();
        request.setAttribute("stats", stats);
        request.setAttribute("revenueByMonth", Collections.emptyList());
        request.setAttribute("revenueByCategory", Collections.emptyList());

        request.setAttribute("currentTab", "report");
        request.setAttribute(AttributeNames.PAGE_TITLE, "Báo Cáo Doanh Thu");
        request.setAttribute(AttributeNames.VIEW_PATH, REPORT_VIEW);

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
