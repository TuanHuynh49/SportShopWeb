package com.sportshop.controller;

import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

/**
 * AdminOrderServlet — Controller Quản lý Đơn Hàng Admin (REQ-A04).
 */
@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/order", "/admin/orders", "/admin/order/detail"})
public class AdminOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String ORDER_LIST_VIEW = "/WEB-INF/views/admin/order-list.jsp";
    private static final String ORDER_DETAIL_VIEW = "/WEB-INF/views/admin/order-detail.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/order/detail".equals(path)) {
            request.setAttribute("order", null);
            request.setAttribute("currentTab", "order");
            request.setAttribute(AttributeNames.PAGE_TITLE, "Chi Tiết Đơn Hàng Quản Trị");
            request.setAttribute(AttributeNames.VIEW_PATH, ORDER_DETAIL_VIEW);
        } else {
            request.setAttribute("orders", Collections.emptyList());
            request.setAttribute("currentTab", "order");
            request.setAttribute(AttributeNames.PAGE_TITLE, "Quản Lý Đơn Hàng");
            request.setAttribute(AttributeNames.VIEW_PATH, ORDER_LIST_VIEW);
        }

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
