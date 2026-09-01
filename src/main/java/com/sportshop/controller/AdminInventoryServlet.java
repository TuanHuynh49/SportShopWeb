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
 * AdminInventoryServlet — Controller Quản lý Tồn kho & Biến thể (REQ-A03).
 */
@WebServlet(name = "AdminInventoryServlet", urlPatterns = {"/admin/inventory"})
public class AdminInventoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String INVENTORY_VIEW = "/WEB-INF/views/admin/variant-inventory.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("inventoryList", Collections.emptyList());
        request.setAttribute("currentTab", "inventory");
        request.setAttribute(AttributeNames.PAGE_TITLE, "Quản Lý Tồn Kho & Biến Thể");
        request.setAttribute(AttributeNames.VIEW_PATH, INVENTORY_VIEW);

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
