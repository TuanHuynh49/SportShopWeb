<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán thất bại | SportShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/vnpay-result.css">
</head>
<body>
    <div class="result-container">
        <div class="result-card result-fail">
            <div class="result-icon-wrapper">
                <span class="icon">&#10006;</span>
            </div>
            <h1 class="result-title">Thanh toán thất bại!</h1>
            <p class="result-desc">
                Giao dịch của bạn chưa được xác nhận thành công. Vui lòng kiểm tra lại trạng thái đơn hàng hoặc thử thanh toán lại.
            </p>

            <c:if test="${not empty param.reason}">
                <div class="reason-box">
                    <span class="reason-label">Lý do:</span>
                    <span class="reason-text">
                        <c:choose>
                            <c:when test="${param.reason eq 'invalid_signature'}">
                                Chữ ký bảo mật không khớp hoặc không hợp lệ (Invalid Signature).
                            </c:when>
                            <c:when test="${param.reason eq 'payment_failed'}">
                                Giao dịch bị hủy hoặc thanh toán không thành công tại cổng VNPay.
                            </c:when>
                            <c:when test="${param.reason eq 'amount_mismatch'}">
                                Số tiền thanh toán không khớp với số tiền đơn hàng trong hệ thống.
                            </c:when>
                            <c:when test="${param.reason eq 'session_expired'}">
                                Phiên giao dịch thanh toán đã hết hạn.
                            </c:when>
                            <c:when test="${param.reason eq 'order_not_found'}">
                                Không tìm thấy đơn hàng cần thanh toán.
                            </c:when>
                            <c:when test="${param.reason eq 'missing_data'}">
                                Thiếu dữ liệu phản hồi từ cổng thanh toán.
                            </c:when>
                            <c:when test="${param.reason eq 'invalid_amount'}">
                                Định dạng số tiền trả về từ VNPay không hợp lệ.
                            </c:when>
                            <c:otherwise>
                                <c:out value="${param.reason}"/>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </c:if>

            <div class="result-actions">
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">
                    Xem danh sách đơn hàng
                </a>
                <a href="${pageContext.request.contextPath}/HomeServlet" class="btn btn-secondary">
                    Quay về trang chủ
                </a>
            </div>
        </div>
    </div>
</body>
</html>
