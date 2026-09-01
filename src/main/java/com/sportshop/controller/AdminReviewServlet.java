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
 * AdminReviewServlet — Controller Kiểm duyệt Đánh Giá Review (REQ-A07).
 */
@WebServlet(name = "AdminReviewServlet", urlPatterns = {"/admin/review", "/admin/reviews"})
public class AdminReviewServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_LAYOUT_VIEW = "/WEB-INF/views/common/layout-admin.jsp";
    private static final String REVIEW_VIEW = "/WEB-INF/views/admin/review-moderation.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("reviews", Collections.emptyList());
        request.setAttribute("currentTab", "review");
        request.setAttribute(AttributeNames.PAGE_TITLE, "Kiểm Duyệt Đánh Giá Sản Phẩm");
        request.setAttribute(AttributeNames.VIEW_PATH, REVIEW_VIEW);

        request.getRequestDispatcher(ADMIN_LAYOUT_VIEW).forward(request, response);
    }
}
