<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập | SportShop — Đồ Thể Thao Chính Hãng</title>

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
            <h1 class="auth-card__title">Đăng Nhập</h1>
            <p class="auth-card__subtitle">Chào mừng bạn quay lại! Vui lòng nhập thông tin để tiếp tục.</p>

            <!-- Dynamic Error Alert -->
            <c:if test="${not empty errorMessage}">
                <div class="auth-alert auth-alert-danger" role="alert">
                    <span>⚠️</span>
                    <div>${errorMessage}</div>
                </div>
            </c:if>

            <!-- Dynamic Success / Flash Alert -->
            <c:if test="${not empty flashSuccess}">
                <div class="auth-alert auth-alert-success" role="alert">
                    <span>✓</span>
                    <div>${flashSuccess}</div>
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form" id="loginForm">
                <input type="hidden" name="action" value="login">

                <!-- Email Input -->
                <div class="form-group">
                    <label for="email" class="form-label">Địa chỉ Email <span class="text-error">*</span></label>
                    <input type="email"
                           id="email"
                           name="email"
                           class="form-control"
                           placeholder="name@example.com"
                           value="${param.email}"
                           required
                           autocomplete="email"
                           autofocus>
                </div>

                <!-- Password Input -->
                <div class="form-group">
                    <div class="d-flex justify-content-between align-items-center mb-1">
                        <label for="password" class="form-label mb-0">Mật khẩu <span class="text-error">*</span></label>
                        <a href="#" class="auth-link text-xs">Quên mật khẩu?</a>
                    </div>
                    <input type="password"
                           id="password"
                           name="password"
                           class="form-control"
                           placeholder="••••••••"
                           required
                           autocomplete="current-password">
                </div>

                <!-- Remember Me Checkbox -->
                <div class="auth-options">
                    <label class="auth-checkbox">
                        <input type="checkbox" name="rememberMe" id="rememberMe" value="on" checked>
                        <span>Ghi nhớ đăng nhập (30 ngày)</span>
                    </label>
                </div>

                <!-- Submit Button -->
                <button type="submit" class="auth-btn-submit" id="btnSubmit">
                    ĐĂNG NHẬP
                </button>
            </form>

            <!-- Test Accounts Quick Info Box -->
            <div class="auth-test-accounts">
                <div class="auth-test-accounts__title">💡 Tài khoản thử nghiệm có sẵn:</div>
                <div>• <strong>Admin:</strong> <code>admin@sportshop.vn</code> / <code>Admin@123</code></div>
                <div>• <strong>Customer:</strong> <code>customer@sportshop.vn</code> / <code>User@123</code></div>
            </div>
        </div>

        <!-- Auth Footer Link -->
        <div class="auth-footer">
            Chưa có tài khoản SportShop? 
            <a href="${pageContext.request.contextPath}/register" class="auth-link">Tạo tài khoản mới</a>
        </div>
    </div>

</body>
</html>
