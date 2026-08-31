package com.sportshop.controller;

import com.sportshop.dao.CategoryDAO;
import com.sportshop.dao.ProductDAO;
import com.sportshop.model.Category;
import com.sportshop.model.Product;
import com.sportshop.util.AttributeNames;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * HomeServlet — Controller điều hướng trang chủ (MVC Architecture).
 * Truy vấn danh mục và sản phẩm từ cơ sở dữ liệu qua DAO.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home", ""})
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String LAYOUT_VIEW = "/WEB-INF/views/common/layout-customer.jsp";
    private static final String HOME_VIEW = "/WEB-INF/views/customer/home.jsp";

    private final CategoryDAO categoryDAO = CategoryDAO.getInstance();
    private final ProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Truy vấn dữ liệu động từ DAO
        List<Category> categories = categoryDAO.findAllTree();
        List<Product> featuredProducts = productDAO.findFeatured(8);
        List<Product> newArrivals = productDAO.findNewArrivals(8);

        // 2. Set Request Attributes
        request.setAttribute(AttributeNames.CATEGORIES, categories);
        request.setAttribute(AttributeNames.FEATURED_PRODUCTS, featuredProducts);
        request.setAttribute(AttributeNames.NEW_ARRIVALS, newArrivals);

        request.setAttribute(AttributeNames.PAGE_TITLE, "Trang Chủ");
        request.setAttribute(AttributeNames.VIEW_PATH, HOME_VIEW);

        // 3. Forward tới Layout chung
        request.getRequestDispatcher(LAYOUT_VIEW).forward(request, response);
    }
}
