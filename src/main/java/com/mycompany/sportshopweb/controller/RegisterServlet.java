package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.UserDAO;
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

/**
 *
 * @author Anh Tuan
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;

    // Tận dụng vòng đời Servlet: Khởi tạo UserDAO một lần duy nhất khi Servlet được nạp
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    // Khi người dùng truy cập trực tiếp bằng URL thì chuyển hướng đến trang register.jsp
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }

    // Khi người dùng bấm nút Submit form đăng ký
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thiết lập mã hóa tiếng Việt
        request.setCharacterEncoding("UTF-8");

        // Lấy dữ liệu từ form gửi lên
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Loại bỏ khoảng trắng thừa
        if (firstName != null) firstName = firstName.trim();
        if (lastName != null) lastName = lastName.trim();
        if (email != null) email = email.trim();
        if (phone != null) phone = phone.trim();

        // Giữ lại giá trị người dùng đã nhập để hiển thị lại nếu có lỗi
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("gender", gender);

        // Kiểm tra tính hợp lệ cơ bản của dữ liệu
        if (firstName == null || firstName.isEmpty() ||
            lastName == null || lastName.isEmpty() ||
            email == null || email.isEmpty() ||
            phone == null || phone.isEmpty() ||
            password == null || password.isEmpty()) {
            forwardError(request, response, "Please fill in all required fields.");
            return;
        }

        // Kiểm tra độ mạnh mật khẩu theo chuẩn Chapter 17
        String passwordError = PasswordUtil.checkPasswordStrength(password);
        if (passwordError != null) {
            forwardError(request, response, passwordError);
            return;
        }

        if (!password.equals(confirmPassword)) {
            forwardError(request, response, "Confirm password does not match.");
            return;
        }

        // Kiểm tra định dạng Email hợp lệ
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            forwardError(request, response, "Invalid email format. Please enter a valid email address (e.g. name@gmail.com).");
            return;
        }

        // Kiểm tra định dạng Số điện thoại Việt Nam (10 chữ số, bắt đầu bằng số 0)
        String phoneRegex = "^0[0-9]{9}$";
        if (!phone.matches(phoneRegex)) {
            forwardError(request, response, "Invalid phone number. Must be 10 digits starting with 0 (e.g. 0901234567).");
            return;
        }

        // Kiểm tra xem Email đã có ai đăng ký chưa
        if (userDAO.existsByEmail(email)) {
            forwardError(request, response, "Email is already registered. Please use another email.");
            return;
        }

        // Kiểm tra xem Số điện thoại đã có ai đăng ký chưa
        if (userDAO.existsByPhone(phone)) {
            forwardError(request, response, "Phone number is already registered. Please use another phone number.");
            return;
        }

        // Tạo mã OTP ngẫu nhiên 6 chữ số (Hiệu lực 5 phút)
        String otp = String.format("%06d", new Random().nextInt(1000000));

        // Lưu tạm thông tin đăng ký vào Session để chờ xác thực OTP
        HttpSession session = request.getSession();
        session.setAttribute("reg_firstName", firstName);
        session.setAttribute("reg_lastName", lastName);
        session.setAttribute("reg_email", email);
        session.setAttribute("reg_phone", phone);
        session.setAttribute("reg_gender", (gender != null && !gender.isEmpty()) ? gender : "Other");
        session.setAttribute("reg_password", password);
        session.setAttribute("reg_otp", otp);
        session.setAttribute("reg_otp_expiry", System.currentTimeMillis() + (5 * 60 * 1000));

        // Gửi mã OTP xác nhận đến Gmail của khách hàng
        String fullName = firstName + " " + lastName;
        EmailService.sendOtpEmail(email, fullName, otp);

        // Chuyển hướng sang trang xác nhận mã OTP
        response.sendRedirect(request.getContextPath() + "/VerifyOtpServlet");
    }

    // Helper chuyển hướng báo lỗi (Áp dụng DRY - Don't Repeat Yourself)
    private void forwardError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/views/register.jsp").forward(request, response);
    }
}
