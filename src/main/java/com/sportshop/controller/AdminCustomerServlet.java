package com.sportshop.controller;

import com.sportshop.dao.UserDAO;
import com.sportshop.model.User;
import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * AdminCustomerServlet — Controller Quản lý Khách Hàng (REQ-A05).
 */
@WebServlet(name = "AdminCustomerServlet", urlPatterns = {"/admin/customer", "/admin/customers"})
public class AdminCustomerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String CUSTOMER_LIST_VIEW = "/WEB-INF/views/admin/customer-list.jsp";

    private final UserDAO userDAO = UserDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> customers = userDAO.findAllCustomers();
        request.setAttribute("customers", customers);
        request.setAttribute("currentTab", "customer");
        request.setAttribute(AttributeNames.PAGE_TITLE, "Quản Lý Khách Hàng");
        request.setAttribute(AttributeNames.VIEW_PATH, CUSTOMER_LIST_VIEW);

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
