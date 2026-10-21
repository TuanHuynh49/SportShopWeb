package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.UserDAO;
import com.mycompany.sportshopweb.model.User;
import com.mycompany.sportshopweb.util.EmailService;
import com.mycompany.sportshopweb.util.PasswordUtil;

import java.io.IOException;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/ForgotPasswordServlet", "/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String step = request.getParameter("step");
        HttpSession session = request.getSession(false);

        // Chuyển sang trang nhập OTP và mật khẩu mới nếu đã gửi OTP thành công
        if ("reset".equals(step)) {
            if (session != null && session.getAttribute("reset_email") != null) {
                request.setAttribute("resetEmail", session.getAttribute("reset_email"));

                // Lấy thông báo gửi lại mã nếu có trong session
                if (session.getAttribute("message") != null) {
                    request.setAttribute("message", session.getAttribute("message"));
                    session.removeAttribute("message");
                }

                request.getRequestDispatcher("/views/reset-password.jsp").forward(request, response);
                return;
            }
            // Nếu chưa có email trong session thì quay về bước nhập email
            response.sendRedirect(request.getContextPath() + "/ForgotPasswordServlet");
            return;
        }

        // Mặc định hiển thị trang nhập email quên mật khẩu
        request.getRequestDispatcher("/views/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("sendOtp".equals(action)) {
            handleSendOtp(request, response);
        } else if ("verifyAndReset".equals(action)) {
            handleVerifyAndReset(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/ForgotPasswordServlet");
        }
    }

    // Xử lý kiểm tra email và gửi mã xác thực OTP
    private void handleSendOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        if (email != null) {
            email = email.trim();
        }

        if (email == null || email.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập địa chỉ email.");
            request.getRequestDispatcher("/views/forgot-password.jsp").forward(request, response);
            return;
        }

        User user = userDAO.findByIdentifier(email);
        if (user == null) {
            request.setAttribute("error", "Email không tồn tại trong hệ thống.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/views/forgot-password.jsp").forward(request, response);
            return;
        }

        // Tạo mã OTP ngẫu nhiên 6 chữ số
        String otp = String.format("%06d", new Random().nextInt(1000000));
        long expiry = System.currentTimeMillis() + (5 * 60 * 1000); // Hiệu lực 5 phút

        // Lưu thông tin vào Session để kiểm tra ở bước sau
        HttpSession session = request.getSession();
        session.setAttribute("reset_email", email);
        session.setAttribute("reset_otp", otp);
        session.setAttribute("reset_otp_expiry", expiry);
        session.setAttribute("message", "Mã xác thực OTP đã được gửi đến email của bạn.");

        // Gửi email chứa OTP qua Brevo API
        EmailService.sendOtpEmail(email, otp, "Đặt lại mật khẩu tài khoản SportShop");

        response.sendRedirect(request.getContextPath() + "/ForgotPasswordServlet?step=reset");
    }

    // Xử lý xác thực OTP và đổi mật khẩu mới
    private void handleVerifyAndReset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            request.setAttribute("error", "Phiên xác thực đã hết hạn. Vui lòng nhập lại email.");
            request.getRequestDispatcher("/views/forgot-password.jsp").forward(request, response);
            return;
        }

        String sessionEmail = (String) session.getAttribute("reset_email");
        String sessionOtp = (String) session.getAttribute("reset_otp");
        Long expiry = (Long) session.getAttribute("reset_otp_expiry");

        String inputOtp = request.getParameter("otp");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        request.setAttribute("resetEmail", sessionEmail);

        // Kiểm tra thời hạn mã OTP
        if (expiry == null || System.currentTimeMillis() > expiry) {
            request.setAttribute("error", "Mã OTP đã hết hiệu lực (quá 5 phút). Vui lòng gửi lại mã mới.");
            request.getRequestDispatcher("/views/reset-password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra tính chính xác của OTP
        if (inputOtp == null || !inputOtp.trim().equals(sessionOtp)) {
            request.setAttribute("error", "Mã xác thực OTP không chính xác.");
            request.getRequestDispatcher("/views/reset-password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra độ dài mật khẩu mới
        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự.");
            request.getRequestDispatcher("/views/reset-password.jsp").forward(request, response);
            return;
        }

        // Kiểm tra khớp mật khẩu xác nhận
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Xác nhận mật khẩu mới không khớp.");
            request.getRequestDispatcher("/views/reset-password.jsp").forward(request, response);
            return;
        }

        // Cập nhật mật khẩu mới đã băm vào cơ sở dữ liệu
        User user = userDAO.findByIdentifier(sessionEmail);
        if (user != null) {
            user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
            userDAO.update(user);
        }

        // Xóa thông tin reset khỏi session để bảo mật
        session.removeAttribute("reset_email");
        session.removeAttribute("reset_otp");
        session.removeAttribute("reset_otp_expiry");

        // Ghi nhận thông báo và chuyển hướng sang trang Đăng nhập
        session.setAttribute("message", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
    }
}
