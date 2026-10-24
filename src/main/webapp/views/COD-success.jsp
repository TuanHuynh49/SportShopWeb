<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Thanh toán thành công | SportShopWeb</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/order-sucess.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
</head>

<body>
    <jsp:include page="/views/common/header.jsp" />

    <main class="success-page">
        <div class="success-container">
            <section class="success-card">

                <!-- Thông báo thành công -->
                <header class="success-header">
                    <div class="success-icon" aria-hidden="true">
                        &#10003;
                    </div>

                    <p class="eyebrow">SPORTSHOPWEB</p>

                    <h1>Thanh toán thành công!</h1>

                    <p class="success-message">
                        Cảm ơn bạn đã mua sắm tại SportShopWeb.
                        Đơn hàng của bạn đã được xác nhận và thanh toán thành công.
                    </p>
                </header>

                <div class="content">

                    <!-- Thông tin đơn hàng -->
                    <h2 class="section-title">Thông tin đơn hàng</h2>

                    <div class="order-info">
                        <div class="info-row">
                            <span class="info-label">Mã đơn hàng</span>

                            <strong class="info-value">
                                <c:choose>
                                    <c:when test="${not empty orderId}">
                                        #<c:out value="${orderId}" />
                                    </c:when>
                                    <c:otherwise>
                                        Đang cập nhật
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                        </div>

                        <div class="info-row">
                            <span class="info-label">Trạng thái đơn hàng</span>

                            <span class="status-badge">
                                <span class="status-dot"></span>
                                Đã hoàn tất
                            </span>
                        </div>

                        <div class="info-row">
                            <span class="info-label">Phương thức thanh toán</span>

                            <strong class="info-value">
                                Thanh toán khi nhận hàng (COD)
                            </strong>
                        </div>

                        <div class="info-row">
                            <span class="info-label">Trạng thái thanh toán</span>

                            <span class="status-badge">
                                <span class="status-dot"></span>
                                Thành công
                            </span>
                        </div>
                    </div>

                    <!-- Danh sách sản phẩm -->
                    <h2 class="section-title product-section-title">
                        Sản phẩm đã đặt
                    </h2>

                    <div class="ordered-products">
                        <c:choose>
                            <c:when test="${not empty orderItems}">

                                <c:forEach var="item" items="${orderItems}">
                                    <div class="ordered-product">

                                        <div class="ordered-product-image">
                                            <c:if test="${not empty item.productVariant.product.images}">
                                                <img
                                                    src="<c:out value='${item.productVariant.product.images[0].imageUrl}' />"
                                                    alt="<c:out value='${item.productVariant.variantName}' />"
                                                    class="ordered-product-img"
                                                    loading="lazy"
                                                    onerror="this.style.display='none';">
                                            </c:if>
                                        </div>

                                        <div class="ordered-product-details">
                                            <strong class="ordered-product-name">
                                                <c:out value="${item.productVariant.variantName}" />
                                            </strong>

                                            <span class="ordered-product-quantity">
                                                Số lượng:
                                                <c:out value="${item.quantity}" />
                                            </span>

                                            <strong class="ordered-product-price">
                                                <fmt:formatNumber
                                                    value="${item.productVariant.price * item.quantity}"
                                                    type="number"
                                                    groupingUsed="true"
                                                    maxFractionDigits="0" /> ₫
                                            </strong>
                                        </div>

                                    </div>
                                </c:forEach>

                            </c:when>

                            <c:otherwise>
                                <p class="ordered-products-empty">
                                    Chưa có thông tin chi tiết sản phẩm.
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Tổng tiền -->
                    <div class="total-box">
                        <span class="total-label">
                            Tổng tiền thanh toán
                        </span>

                        <strong class="total-amount">
                            <c:choose>
                                <c:when test="${not empty orderTotal}">
                                    <fmt:formatNumber
                                        value="${orderTotal}"
                                        type="number"
                                        groupingUsed="true"
                                        maxFractionDigits="0" /> ₫
                                </c:when>

                                <c:otherwise>
                                    Đang cập nhật
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <!-- Thông báo -->
                    <section class="payment-card">
                        <h2 class="payment-title">
                            Đơn hàng đã được xử lý
                        </h2>

                        <p class="payment-description">
                            Hệ thống đã ghi nhận đơn hàng và hoàn tất
                            quy trình thanh toán minh họa.
                            Cảm ơn bạn đã tin tưởng SportShopWeb!
                        </p>
                    </section>

                    <!-- Điều hướng -->
                    <div class="actions">
                        <a
                            class="btn btn-secondary"
                            href="${pageContext.request.contextPath}/HomeServlet">
                            &#8962; Tiếp tục mua sắm
                        </a>

                        <a
                            class="btn btn-secondary"
                            href="${pageContext.request.contextPath}/orders">
                            Xem lịch sử đơn hàng
                        </a>
                    </div>

                </div>
            </section>
        </div>
    </main>

    <jsp:include page="/views/common/footer.jsp" />
</body>
</html>