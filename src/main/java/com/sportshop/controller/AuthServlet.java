package com.sportshop.controller;

import com.sportshop.dao.UserDAO;
import com.sportshop.model.Role;
import com.sportshop.model.User;
import com.sportshop.model.UserStatus;
import com.sportshop.util.AttributeNames;
import com.sportshop.util.CookieUtil;
import com.sportshop.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * AuthServlet — Controller xử lý Đăng nhập, Đăng ký và Đăng xuất (MVC Architecture).
 *
 * Hỗ trợ các URL:
 * - GET/POST /login
 * - GET/POST /register
 * - GET/POST /logout
 * - GET/POST /auth (qua tham số action)
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/auth", "/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String LOGIN_VIEW = "/WEB-INF/views/auth/login.jsp";
    private static final String REGISTER_VIEW = "/WEB-INF/views/auth/register.jsp";

    // Regex kiểm tra định dạng email và số điện thoại Việt Nam
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[3|5|7|8|9][0-9]{8}$");

    private final UserDAO userDAO = UserDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        String action = request.getParameter("action");
        if (action == null) action = "";

        if ("/logout".equals(servletPath) || "logout".equalsIgnoreCase(action)) {
            handleLogout(request, response);
            return;
        }

        if ("/register".equals(servletPath) || "register".equalsIgnoreCase(action)) {
            handleShowRegister(request, response);
            return;
        }

        // Mặc định hoặc /login
        handleShowLogin(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        String action = request.getParameter("action");
        if (action == null) action = "";

        if ("/logout".equals(servletPath) || "logout".equalsIgnoreCase(action)) {
            handleLogout(request, response);
            return;
        }

        if ("/register".equals(servletPath) || "register".equalsIgnoreCase(action)) {
            handleProcessRegister(request, response);
            return;
        }

        // Mặc định hoặc /login
        handleProcessLogin(request, response);
    }

    // ── XỬ LÝ GET ─────────────────────────────────────────────────────────────

    private void handleShowLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AttributeNames.AUTH_USER) != null) {
            // Nếu đã đăng nhập thì chuyển hướng về trang phù hợp
            User authUser = (User) session.getAttribute(AttributeNames.AUTH_USER);
            redirectAfterLogin(request, response, authUser);
            return;
        }

        // Đọc các tham số thông báo từ URL (nếu có)
        String errorParam = request.getParameter("error");
        if ("unauthorized".equalsIgnoreCase(errorParam)) {
            request.setAttribute("errorMessage", "Bạn cần quyền Quản trị viên (ADMIN) để truy cập trang này.");
        } else if ("login_required".equalsIgnoreCase(errorParam)) {
            request.setAttribute("errorMessage", "Vui lòng đăng nhập để tiếp tục thực hiện hành động này.");
        }

        if ("true".equalsIgnoreCase(request.getParameter("registered"))) {
            request.setAttribute("flashSuccess", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
        } else if ("true".equalsIgnoreCase(request.getParameter("logout"))) {
            request.setAttribute("flashSuccess", "Bạn đã đăng xuất thành công khỏi hệ thống.");
        }

        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }

    private void handleShowRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AttributeNames.AUTH_USER) != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
    }

    // ── XỬ LÝ POST ────────────────────────────────────────────────────────────

    /**
     * Xử lý Đăng nhập
     */
    private void handleProcessLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // Validate cơ bản
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Vui lòng nhập đầy đủ email và mật khẩu.");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            return;
        }

        email = email.trim();

        // Xác thực qua DAO
        User user = userDAO.authenticate(email, password);

        if (user == null) {
            request.setAttribute("errorMessage", "Email hoặc mật khẩu không chính xác.");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            return;
        }

        // Kiểm tra trạng thái tài khoản
        if (user.isLocked()) {
            request.setAttribute("errorMessage", "Tài khoản này đang bị khóa. Vui lòng liên hệ quản trị viên.");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            return;
        }

        // Đăng nhập thành công: Lưu session
        HttpSession session = request.getSession(true);
        session.setAttribute(AttributeNames.AUTH_USER, user);
        if (user.isAdmin()) {
            session.setAttribute(AttributeNames.SESSION_ADMIN_USER, user);
        }

        // Xử lý Remember Me (30 ngày)
        if ("on".equalsIgnoreCase(rememberMe) || "true".equalsIgnoreCase(rememberMe)) {
            CookieUtil.setCookie(response, AttributeNames.REMEMBER_ME_COOKIE, user.getEmail(), 30 * 24 * 60 * 60);
        } else {
            CookieUtil.deleteCookie(response, AttributeNames.REMEMBER_ME_COOKIE);
        }

        // Chuyển hướng theo luồng
        redirectAfterLogin(request, response, user);
    }

    /**
     * Xử lý Đăng ký tài khoản mới
     */
    private void handleProcessRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Kiểm tra rỗng
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()) {

            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ tất cả các trường thông tin.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            return;
        }

        fullName = fullName.trim();
        email = email.trim();
        phone = phone.trim();

        // 2. Validate định dạng email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("errorMessage", "Địa chỉ email không đúng định dạng.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            return;
        }

        // 3. Validate định dạng SĐT
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            request.setAttribute("errorMessage", "Số điện thoại không hợp lệ (gồm 10 số, bắt đầu bằng 0 hoặc +84).");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            return;
        }

        // 4. Validate độ dài mật khẩu
        if (password.length() < 8) {
            request.setAttribute("errorMessage", "Mật khẩu phải chứa ít nhất 8 ký tự.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            return;
        }

        // 5. Kiểm tra mật khẩu khớp nhau
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp với mật khẩu đã nhập.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            return;
        }

        // 6. Kiểm tra trùng email
        if (userDAO.existsByEmail(email)) {
            request.setAttribute("errorMessage", "Email này đã được sử dụng. Vui lòng chọn email khác hoặc đăng nhập.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
            return;
        }

        // 7. Băm mật khẩu và tạo User mới (vai trò mặc định CUSTOMER)
        String passwordHash = PasswordUtil.hashPassword(password);
        User newUser = new User(
                null,
                fullName,
                email,
                passwordHash,
                Role.CUSTOMER,
                phone,
                UserStatus.ACTIVE
        );

        boolean success = userDAO.create(newUser);
        if (success) {
            // Chuyển hướng về đăng nhập kèm cờ thông báo (mô hình PRG)
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } else {
            request.setAttribute("errorMessage", "Không thể tạo tài khoản vào lúc này. Vui lòng thử lại sau.");
            request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
        }
    }

    /**
     * Xử lý Đăng xuất
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Xóa Cookie Remember Me
        CookieUtil.deleteCookie(response, AttributeNames.REMEMBER_ME_COOKIE);

        // Chuyển hướng về trang đăng nhập
        response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }

    /**
     * Điều hướng thông minh sau khi đăng nhập thành công
     */
    private void redirectAfterLogin(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        HttpSession session = request.getSession(false);
        String redirectUrl = null;
        if (session != null) {
            redirectUrl = (String) session.getAttribute(AttributeNames.REDIRECT_URL);
            if (redirectUrl != null) {
                session.removeAttribute(AttributeNames.REDIRECT_URL);
                response.sendRedirect(redirectUrl);
                return;
            }
        }

        // Nếu là ADMIN -> chuyển hướng sang Dashboard quản trị
        if (user != null && user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        // Mặc định về trang chủ
        response.sendRedirect(request.getContextPath() + "/");
    }
}
