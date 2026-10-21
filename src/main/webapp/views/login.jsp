<%-- 
    Document   : login
    Created on : 10 Oct 2026, 3:36:24 PM
    Author     : Anh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign In - SPORTSHOP</title>
        <!-- Nhúng CSS dùng chung và CSS riêng cho Account -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/account.css">
    </head>
    <body>

        <!-- Nhúng Header dùng chung -->
        <jsp:include page="/views/common/header.jsp" />

        <!-- Phần nội dung chính: Bố cục 2 cột (Split-Screen Card) theo mẫu thiết kế -->
        <main class="auth-wrapper">
            <div class="auth-split-card">
                
                <!-- CỘT TRÁI: BANNER HÌNH ẢNH & THÔNG ĐIỆP -->
                <!-- Lưu ý: Bạn có thể tự đặt hình nền tùy thích trong account.css (.auth-banner-pane) -->
                <div class="auth-banner-pane">
                    
                    <!-- Phần trên: Logo thương hiệu -->
                    <div class="banner-top">
                        <div class="banner-brand">
                            <i class="fa-solid fa-bolt"></i>
                            <span>SPORTSHOP</span>
                        </div>
                    </div>

                    <!-- Phần giữa: Tiêu đề lớn & 3 điểm nổi bật -->
                    <div class="banner-middle">
                        <h2 class="banner-title">
                            Gear Up Today,<br>
                            Conquer <span class="highlight-text">Tomorrow</span>
                        </h2>
                        <p class="banner-desc">
                            Your ultimate destination for authentic sportswear, performance footwear, and training gear.
                        </p>

                        <!-- Danh sách 3 ưu điểm dịch vụ -->
                        <div class="banner-features">
                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-certificate"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">100% Authentic Gear</div>
                                    <div class="feature-text-sub">Guaranteed genuine sports products</div>
                                </div>
                            </div>

                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-truck-fast"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Express Delivery</div>
                                    <div class="feature-text-sub">Fast and secure shipping nationwide</div>
                                </div>
                            </div>

                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-shield-halved"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Member Privileges</div>
                                    <div class="feature-text-sub">Exclusive discounts & early access drops</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Phần dưới: Slogan kết bài -->
                    <div class="banner-bottom">
                        <p class="banner-slogan">Better Performance, Brighter Future</p>
                    </div>

                </div>

                <!-- CỘT PHẢI: FORM ĐĂNG NHẬP -->
                <div class="auth-form-pane">
                    
                    <!-- Tiện ích góc trên: Ngôn ngữ -->
                    <div class="form-top-bar">
                        <span class="lang-badge">
                            <i class="fa-solid fa-globe"></i> English
                        </span>
                    </div>

                    <!-- Tiêu đề form -->
                    <div class="auth-header">
                        <h2>Welcome Back!</h2>
                        <p>Please enter your details to sign in</p>
                    </div>

                    <!-- Hiển thị thông báo lỗi từ LoginServlet nếu có -->
                    <c:if test="${not empty error}">
                        <div class="auth-alert-error">
                            <i class="fa-solid fa-circle-exclamation"></i> ${error}
                        </div>
                    </c:if>

                    <!-- Hiển thị thông báo thành công (ví dụ sau khi đăng ký) -->
                    <c:if test="${not empty message}">
                        <div class="auth-alert-success">
                            <i class="fa-solid fa-circle-check"></i> ${message}
                        </div>
                    </c:if>

                    <!-- Form gửi dữ liệu POST tới LoginServlet -->
                    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                        
                        <!-- Nhập Email hoặc Số điện thoại -->
                        <div class="form-group">
                            <label for="identifier" class="form-label">Email or Phone Number</label>
                            <div class="input-with-icon">
                                <i class="fa-regular fa-envelope input-icon"></i>
                                <input type="text" 
                                       id="identifier" 
                                       name="identifier" 
                                       class="form-input has-icon" 
                                       placeholder="Enter your email or phone number" 
                                       value="${identifier}" 
                                       required>
                            </div>
                        </div>

                        <!-- Nhập Mật khẩu (Có nút bật/tắt ẩn hiện mật khẩu) -->
                        <div class="form-group">
                            <label for="password" class="form-label">Password</label>
                            <div class="input-with-icon">
                                <i class="fa-solid fa-lock input-icon"></i>
                                <input type="password" 
                                       id="password" 
                                       name="password" 
                                       class="form-input has-icon has-toggle" 
                                       placeholder="Enter your password" 
                                       required>
                                <button type="button" class="btn-toggle-password" title="Show password" aria-label="Toggle password visibility">
                                    <i class="fa-regular fa-eye"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Ghi nhớ đăng nhập & Quên mật khẩu -->
                        <div class="form-options">
                            <label class="form-checkbox-label">
                                <input type="checkbox" name="rememberMe" value="true" ${rememberMe ? 'checked' : ''}>
                                <span>Remember me</span>
                            </label>
                            <a href="#" class="form-link">Forgot password?</a>
                        </div>

                        <!-- Nút xác nhận đăng nhập -->
                        <button type="submit" class="auth-btn">
                            <span>Sign In</span>
                            <i class="fa-solid fa-arrow-right"></i>
                        </button>

                        <!-- Dòng phân cách "or" -->
                        <div class="auth-divider">
                            <span>or continue with</span>
                        </div>

                        <!-- Đăng nhập bằng Google / Apple (Giao diện hiển thị) -->
                        <div class="social-buttons">
                            <button type="button" class="btn-social">
                                <i class="fa-brands fa-google google-icon"></i>
                                <span>Sign in with Google</span>
                            </button>
                            <button type="button" class="btn-social">
                                <i class="fa-brands fa-apple apple-icon"></i>
                                <span>Sign in with Apple</span>
                            </button>
                        </div>

                        <!-- Chuyển hướng sang trang đăng ký -->
                        <div class="auth-footer">
                            Don't have an account? 
                            <a href="${pageContext.request.contextPath}/RegisterServlet" class="form-link">Sign up</a>
                        </div>

                    </form>

                </div>

            </div>
        </main>

        <!-- Nhúng Footer dùng chung -->
        <jsp:include page="/views/common/footer.jsp" />

        <!-- Nhúng script bật/tắt ẩn hiện mật khẩu -->
        <script src="${pageContext.request.contextPath}/assets/js/password-toggle.js"></script>

    </body>
</html>
