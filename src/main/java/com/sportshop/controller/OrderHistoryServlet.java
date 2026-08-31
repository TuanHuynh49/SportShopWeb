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
 * OrderHistoryServlet — Controller xem lịch sử đơn hàng của khách hàng.
 */
@WebServlet(name = "OrderHistoryServlet", urlPatterns = {"/order/history", "/orders"})
public class OrderHistoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LAYOUT_VIEW = "/WEB-INF/views/common/layout-customer.jsp";
    private static final String ORDER_HISTORY_VIEW = "/WEB-INF/views/customer/order-history.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(AttributeNames.ORDER_LIST, Collections.emptyList());
        request.setAttribute(AttributeNames.PAGE_TITLE, "Lịch Sử Đơn Hàng");
        request.setAttribute(AttributeNames.VIEW_PATH, ORDER_HISTORY_VIEW);

        request.getRequestDispatcher(LAYOUT_VIEW).forward(request, response);
    }
}
