
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.ProductVariantDAO;
import com.mycompany.sportshopweb.model.ProductVariant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ProductVariantServlet", urlPatterns = { "/admin/product-variants" })
public class ProductVariantServlet extends HttpServlet {

    private ProductVariantDAO variantDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        variantDAO = new ProductVariantDAO();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int productId = parsePositiveId(request.getParameter("productId"));

        if (productId <= 0) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID");
            return;
        }

        String action = request.getParameter("action");

        String redirectUrl = request.getContextPath()
                + "/admin/products?manageVariants=" + productId;

        try {
            boolean success;

            switch (action == null ? "" : action) {
                case "create":
                    success = variantDAO.insertVariant(
                            productId,
                            readText(request, "variantName"),
                            readText(request, "size"),
                            readText(request, "color"),
                            readLong(request, "price"),
                            readInt(request, "quantity"));

                    redirectWithMessage(
                            response,
                            redirectUrl,
                            success ? "created" : "failed");
                    return;

                case "update": {
                    int variantId = readPositiveId(request.getParameter("variantId"));

                    if (variantId <= 0) {
                        redirectWithMessage(
                                response, redirectUrl, "invalid");
                        return;
                    }

                    ProductVariant variant = variantDAO.getVariantById(variantId);

                    if (!belongsToProduct(variant, productId)) {
                        redirectWithMessage(
                                response, redirectUrl, "failed");
                        return;
                    }

                    success = variantDAO.updateVariant(
                            variantId,
                            readText(request, "variantName"),
                            readText(request, "size"),
                            readText(request, "color"),
                            readLong(request, "price"),
                            readInt(request, "quantity"));

                    redirectWithMessage(
                            response,
                            redirectUrl,
                            success ? "updated" : "failed");
                    return;
                }

                case "delete": {
                    int variantId = readPositiveId(request.getParameter("variantId"));

                    if (variantId <= 0) {
                        redirectWithMessage(
                                response, redirectUrl, "invalid");
                        return;
                    }

                    ProductVariant variant = variantDAO.getVariantById(variantId);

                    if (!belongsToProduct(variant, productId)) {
                        redirectWithMessage(
                                response, redirectUrl, "failed");
                        return;
                    }

                    success = variantDAO.deleteVariant(variantId);

                    redirectWithMessage(
                            response,
                            redirectUrl,
                            success ? "deleted" : "failed");
                    return;
                }

                default:
                    redirectWithMessage(
                            response, redirectUrl, "invalidAction");
            }

        } catch (IllegalArgumentException e) {
            redirectWithMessage(response, redirectUrl, "invalid");

        } catch (RuntimeException e) {
            log("Unable to process variant action for product ID: "
                    + productId, e);

            redirectWithMessage(response, redirectUrl, "error");
        }
    }

    private boolean belongsToProduct(
            ProductVariant variant,
            int productId) {

        return variant != null
                && variant.getProduct() != null
                && variant.getProduct().getProductId() == productId;
    }

    private void redirectWithMessage(
            HttpServletResponse response,
            String redirectUrl,
            String message) throws IOException {

        response.sendRedirect(
                redirectUrl + "&variantMessage=" + message);
    }

    private String readText(
            HttpServletRequest request,
            String name) {

        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private int readInt(
            HttpServletRequest request,
            String name) {

        try {
            String rawValue = request.getParameter(name);

            if (rawValue == null || rawValue.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Missing number: " + name);
            }

            int value = Integer.parseInt(rawValue.trim());

            if (value < 0) {
                throw new IllegalArgumentException(
                        name + " must not be negative");
            }

            return value;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid number: " + name, e);
        }
    }

    private long readLong(
            HttpServletRequest request,
            String name) {

        try {
            String rawValue = request.getParameter(name);

            if (rawValue == null || rawValue.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Missing number: " + name);
            }

            long value = Long.parseLong(rawValue.trim());

            if (value < 0) {
                throw new IllegalArgumentException(
                        name + " must not be negative");
            }

            return value;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid number: " + name, e);
        }
    }

    private int parsePositiveId(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return -1;
        }

        try {
            int value = Integer.parseInt(rawValue.trim());
            return value > 0 ? value : -1;

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int readPositiveId(String rawValue) {
        return parsePositiveId(rawValue);
    }
}
