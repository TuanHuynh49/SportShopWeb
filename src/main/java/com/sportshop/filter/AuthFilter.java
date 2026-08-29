package com.sportshop.filter;

import com.sportshop.dao.UserDAO;
import com.sportshop.model.User;
import com.sportshop.util.AttributeNames;
import com.sportshop.util.CookieUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * AuthFilter — Servlet Filter kiểm soát Phân quyền (RBAC) & Bảo vệ Session.
 *
 * Nhiệm vụ:
 * 1. Bỏ qua tài nguyên tĩnh và các route public.
 * 2. Bảo vệ các URL admin (/admin/*): Bắt buộc vai trò ADMIN, nếu không -> chuyển hướng về /login?error=unauthorized.
 * 3. Bảo vệ các route khách hàng nhạy cảm (/checkout, /account/*, /orders/*): Bắt buộc đăng nhập, nếu chưa -> lưu redirectUrl và đưa về /login.
 * 4. Tự động phục hồi phiên đăng nhập qua Cookie Remember Me nếu khả dụng.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    // Danh sách phần mở rộng tài nguyên tĩnh
    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
            ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico",
            ".webp", ".woff", ".woff2", ".ttf", ".eot", ".map"
    );

    // Danh sách tiền tố URL tài nguyên tĩnh
    private static final List<String> STATIC_PREFIXES = Arrays.asList(
            "/assets/", "/css/", "/js/", "/images/", "/preview/"
    );

    // Danh sách route nhạy cảm yêu cầu bắt buộc đăng nhập (CUSTOMER hoặc ADMIN)
    private static final List<String> PROTECTED_CUSTOMER_ROUTES = Arrays.asList(
            "/checkout", "/account", "/order", "/orders", "/address", "/review-form"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String path = requestURI.substring(contextPath.length());

        // ── 1. BỎ QUA TÀI NGUYÊN TĨNH ─────────────────────────────────────────
        if (isStaticResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(true);
        User authUser = (User) session.getAttribute(AttributeNames.AUTH_USER);

        // ── 2. PHỤC HỒI ĐĂNG NHẬP QUA REMEMBER ME COOKIE (NẾU CHƯA CÓ SESSION) ─
        if (authUser == null) {
            String rememberEmail = CookieUtil.getCookieValue(request, AttributeNames.REMEMBER_ME_COOKIE);
            if (rememberEmail != null && !rememberEmail.trim().isEmpty()) {
                User userFromCookie = UserDAO.getInstance().findByEmail(rememberEmail);
                if (userFromCookie != null && userFromCookie.isActive()) {
                    authUser = userFromCookie;
                    session.setAttribute(AttributeNames.AUTH_USER, authUser);
                    if (authUser.isAdmin()) {
                        session.setAttribute(AttributeNames.SESSION_ADMIN_USER, authUser);
                    }
                }
            }
        }

        // ── 3. KIỂM TRA PHÂN QUYỀN TRANG QUẢN TRỊ (/admin/*) ─────────────────
        if (path.startsWith("/admin")) {
            if (authUser == null || !authUser.isAdmin()) {
                // Lưu thông báo hoặc mã lỗi phân quyền
                response.sendRedirect(contextPath + "/login?error=unauthorized");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // ── 4. KIỂM TRA ROUTE KHÁCH HÀNG NHẠY CẢM (/checkout, /account, /orders) ──
        if (isProtectedCustomerRoute(path)) {
            if (authUser == null) {
                // Lưu lại URL ban đầu để quay lại sau khi đăng nhập thành công
                String targetUrl = requestURI;
                if (request.getQueryString() != null) {
                    targetUrl += "?" + request.getQueryString();
                }
                session.setAttribute(AttributeNames.REDIRECT_URL, targetUrl);

                response.sendRedirect(contextPath + "/login?error=login_required");
                return;
            }
        }

        // ── 5. CHO PHÉP ĐI TIẾP VỚI CÁC ROUTE CÔNG KHAI ───────────────────────
        chain.doFilter(request, response);
    }

    /**
     * Kiểm tra xem path có phải là file tĩnh không
     */
    private boolean isStaticResource(String path) {
        if (path == null || path.isEmpty()) return false;
        for (String prefix : STATIC_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        for (String ext : STATIC_EXTENSIONS) {
            if (path.toLowerCase().endsWith(ext)) return true;
        }
        return false;
    }

    /**
     * Kiểm tra xem path có thuộc danh sách route yêu cầu đăng nhập khách hàng không
     */
    private boolean isProtectedCustomerRoute(String path) {
        if (path == null) return false;
        for (String protectedRoute : PROTECTED_CUSTOMER_ROUTES) {
            if (path.equals(protectedRoute) || path.startsWith(protectedRoute + "/")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // Hủy filter
    }
}
