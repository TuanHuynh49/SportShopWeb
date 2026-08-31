package com.sportshop.controller;

import com.sportshop.model.Cart;
import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * CartServlet — Controller giỏ hàng.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/*"})
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LAYOUT_VIEW = "/WEB-INF/views/common/layout-customer.jsp";
    private static final String CART_VIEW = "/WEB-INF/views/customer/cart.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute(AttributeNames.SESSION_CART);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(AttributeNames.SESSION_CART, cart);
        }

        request.setAttribute(AttributeNames.PAGE_TITLE, "Giỏ Hàng Của Bạn");
        request.setAttribute(AttributeNames.VIEW_PATH, CART_VIEW);

        request.getRequestDispatcher(LAYOUT_VIEW).forward(request, response);
    }
}
