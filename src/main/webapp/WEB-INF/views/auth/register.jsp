<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký Tài Khoản | SportShop — Đồ Thể Thao Chính Hãng</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- CSS Tokens & Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body class="auth-page">

    <div class="auth-container">
        <!-- Brand Header -->
        <a href="${pageContext.request.contextPath}/" class="auth-brand">
            <div class="auth-brand__logo">S</div>
            <div class="auth-brand__name">SPORT<span>SHOP</span></div>
            <div class="auth-brand__tagline">Athletic Performance Gear</div>
        </a>

        <!-- Main Auth Card -->
        <div class="auth-card">
            <h1 class="auth-card__title">Đăng Ký Tài Khoản</h1>
            <p class="auth-card__subtitle">Gia nhập cộng đồng thể thao SportShop để nhận nhiều ưu đãi.</p>

            <!-- Dynamic Error Alert -->
            <c:if test="${not empty errorMessage}">
                <div class="auth-alert auth-alert-danger" id="serverAlert" role="alert">
                    <span>⚠️</span>
                    <div>${errorMessage}</div>
                </div>
            </c:if>

            <!-- Client-side Validation Alert Box (ẩn mặc định) -->
            <div class="auth-alert auth-alert-danger" id="clientAlert" style="display: none;" role="alert">
                <span>⚠️</span>
                <div id="clientAlertText"></div>
            </div>

            <!-- Register Form -->
            <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form" id="registerForm" novalidate>
                <input type="hidden" name="action" value="register">

                <!-- Full Name -->
                <div class="form-group">
                    <label for="fullName" class="form-label">Họ và tên <span class="text-error">*</span></label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           class="form-control"
                           placeholder="Nguyễn Văn A"
                           value="${param.fullName}"
                           required
                           autofocus>
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label for="email" class="form-label">Địa chỉ Email <span class="text-error">*</span></label>
                    <input type="email"
                           id="email"
                           name="email"
                           class="form-control"
                           placeholder="name@example.com"
                           value="${param.email}"
                           required
                           autocomplete="email">
                </div>

                <!-- Phone -->
                <div class="form-group">
                    <label for="phone" class="form-label">Số điện thoại <span class="text-error">*</span></label>
                    <input type="tel"
                           id="phone"
                           name="phone"
                           class="form-control"
                           placeholder="0912345678"
                           value="${param.phone}"
                           required
                           autocomplete="tel">
                </div>

                <!-- Password -->
                <div class="form-group">
                    <label for="password" class="form-label">Mật khẩu <span class="text-error">*</span></label>
                    <input type="password"
                           id="password"
                           name="password"
                           class="form-control"
                           placeholder="Tối thiểu 8 ký tự"
                           required
                           autocomplete="new-password">
                </div>

                <!-- Confirm Password -->
                <div class="form-group">
                    <label for="confirmPassword" class="form-label">Xác nhận mật khẩu <span class="text-error">*</span></label>
                    <input type="password"
                           id="confirmPassword"
                           name="confirmPassword"
                           class="form-control"
                           placeholder="Nhập lại mật khẩu"
                           required
                           autocomplete="new-password">
                </div>

                <!-- Submit Button -->
                <button type="submit" class="auth-btn-submit" id="btnRegister">
                    TẠO TÀI KHOẢN
                </button>
            </form>
        </div>

        <!-- Auth Footer Link -->
        <div class="auth-footer">
            Đã có tài khoản SportShop? 
            <a href="${pageContext.request.contextPath}/login" class="auth-link">Đăng nhập ngay</a>
        </div>
    </div>

    <!-- Client-side Validation Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const form = document.getElementById('registerForm');
            const clientAlert = document.getElementById('clientAlert');
            const clientAlertText = document.getElementById('clientAlertText');

            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const phoneRegex = /^(0|\+84)[3|5|7|8|9][0-9]{8}$/;

            function showError(message, inputElement) {
                clientAlertText.textContent = message;
                clientAlert.style.display = 'flex';
                if (inputElement) {
                    inputElement.classList.add('is-invalid');
                    inputElement.focus();
                }
            }

            function clearErrors() {
                clientAlert.style.display = 'none';
                form.querySelectorAll('.form-control').forEach(function (el) {
                    el.classList.remove('is-invalid');
                });
            }

            form.addEventListener('submit', function (e) {
                clearErrors();

                const fullName = document.getElementById('fullName');
                const email = document.getElementById('email');
                const phone = document.getElementById('phone');
                const password = document.getElementById('password');
                const confirmPassword = document.getElementById('confirmPassword');

                if (!fullName.value.trim()) {
                    e.preventDefault();
                    showError('Vui lòng nhập họ và tên của bạn.', fullName);
                    return;
                }

                if (!email.value.trim() || !emailRegex.test(email.value.trim())) {
                    e.preventDefault();
                    showError('Vui lòng nhập địa chỉ email hợp lệ.', email);
                    return;
                }

                if (!phone.value.trim() || !phoneRegex.test(phone.value.trim())) {
                    e.preventDefault();
                    showError('Vui lòng nhập số điện thoại hợp lệ (10 chữ số, bắt đầu bằng 0 hoặc +84).', phone);
                    return;
                }

                if (password.value.length < 8) {
                    e.preventDefault();
                    showError('Mật khẩu phải chứa ít nhất 8 ký tự.', password);
                    return;
                }

                if (password.value !== confirmPassword.value) {
                    e.preventDefault();
                    showError('Mật khẩu xác nhận không khớp với mật khẩu đã nhập.', confirmPassword);
                    return;
                }
            });
        });
    </script>
</body>
</html>
