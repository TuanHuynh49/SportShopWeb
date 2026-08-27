<%-- error-500.jsp — Trang lỗi 500 Internal Server Error --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 — Lỗi hệ thống | SportShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">
    <style>
        body{display:flex;align-items:center;justify-content:center;min-height:100vh;
             background:linear-gradient(135deg,#1a1a2e 0%,#16213e 100%);}
        .err-box{background:#fff;border-radius:var(--radius-xl);padding:var(--space-12) var(--space-16);
                 text-align:center;max-width:520px;width:100%;box-shadow:var(--shadow-xl);}
        .err-code{font-family:var(--font-heading);font-size:6rem;font-weight:800;
                  color:var(--color-danger);line-height:1;margin-bottom:var(--space-4);}
        .err-title{font-family:var(--font-heading);font-size:var(--fs-h2);font-weight:700;
                   color:var(--color-text);margin-bottom:var(--space-3);}
        .err-desc{color:var(--color-text-muted);font-size:var(--fs-small);
                  line-height:1.7;margin-bottom:var(--space-8);}
    </style>
</head>
<body>
    <div class="err-box">
        <div class="err-code">500</div>
        <div class="err-title">Lỗi hệ thống</div>
        <p class="err-desc">
            Máy chủ gặp sự cố không mong muốn. Chúng tôi đã ghi nhận lỗi và đang khắc phục.
            Vui lòng thử lại sau hoặc liên hệ hỗ trợ nếu tình trạng tiếp diễn.
        </p>
        <div style="display:flex;gap:var(--space-3);justify-content:center;flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/home"
               class="btn btn-primary btn-lg">🏠 Về trang chủ</a>
            <a href="javascript:location.reload()"
               class="btn btn-outline-secondary btn-lg">🔄 Thử lại</a>
        </div>
    </div>
</body>
</html>
