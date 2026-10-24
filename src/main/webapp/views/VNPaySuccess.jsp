<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán thành công | SportShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vnpay-result.css">
</head>
<body>
    <div class="result-container">
        <div class="result-card result-success">
            <div class="result-icon-wrapper">
                <span class="icon">&#10004;</span>
            </div>
            <h1 class="result-title">Thanh toán thành công!</h1>
            <p class="result-desc">
                Giao dịch VNPay đã được xác nhận thành công. Đơn hàng của bạn đã hoàn tất thanh toán và đang được xử lý giao hàng.
            </p>
            <div class="result-actions">
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">
                    Xem lịch sử đơn hàng
                </a>
                <a href="${pageContext.request.contextPath}/HomeServlet" class="btn btn-secondary">
                    Quay về trang chủ
                </a>
            </div>
        </div>
    </div>
</body>
</html>
