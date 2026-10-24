<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset Password - SPORTSHOP</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/account.css">
    </head>
    <body>

        <!-- Nhúng Header dùng chung -->
        <jsp:include page="/views/common/header.jsp" />

        <main class="auth-wrapper">
            <div class="auth-split-card auth-split-card-forgot">
                
                <!-- CỘT TRÁI: BANNER THÔNG ĐIỆP -->
                <div class="auth-banner-pane">
                    <div class="banner-top">
                        <div class="banner-brand">
                            <i class="fa-solid fa-bolt"></i>
                            <span>SPORTSHOP</span>
                        </div>
                    </div>

                    <div class="banner-middle">
                        <h2 class="banner-title">
                            Set New <span class="highlight-text">Password</span>
                        </h2>
                        <p class="banner-desc">
                            Please choose a strong password that you haven't used before to ensure your account remains safe.
                        </p>

                        <div class="banner-features">
                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-envelope-circle-check"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Code Sent</div>
                                    <div class="feature-text-sub">Valid for 5 minutes</div>
                                </div>
                            </div>
                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-lock"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Strong Security</div>
                                    <div class="feature-text-sub">Encrypted with modern hash standards</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="banner-bottom">
                        <p class="banner-slogan">Better Performance, Brighter Future</p>
                    </div>
                </div>

                <!-- CỘT PHẢI: FORM NHẬP OTP & ĐẶT MẬT KHẨU MỚI -->
                <div class="auth-form-pane">
                    
                    <div class="form-top-bar">
                        <a href="${pageContext.request.contextPath}/ForgotPasswordServlet" class="back-link">
                            <i class="fa-solid fa-arrow-left"></i> Change email
                        </a>
                    </div>

                    <div class="auth-header">
                        <h2>Reset Password</h2>
                        <p>We sent a 6-digit verification code to <strong><c:out value="${resetEmail}" /></strong>.</p>
                    </div>

                    <!-- Thông báo lỗi (nếu có) -->
                    <c:if test="${not empty error}">
                        <div class="auth-alert-error">
                            <i class="fa-solid fa-circle-exclamation"></i> ${error}
                        </div>
                    </c:if>

                    <!-- Thông báo thành công (nếu gửi lại mã) -->
                    <c:if test="${not empty message}">
                        <div class="auth-alert-success">
                            <i class="fa-solid fa-circle-check"></i> ${message}
                        </div>
                    </c:if>

                    <!-- Form xác nhận OTP & mật khẩu mới -->
                    <form action="${pageContext.request.contextPath}/ForgotPasswordServlet" method="post">
                        <input type="hidden" name="action" value="verifyAndReset">

                        <!-- Nhập OTP 6 số -->
                        <div class="form-group">
                            <label for="otp" class="form-label">Verification Code (OTP)</label>
                            <div class="input-with-icon">
                                <i class="fa-solid fa-shield-halved input-icon"></i>
                                <input type="text" 
                                       id="otp" 
                                       name="otp" 
                                       class="form-input has-icon" 
                                       placeholder="6-digit code" 
                                       maxlength="6" 
                                       pattern="[0-9]{6}" 
                                       required>
                            </div>
                        </div>

                        <!-- Nhập Mật khẩu mới -->
                        <div class="form-group">
                            <label for="newPassword" class="form-label">New Password</label>
                            <div class="input-with-icon">
                                <i class="fa-solid fa-lock input-icon"></i>
                                <input type="password" 
                                       id="newPassword" 
                                       name="newPassword" 
                                       class="form-input has-icon has-toggle" 
                                       placeholder="At least 6 characters" 
                                       required>
                                <button type="button" class="btn-toggle-password" title="Show password">
                                    <i class="fa-regular fa-eye"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Xác nhận mật khẩu mới -->
                        <div class="form-group">
                            <label for="confirmPassword" class="form-label">Confirm New Password</label>
                            <div class="input-with-icon">
                                <i class="fa-solid fa-lock input-icon"></i>
                                <input type="password" 
                                       id="confirmPassword" 
                                       name="confirmPassword" 
                                       class="form-input has-icon has-toggle" 
                                       placeholder="Re-enter new password" 
                                       required>
                                <button type="button" class="btn-toggle-password" title="Show password">
                                    <i class="fa-regular fa-eye"></i>
                                </button>
                            </div>
                        </div>

                        <button type="submit" class="auth-btn">
                            <span>Update Password</span>
                            <i class="fa-solid fa-check"></i>
                        </button>
                    </form>

                    <!-- Gửi lại mã OTP nếu chưa nhận được -->
                    <div class="auth-footer" style="margin-top: 18px;">
                        Didn't receive the code? 
                        <form action="${pageContext.request.contextPath}/ForgotPasswordServlet" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="sendOtp">
                            <input type="hidden" name="email" value="${resetEmail}">
                            <button type="submit" class="form-link" style="background:none;border:none;cursor:pointer;font-weight:600;padding:0;">
                                Resend OTP
                            </button>
                        </form>
                    </div>

                </div>

            </div>
        </main>

        <!-- Nhúng Footer dùng chung -->
        <jsp:include page="/views/common/footer.jsp" />

        <!-- Nhúng script hỗ trợ nhập số OTP và ẩn hiện mật khẩu -->
        <script src="${pageContext.request.contextPath}/assets/js/verify-otp.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/password-toggle.js"></script>

    </body>
</html>
