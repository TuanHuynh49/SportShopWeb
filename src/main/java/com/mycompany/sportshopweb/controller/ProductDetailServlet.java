package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.model.Product;
import com.mycompany.sportshopweb.model.ProductVariant;
import com.mycompany.sportshopweb.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/ProductDetailServlet"})
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Product product = ProductDAO.getProductById(id);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
            return;
        }

        List<ProductVariant> variants = ProductDAO.getVariantsByProductId(id);

        int totalStock = 0;
        long minPrice = 0;
        boolean first = true;
        for (ProductVariant v : variants) {
            if (v.getQuantity() > 0) { 
                totalStock += v.getQuantity();
                if (first || v.getPrice() < minPrice) {
                    minPrice = v.getPrice();
                    first = false;
                }
            }
        }
        if (first && !variants.isEmpty()) {
            minPrice = variants.get(0).getPrice();
        }

        request.setAttribute("product", product);
        request.setAttribute("variants", variants);
        request.setAttribute("totalStock", totalStock);
        request.setAttribute("inStock", totalStock > 0);
        request.setAttribute("minPrice", minPrice);

        request.getRequestDispatcher("/views/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}