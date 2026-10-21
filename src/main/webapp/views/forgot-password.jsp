<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forgot Password - SPORTSHOP</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/account.css">
    </head>
    <body>

        <!-- Nhúng Header dùng chung -->
        <jsp:include page="/views/common/header.jsp" />

        <main class="auth-wrapper">
            <div class="auth-split-card auth-split-card-forgot">
                
                <!-- CỘT TRÁI: BANNER HÌNH ẢNH & THÔNG ĐIỆP -->
                <div class="auth-banner-pane">
                    <div class="banner-top">
                        <div class="banner-brand">
                            <i class="fa-solid fa-bolt"></i>
                            <span>SPORTSHOP</span>
                        </div>
                    </div>

                    <div class="banner-middle">
                        <h2 class="banner-title">
                            Account <span class="highlight-text">Security</span>
                        </h2>
                        <p class="banner-desc">
                            Don't worry, it happens to the best of us. We will help you recover access to your account quickly and safely.
                        </p>

                        <div class="banner-features">
                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-shield-halved"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Secure Recovery</div>
                                    <div class="feature-text-sub">OTP code sent directly to your email</div>
                                </div>
                            </div>
                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-clock"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Fast & Easy</div>
                                    <div class="feature-text-sub">Reset your password in under 2 minutes</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="banner-bottom">
                        <p class="banner-slogan">Better Performance, Brighter Future</p>
                    </div>
                </div>

                <!-- CỘT PHẢI: FORM NHẬP EMAIL -->
                <div class="auth-form-pane">
                    
                    <div class="form-top-bar">
                        <a href="${pageContext.request.contextPath}/LoginServlet" class="back-link">
                            <i class="fa-solid fa-arrow-left"></i> Back to Sign In
                        </a>
                    </div>

                    <div class="auth-header">
                        <h2>Forgot Password?</h2>
                        <p>Enter your registered email address to receive a one-time verification code (OTP).</p>
                    </div>

                    <!-- Thông báo lỗi (nếu có) -->
                    <c:if test="${not empty error}">
                        <div class="auth-alert-error">
                            <i class="fa-solid fa-circle-exclamation"></i> ${error}
                        </div>
                    </c:if>

                    <!-- Form gửi yêu cầu cấp mã OTP -->
                    <form action="${pageContext.request.contextPath}/ForgotPasswordServlet" method="post">
                        <input type="hidden" name="action" value="sendOtp">

                        <div class="form-group">
                            <label for="email" class="form-label">Email Address</label>
                            <div class="input-with-icon">
                                <i class="fa-regular fa-envelope input-icon"></i>
                                <input type="email" 
                                       id="email" 
                                       name="email" 
                                       class="form-input has-icon" 
                                       placeholder="name@example.com" 
                                       value="${email}" 
                                       required>
                            </div>
                        </div>

                        <button type="submit" class="auth-btn">
                            <span>Send Verification Code</span>
                            <i class="fa-solid fa-paper-plane"></i>
                        </button>

                        <div class="auth-footer">
                            Remember your password? 
                            <a href="${pageContext.request.contextPath}/LoginServlet" class="form-link">Sign In</a>
                        </div>
                    </form>

                </div>

            </div>
        </main>

        <!-- Nhúng Footer dùng chung -->
        <jsp:include page="/views/common/footer.jsp" />

    </body>
</html>
