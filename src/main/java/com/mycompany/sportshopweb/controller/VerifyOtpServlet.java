package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.CartDAO;
import com.mycompany.sportshopweb.dao.CustomerDAO;
import com.mycompany.sportshopweb.dao.UserDAO;
import com.mycompany.sportshopweb.model.Cart;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.MembershipTier;
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

/**
 * Controller xử lý xác thực mã OTP đăng ký tài khoản qua Gmail
 * @author Anh Tuan
 */
@WebServlet(name = "VerifyOtpServlet", urlPatterns = {"/VerifyOtpServlet"})
public class VerifyOtpServlet extends HttpServlet {

    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private CartDAO cartDAO;

    // Tận dụng vòng đời Servlet: Khởi tạo các DAO một lần duy nhất khi Servlet được nạp
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        customerDAO = new CustomerDAO();
        cartDAO = new CartDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String regEmail = (String) session.getAttribute("reg_email");
        String regOtp = (String) session.getAttribute("reg_otp");

        // Nếu người dùng chưa điền form đăng ký mà vào thẳng trang OTP thì chuyển về trang đăng ký
        if (regEmail == null || regOtp == null) {
            response.sendRedirect(request.getContextPath() + "/RegisterServlet");
            return;
        }

        // Tính toán số giây còn lại của mã OTP để hiển thị đồng hồ đếm ngược
        request.setAttribute("remainingSeconds", getRemainingSeconds(session));
        request.getRequestDispatcher("/views/verify-otp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String regEmail = (String) session.getAttribute("reg_email");
        String regOtp = (String) session.getAttribute("reg_otp");
        String firstName = (String) session.getAttribute("reg_firstName");
        String lastName = (String) session.getAttribute("reg_lastName");
        String phone = (String) session.getAttribute("reg_phone");
        String gender = (String) session.getAttribute("reg_gender");
        String rawPassword = (String) session.getAttribute("reg_password");
        Long expiryTime = (Long) session.getAttribute("reg_otp_expiry");

        if (regEmail == null || regOtp == null) {
            response.sendRedirect(request.getContextPath() + "/RegisterServlet");
            return;
        }

        String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
        String action = request.getParameter("action");

        // Xử lý trường hợp người dùng bấm "Gửi lại mã OTP"
        if ("resend".equalsIgnoreCase(action)) {
            String newOtp = String.format("%06d", new Random().nextInt(1000000));
            long newExpiry = System.currentTimeMillis() + (5 * 60 * 1000); // 5 phút

            session.setAttribute("reg_otp", newOtp);
            session.setAttribute("reg_otp_expiry", newExpiry);

            // Gửi lại email qua Brevo API
            EmailService.sendOtpEmail(regEmail, newOtp, "Gửi lại mã xác thực cho " + fullName);

            request.setAttribute("message", "A new OTP has been sent to your email (" + regEmail + "). Please check your inbox!");
            request.setAttribute("remainingSeconds", 300);
            request.getRequestDispatcher("/views/verify-otp.jsp").forward(request, response);
            return;
        }

        // Xử lý xác thực mã OTP người dùng nhập vào
        String enteredOtp = request.getParameter("otp");
        if (enteredOtp != null) {
            enteredOtp = enteredOtp.trim();
        }

        if (enteredOtp == null || enteredOtp.isEmpty()) {
            forwardError(request, response, session, "Please enter the 6-digit OTP code sent to your email.");
            return;
        }

        // Kiểm tra xem OTP đã hết hạn chưa
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            request.setAttribute("error", "The OTP has expired. Please click 'Resend OTP' to receive a new code.");
            request.setAttribute("remainingSeconds", 0);
            request.getRequestDispatcher("/views/verify-otp.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mã OTP có trùng khớp không
        if (!regOtp.equals(enteredOtp)) {
            forwardError(request, response, session, "Invalid OTP code. Please check your email and try again.");
            return;
        }

        // Kiểm tra trùng lặp email hoặc số điện thoại lần cuối phòng trường hợp cạnh tranh
        if (userDAO.existsByEmail(regEmail)) {
            request.setAttribute("error", "Email is already registered. Please use another email.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }
        if (userDAO.existsByPhone(phone)) {
            request.setAttribute("error", "Phone number is already registered. Please use another phone.");
            request.getRequestDispatcher("/views/register.jsp").forward(request, response);
            return;
        }

        // Băm mật khẩu SHA-256
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);

        // Lưu thông tin User
        User user = new User(phone, regEmail, hashedPassword);
        boolean isUserSaved = userDAO.insert(user);
        if (!isUserSaved) {
            forwardError(request, response, session, "System error while creating account. Please try again.");
            return;
        }

        // Lưu thông tin Customer
        MembershipTier defaultTier = customerDAO.getDefaultMembershipTier();
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setMembershipTier(defaultTier);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setGender(gender != null ? gender : "Other");
        customerDAO.insert(customer);

        // Tạo giỏ hàng rỗng ban đầu cho khách hàng
        Cart cart = new Cart();
        cart.setCustomer(customer);
        cartDAO.insert(cart);

        // Gửi email chào mừng thành viên mới kèm mã ưu đãi WELCOME10
        EmailService.sendWelcomeEmail(regEmail, fullName);

        // Xóa các dữ liệu đăng ký tạm thời khỏi Session
        clearRegistrationSession(session);

        // Chuyển hướng sang trang Đăng nhập kèm thông báo thành công
        request.setAttribute("message", "Email verified successfully! Your account is active. Please sign in.");
        request.getRequestDispatcher("/views/login.jsp").forward(request, response);
    }

    // Helper chuyển tiếp lỗi kèm số giây còn lại của mã OTP
    private void forwardError(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.setAttribute("remainingSeconds", getRemainingSeconds(session));
        request.getRequestDispatcher("/views/verify-otp.jsp").forward(request, response);
    }

    // Tính toán số giây hiệu lực còn lại của mã OTP trong session
    private long getRemainingSeconds(HttpSession session) {
        Long expiryTime = (Long) session.getAttribute("reg_otp_expiry");
        if (expiryTime != null) {
            long diff = (expiryTime - System.currentTimeMillis()) / 1000;
            return diff > 0 ? diff : 0;
        }
        return 0;
    }

    // Dọn dẹp các biến tạm thời phục vụ đăng ký khỏi session
    private void clearRegistrationSession(HttpSession session) {
        session.removeAttribute("reg_firstName");
        session.removeAttribute("reg_lastName");
        session.removeAttribute("reg_email");
        session.removeAttribute("reg_phone");
        session.removeAttribute("reg_gender");
        session.removeAttribute("reg_password");
        session.removeAttribute("reg_otp");
        session.removeAttribute("reg_otp_expiry");
    }
}
