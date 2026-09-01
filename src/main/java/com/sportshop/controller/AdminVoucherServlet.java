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
 * AdminVoucherServlet — Controller Quản lý Mã giảm giá (REQ-A06).
 */
@WebServlet(name = "AdminVoucherServlet", urlPatterns = {"/admin/voucher", "/admin/voucher/new"})
public class AdminVoucherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String VOUCHER_LIST_VIEW = "/WEB-INF/views/admin/voucher-list.jsp";
    private static final String VOUCHER_FORM_VIEW = "/WEB-INF/views/admin/voucher-form.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/voucher/new".equals(path)) {
            request.setAttribute("currentTab", "voucher");
            request.setAttribute(AttributeNames.PAGE_TITLE, "Tạo Mã Giảm Giá Mới");
            request.setAttribute(AttributeNames.VIEW_PATH, VOUCHER_FORM_VIEW);
        } else {
            request.setAttribute("vouchers", Collections.emptyList());
            request.setAttribute("currentTab", "voucher");
            request.setAttribute(AttributeNames.PAGE_TITLE, "Quản Lý Mã Giảm Giá (Voucher)");
            request.setAttribute(AttributeNames.VIEW_PATH, VOUCHER_LIST_VIEW);
        }

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
