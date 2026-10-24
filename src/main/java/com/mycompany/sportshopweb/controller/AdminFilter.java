package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AdminAuthDAO;
import com.mycompany.sportshopweb.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = { "/admin/*", "/views/admin/*" })
public class AdminFilter implements Filter {

    private final AdminAuthDAO authDAO = new AdminAuthDAO();

    @Override
    public void doFilter(
            jakarta.servlet.ServletRequest request,
            jakarta.servlet.ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Lấy session hiện có; không tạo session mới nếu chưa có.
        HttpSession session = httpRequest.getSession(false);

        // Bước 1: Kiểm tra người dùng đã đăng nhập chưa.
        if (session == null
                || !(session.getAttribute("user") instanceof User)) {

            httpResponse.sendRedirect(
                    httpRequest.getContextPath() + "/LoginServlet");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");

        // Bước 2 & 3: Kiểm tra tài khoản có quyền Admin thông qua DAO.
        boolean isAdmin = authDAO.isAdmin(sessionUser.getUserId());

        // Bước 4: Không có quyền admin thì từ chối truy cập.
        if (!isAdmin) {
            httpResponse.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to access this page.");
            return;
        }

        // Bước 5: Đã được xác thực và cấp quyền.
        // Cho request tiếp tục tới servlet hoặc resource đích.
        chain.doFilter(request, response);
    }
}
