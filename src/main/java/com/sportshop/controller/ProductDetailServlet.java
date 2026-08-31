package com.sportshop.controller;

import com.sportshop.dao.ProductDAO;
import com.sportshop.model.Product;
import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * ProductDetailServlet — Controller xem chi tiết sản phẩm.
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product/detail", "/product"})
public class ProductDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LAYOUT_VIEW = "/WEB-INF/views/common/layout-customer.jsp";
    private static final String DETAIL_VIEW = "/WEB-INF/views/customer/product-detail.jsp";

    private final ProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        Long id = null;
        if (idParam != null) {
            try {
                id = Long.parseLong(idParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        Product product = null;
        if (id != null) {
            product = productDAO.findById(id);
        }

        request.setAttribute(AttributeNames.PRODUCT, product);
        request.setAttribute(AttributeNames.PAGE_TITLE, product != null ? product.getName() : "Chi Tiết Sản Phẩm");
        request.setAttribute(AttributeNames.VIEW_PATH, DETAIL_VIEW);

        request.getRequestDispatcher(LAYOUT_VIEW).forward(request, response);
    }
}
