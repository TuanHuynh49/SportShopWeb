<%-- 
    Document   : verify-otp
    Created on : 19 Oct 2026, 8:20:00 PM
    Author     : Anh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Verify OTP - SPORTSHOP</title>
        <!-- Nhúng CSS dùng chung và CSS Account -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/account.css">
    </head>
    <body>

        <!-- Header dùng chung -->
        <jsp:include page="/views/common/header.jsp" />

        <!-- Giao diện Split-Screen Card đồng bộ -->
        <main class="auth-wrapper">
            <div class="auth-split-card" style="max-width: 980px;">
                
                <!-- CỘT TRÁI: BANNER THÔNG ĐIỆP BẢO MẬT -->
                <div class="auth-banner-pane">
                    
                    <div class="banner-top">
                        <div class="banner-brand">
                            <i class="fa-solid fa-bolt"></i>
                            <span>SPORTSHOP</span>
                        </div>
                    </div>

                    <div class="banner-middle">
                        <h2 class="banner-title">
                            Secure Your<br>
                            <span class="highlight-text">Sports Account</span>
                        </h2>
                        <p class="banner-desc">
                            We take your security seriously. Two-step verification protects your order history, delivery addresses, and VIP tier benefits.
                        </p>

                        <div class="banner-features">
                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-shield-halved"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Verified Identity</div>
                                    <div class="feature-text-sub">Ensure you own your email address</div>
                                </div>
                            </div>

                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-envelope-open-text"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Instant Delivery</div>
                                    <div class="feature-text-sub">Receive OTP immediately in your Gmail inbox</div>
                                </div>
                            </div>

                            <div class="banner-feature-item">
                                <div class="feature-icon-box">
                                    <i class="fa-solid fa-gift"></i>
                                </div>
                                <div>
                                    <div class="feature-text-title">Instant Welcome Voucher</div>
                                    <div class="feature-text-sub">10% discount code WELCOME10 upon activation</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="banner-bottom">
                        <p class="banner-slogan">Authentic &bull; Premium &bull; Performance</p>
                    </div>

                </div>

                <!-- CỘT PHẢI: FORM NHẬP MÃ OTP -->
                <div class="auth-form-pane" style="text-align: center;">
                    
                    <div class="form-top-bar">
                        <a href="${pageContext.request.contextPath}/RegisterServlet" class="form-link" style="font-size: 0.85rem;">
                            <i class="fa-solid fa-arrow-left"></i> Change Info
                        </a>
                    </div>

                    <!-- Icon xác thực thư điện tử nổi bật -->
                    <div class="otp-header-icon">
                        <i class="fa-solid fa-envelope-circle-check"></i>
                    </div>

                    <!-- Tiêu đề form -->
                    <div class="auth-header" style="text-align: center; margin-bottom: 20px;">
                        <h2>Check Your Inbox</h2>
                        <p style="margin-top: 8px;">
                            We've sent a 6-digit verification code to<br>
                            <strong style="color: #0f172a; font-size: 1.02rem;">${sessionScope.reg_email}</strong>
                        </p>
                    </div>

                    <!-- Thông báo lỗi nếu có -->
                    <c:if test="${not empty error}">
                        <div class="auth-alert-error" style="text-align: left;">
                            <i class="fa-solid fa-circle-exclamation"></i>
                            <span>${error}</span>
                        </div>
                    </c:if>

                    <!-- Thông báo thành công nếu có (khi resend) -->
                    <c:if test="${not empty message}">
                        <div class="auth-alert-success" style="text-align: left;">
                            <i class="fa-solid fa-circle-check"></i>
                            <span>${message}</span>
                        </div>
                    </c:if>

                    <!-- Form xác nhận OTP -->
                    <form action="${pageContext.request.contextPath}/VerifyOtpServlet" method="post" id="otpForm">
                        <div class="form-group" style="margin-bottom: 8px;">
                            <label for="otp" class="form-label" style="text-align: center; margin-bottom: 12px; font-weight: 700; text-transform: uppercase; letter-spacing: 1px; color: #475569;">
                                Enter 6-Digit Code
                            </label>
                            <input type="text" 
                                   id="otp" 
                                   name="otp" 
                                   class="form-input otp-input" 
                                   placeholder="&bull;&bull;&bull;&bull;&bull;&bull;" 
                                   maxlength="6" 
                                   pattern="[0-9]{6}" 
                                   inputmode="numeric" 
                                   autocomplete="one-time-code" 
                                   required 
                                   autofocus>
                        </div>

                        <!-- Đồng hồ đếm ngược thời gian hết hạn OTP -->
                        <div class="otp-timer-box">
                            <i class="fa-regular fa-clock"></i>
                            <span>Code expires in: <strong id="countdownTimer" data-remaining="${remainingSeconds != null ? remainingSeconds : 300}">--:--</strong></span>
                        </div>

                        <!-- Nút xác nhận kích hoạt tài khoản -->
                        <button type="submit" class="auth-btn" style="width: 100%;">
                            <span>Verify &amp; Activate Account</span>
                            <i class="fa-solid fa-check"></i>
                        </button>
                    </form>

                    <!-- Phần gửi lại mã OTP -->
                    <div class="otp-resend-wrapper">
                        <span>Didn't receive the email? Check spam folder or </span>
                        <form action="${pageContext.request.contextPath}/VerifyOtpServlet" method="post" style="display: inline;">
                            <input type="hidden" name="action" value="resend">
                            <button type="submit" class="resend-btn" id="resendBtn">Resend Code</button>
                        </form>
                    </div>

                </div>

            </div>
        </main>

        <!-- Footer dùng chung -->
        <jsp:include page="/views/common/footer.jsp" />

        <!-- Nhúng file JS xử lý OTP riêng biệt -->
        <script src="${pageContext.request.contextPath}/assets/js/verify-otp.js"></script>

    </body>
</html>
