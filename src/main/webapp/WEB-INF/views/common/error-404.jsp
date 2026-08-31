<%-- error-404.jsp — Trang lỗi 404 Not Found --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 — Không tìm thấy trang | SportShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css?v=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css?v=1.0">
    <style>
        body{display:flex;align-items:center;justify-content:center;min-height:100vh;
             background:linear-gradient(135deg,var(--color-primary) 0%,#0D3320 100%);}
        .err-box{background:#fff;border-radius:var(--radius-xl);padding:var(--space-12) var(--space-16);
                 text-align:center;max-width:520px;width:100%;box-shadow:var(--shadow-xl);animation:scaleIn .4s ease;}
        .err-code{font-family:var(--font-heading);font-size:6rem;font-weight:800;
                  color:var(--color-primary);line-height:1;margin-bottom:var(--space-4);}
        .err-title{font-family:var(--font-heading);font-size:var(--fs-h2);font-weight:700;
                   color:var(--color-text);margin-bottom:var(--space-3);}
        .err-desc{color:var(--color-text-muted);font-size:var(--fs-small);
                  line-height:1.7;margin-bottom:var(--space-8);}
    </style>
</head>
<body>
    <div class="err-box">
        <div class="err-code">404</div>
        <div class="err-title">Trang không tồn tại</div>
        <p class="err-desc">
            Trang bạn đang tìm kiếm có thể đã bị xoá, đổi tên, hoặc chưa bao giờ tồn tại.
            Hãy kiểm tra lại URL hoặc quay về trang chủ.
        </p>
        <div style="display:flex;gap:var(--space-3);justify-content:center;flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/home"
               class="btn btn-primary btn-lg">🏠 Về trang chủ</a>
            <a href="javascript:history.back()"
               class="btn btn-outline-secondary btn-lg">← Quay lại</a>
        </div>
    </div>
</body>
</html>
