/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.dao.UserDAO;
import com.mycompany.sportshopweb.model.AccountStatus;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.User;
import com.mycompany.sportshopweb.util.PasswordUtil;
import com.mycompany.sportshopweb.dao.AdminDAO;
import com.mycompany.sportshopweb.model.Admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
        
import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.model.Cart;


/**
 *
 * @author Anh Tuan
 */
@WebServlet(name = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;
    private AdminDAO adminDAO;
    private CustomerDAO customerDAO;
    private CartDAO cartDAO;

    // Tận dụng vòng đời Servlet: Khởi tạo các DAO một lần duy nhất khi Servlet được nạp vào bộ nhớ
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        adminDAO = new AdminDAO();
        customerDAO = new CustomerDAO();
        cartDAO = new CartDAO();
    }

    // Khi người dùng bấm vào link hoặc truy cập URL trang đăng nhập
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy toàn bộ danh sách cookie mà trình duyệt gửi lên
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("remember_identifier".equals(c.getName())) {
                    // Đẩy giá trị email/sdt và trạng thái đã tick sang giao diện JSP
                    request.setAttribute("identifier", c.getValue());
                    request.setAttribute("rememberMe", true);
                    break;
                }
            }
        }
        // Lấy thông báo thành công từ session (nếu có, ví dụ sau khi đổi mật khẩu)
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("message") != null) {
            request.setAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
        
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    // Khi người dùng điền email/sđt + mật khẩu và bấm "Sign In"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập mã hóa tiếng Việt
        request.setCharacterEncoding("UTF-8");
        
        // Lấy dữ liệu từ form login.jsp
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        if (identifier != null) {
            identifier = identifier.trim();
        }

        // Giữ lại tên đăng nhập để người dùng không phải gõ lại nếu sai mật khẩu
        request.setAttribute("identifier", identifier);

        // Kiểm tra không được bỏ trống
        if (identifier == null || identifier.isEmpty() || password == null || password.isEmpty()) {
            forwardError(request, response, "Please enter your email/phone and password.");
            return;
        }

        // Kiểm tra khóa tạm thời do nhập sai quá 5 lần (Chống tấn công Brute-force theo Chapter 17)
        HttpSession session = request.getSession();
        Long lockTime = (Long) session.getAttribute("login_lock_time");
        if (lockTime != null) {
            if (System.currentTimeMillis() < lockTime) {
                long remainingMins = ((lockTime - System.currentTimeMillis()) / (60 * 1000)) + 1;
                forwardError(request, response, "Too many failed login attempts. Please try again in " + remainingMins + " minutes.");
                return;
            } else {
                // Hết thời gian khóa, tự động mở khóa
                session.removeAttribute("login_lock_time");
                session.removeAttribute("login_failed_attempts");
            }
        }

        // Tìm tài khoản trong database theo email hoặc số điện thoại
        User user = userDAO.findByIdentifier(identifier);

        // Kiểm tra mật khẩu băm
        boolean isPasswordCorrect = (user != null) && PasswordUtil.verifyPassword(password, user.getPasswordHash());

        // Nếu sai tài khoản hoặc sai mật khẩu: Tăng số lần thử sai
        if (user == null || !isPasswordCorrect) {
            Integer failedAttempts = (Integer) session.getAttribute("login_failed_attempts");
            failedAttempts = (failedAttempts == null) ? 1 : failedAttempts + 1;
            session.setAttribute("login_failed_attempts", failedAttempts);

            if (failedAttempts >= 5) {
                // Khóa đăng nhập trong 15 phút
                session.setAttribute("login_lock_time", System.currentTimeMillis() + (15 * 60 * 1000));
                forwardError(request, response, "You have entered wrong credentials 5 times. Your login is locked for 15 minutes.");
            } else {
                forwardError(request, response, "Invalid email/phone or password. (Attempt " + failedAttempts + "/5)");
            }
            return;
        }

        // Kiểm tra tài khoản có bị khóa không
        if (user.getStatus() != null && user.getStatus() != AccountStatus.ACTIVE) {
            forwardError(request, response, "Your account has been locked or deactivated. Please contact support.");
            return;
        }

        // Đăng nhập thành công -> Xóa bộ đếm số lần sai
        session.removeAttribute("login_failed_attempts");
        session.removeAttribute("login_lock_time");
        
        // Xử lý lưu hoặc xóa Cookie Ghi nhớ đăng nhập
        handleRememberMeCookie(request, response, identifier, request.getParameter("rememberMe"));
        
        // Lưu thông tin đăng nhập vào Session
        session.setAttribute("user", user);
        
        // Phân quyền: Nếu là Admin thì chuyển đến Admin Dashboard
        Admin admin = adminDAO.findByUserId(user.getUserId());
        if (admin != null) {
            session.setAttribute("admin", admin);
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        // Nếu là Customer thông thường thì chuyển đến trang chủ HomeServlet
        Customer customer = customerDAO.findByUserId(user.getUserId());

        if (customer == null) {
            forwardError(
                    request,
                    response,
                    "Không tìm thấy thông tin khách hàng."
            );
            return;
        }

        session.setAttribute("customer", customer);
        updateCartCount(session, customer.getCustomerId());

        response.sendRedirect(request.getContextPath() + "/HomeServlet");
    }

    // Helper chuyển hướng báo lỗi (Áp dụng DRY - Don't Repeat Yourself)
    private void forwardError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    // Tách hàm xử lý lưu/xóa Cookie Ghi nhớ đăng nhập
    private void handleRememberMeCookie(HttpServletRequest request, HttpServletResponse response,
            String identifier, String rememberMe) {
        Cookie cookie = new Cookie("remember_identifier", identifier);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setHttpOnly(true); // Chống XSS (Cross-Site Scripting)
        cookie.setSecure(request.isSecure()); // Tự động bật bảo vệ SSL/TLS khi chạy trên HTTPS (Chapter 15)

        if ("true".equals(rememberMe)) {
            cookie.setMaxAge(7 * 24 * 60 * 60); // Lưu 7 ngày
        } else {
            cookie.setMaxAge(0); // Xóa cookie nếu bỏ tick
        }
        response.addCookie(cookie);
    }

    // Cập nhật số lượng mặt hàng trong giỏ hàng vào Session
    private void updateCartCount(HttpSession session, int customerId) {
        Cart cart = cartDAO.findByCustomerId(customerId);
        int cartCount = 0;
        if (cart != null && cart.getCartItems() != null) {
            cartCount = cart.getCartItems().size();
        }
        session.setAttribute("cartCount", cartCount);
    }
}
