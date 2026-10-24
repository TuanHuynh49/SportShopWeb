<%-- 
    Document   : register
    Created on : 10 Oct 2026, 3:36:43 PM
    Author     : Anh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Account - SPORTSHOP</title>
        <!-- Nhúng CSS dùng chung và CSS riêng cho Account -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/account.css">
    </head>
    <body>

        <!-- Nhúng Header dùng chung -->
        <jsp:include page="/views/common/header.jsp" />

        <!-- Phần nội dung chính: Bố cục 2 cột (Split-Screen Card) đồng bộ với trang Login -->
        <main class="auth-wrapper">
            <div class="auth-split-card auth-split-card-register">
                
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

                    <!-- Phần giữa: Tiêu đề lớn & 3 điểm nổi bật cho thành viên mới -->
                    <div class="banner-middle">
                        <h2 class="banner-title">
                            Join The Club,<br>
                            Unlock <span class="highlight-text">Privileges</span>
                        </h2>
                        <p class="banner-desc">
                            Create your free account today and experience premier sportswear, member rewards, and priority support.
                        </p>

                        <!-- Danh sách 3 ưu điểm dành cho thành viên -->
                        <div class="banner-features">
                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-gift"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Welcome Voucher 15%</div>
                                    <div class="feature-text-sub">Instant discount on your first online order</div>
                                </div>
                            </div>

                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-truck-fast"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Free Express Shipping</div>
                                    <div class="feature-text-sub">Fast delivery on qualifying club orders</div>
                                </div>
                            </div>

                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-medal"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Tier Loyalty Points</div>
                                    <div class="feature-text-sub">Earn reward points on every gear purchase</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Phần dưới: Slogan kết bài -->
                    <div class="banner-bottom">
                        <p class="banner-slogan">Better Performance, Brighter Future</p>
                    </div>

                </div>

                <!-- CỘT PHẢI: FORM ĐĂNG KÝ (NỀN TRẮNG, CHỮ ĐEN RÕ NÉT THEO CHUẨN LOGIN) -->
                <div class="auth-form-pane">
                    
                    <!-- Tiện ích góc trên: Ngôn ngữ -->
                    <div class="form-top-bar">
                        <span class="lang-badge">
                            <i class="fa-solid fa-globe"></i> English
                        </span>
                    </div>

                    <!-- Tiêu đề form -->
                    <div class="auth-header">
                        <h2>Create Account</h2>
                        <p>Join the SPORTSHOP community to access exclusive deals</p>
                    </div>

                    <!-- Hiển thị thông báo lỗi từ RegisterServlet nếu có -->
                    <c:if test="${not empty error}">
                        <div class="auth-alert-error">
                            <i class="fa-solid fa-circle-exclamation"></i> ${error}
                        </div>
                    </c:if>

                    <!-- Hiển thị thông báo thành công nếu có -->
                    <c:if test="${not empty message}">
                        <div class="auth-alert-success">
                            <i class="fa-solid fa-circle-check"></i> ${message}
                        </div>
                    </c:if>

                    <!-- Form gửi dữ liệu POST tới RegisterServlet -->
                    <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
                        
                        <!-- Họ và Tên (Chia 2 cột ngang hàng) -->
                        <div class="form-row">
                            <div class="form-group">
                                <label for="firstName" class="form-label">First Name</label>
                                <div class="input-with-icon">
                                    <i class="fa-regular fa-user input-icon"></i>
                                    <input type="text" 
                                           id="firstName" 
                                           name="firstName" 
                                           class="form-input has-icon" 
                                           placeholder="e.g. John" 
                                           value="${firstName}" 
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="lastName" class="form-label">Last Name</label>
                                <div class="input-with-icon">
                                    <i class="fa-regular fa-user input-icon"></i>
                                    <input type="text" 
                                           id="lastName" 
                                           name="lastName" 
                                           class="form-input has-icon" 
                                           placeholder="e.g. Doe" 
                                           value="${lastName}" 
                                           required>
                                </div>
                            </div>
                        </div>

                        <!-- Email -->
                        <div class="form-group">
                            <label for="email" class="form-label">Email Address</label>
                            <div class="input-with-icon">
                                <i class="fa-regular fa-envelope input-icon"></i>
                                <input type="email" 
                                       id="email" 
                                       name="email" 
                                       class="form-input has-icon" 
                                       placeholder="e.g. customer@sportshop.vn" 
                                       value="${email}" 
                                       required>
                            </div>
                        </div>

                        <!-- Số điện thoại và Giới tính (Chia 2 cột ngang hàng) -->
                        <div class="form-row">
                            <div class="form-group">
                                <label for="phone" class="form-label">Phone Number</label>
                                <div class="input-with-icon">
                                    <i class="fa-solid fa-phone input-icon"></i>
                                    <input type="tel" 
                                           id="phone" 
                                           name="phone" 
                                           class="form-input has-icon" 
                                           placeholder="e.g. 0901234567" 
                                           value="${phone}" 
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="gender" class="form-label">Gender</label>
                                <div class="input-with-icon">
                                    <i class="fa-solid fa-venus-mars input-icon"></i>
                                    <select id="gender" name="gender" class="form-select has-icon">
                                        <option value="Male" ${gender == 'Male' ? 'selected' : ''}>Male</option>
                                        <option value="Female" ${gender == 'Female' ? 'selected' : ''}>Female</option>
                                        <option value="Other" ${gender == 'Other' ? 'selected' : ''}>Other</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Mật khẩu -->
                        <div class="form-group">
                            <label for="password" class="form-label">Password</label>
                            <div class="input-with-icon">
                                <i class="fa-solid fa-lock input-icon"></i>
                                <input type="password" 
                                       id="password" 
                                       name="password" 
                                       class="form-input has-icon has-toggle" 
                                       placeholder="At least 6 characters" 
                                       required>
                                <button type="button" class="btn-toggle-password" title="Show password" aria-label="Toggle password visibility">
                                    <i class="fa-regular fa-eye"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Xác nhận mật khẩu -->
                        <div class="form-group">
                            <label for="confirmPassword" class="form-label">Confirm Password</label>
                            <div class="input-with-icon">
                                <i class="fa-solid fa-shield-halved input-icon"></i>
                                <input type="password" 
                                       id="confirmPassword" 
                                       name="confirmPassword" 
                                       class="form-input has-icon has-toggle" 
                                       placeholder="Re-enter your password" 
                                       required>
                                <button type="button" class="btn-toggle-password" title="Show password" aria-label="Toggle password visibility">
                                    <i class="fa-regular fa-eye"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Điều khoản sử dụng -->
                        <div class="form-group">
                            <label class="form-checkbox-label">
                                <input type="checkbox" name="terms" required>
                                <span>I agree to the <a href="#" class="form-link">Terms of Service</a> &amp; <a href="#" class="form-link">Privacy Policy</a></span>
                            </label>
                        </div>

                        <!-- Nút xác nhận đăng ký -->
                        <button type="submit" class="auth-btn">
                            <span>Create Account</span>
                            <i class="fa-solid fa-arrow-right"></i>
                        </button>

                        <!-- Chuyển hướng sang trang đăng nhập -->
                        <div class="auth-footer">
                            Already have an account? 
                            <a href="${pageContext.request.contextPath}/LoginServlet" class="form-link">Sign In</a>
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
