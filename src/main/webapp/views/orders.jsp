<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>My Orders | SportShopWeb</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/orders.css">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>

<body>

<jsp:include page="/views/common/header.jsp" />

<main class="orders-page">

    <!-- Page heading -->
    <section class="orders-heading">
        <div>
            <p class="orders-eyebrow">YOUR ACCOUNT</p>
            <h1 class="orders-title">My Orders</h1>
            <p class="orders-description">
                Track your orders and review your purchase history.
            </p>
        </div>

        <c:if test="${not empty orders}">
            <div class="orders-count">
                <c:out value="${orders.size()}" /> orders
            </div>
        </c:if>
    </section>

    <!-- Success message -->
    <c:if test="${not empty success}">
        <div class="orders-alert" role="status">
            <i class="fa-solid fa-circle-check"></i>
            <c:out value="${success}" />
        </div>
    </c:if>

    <!-- Error message -->
    <c:if test="${not empty error}">
        <div class="orders-alert orders-alert-error" role="alert">
            <i class="fa-solid fa-circle-exclamation"></i>
            <c:out value="${error}" />
        </div>
    </c:if>

    <c:choose>

       
        <c:when test="${empty orders}">
            <section class="orders-empty">
                <div class="orders-empty-icon">
                    <i class="fa-solid fa-box-open"></i>
                </div>

                <h2>You Don't Have Any Orders Yet</h2>

                <p>
                    Your orders will appear here once you place them.
                    Explore our products and start shopping today!
                </p>

                <a class="orders-shop-btn"
                   href="${pageContext.request.contextPath}/products">
                    <i class="fa-solid fa-bag-shopping"></i>
                    Explore Products
                </a>
            </section>
        </c:when>

       
        <c:otherwise>

            
            <div class="order-tabs">
                <button type="button"
                        class="order-tab active"
                        data-filter="ALL">
                    Tất cả
                </button>

                <button type="button"
                        class="order-tab"
                        data-filter="COMPLETED">
                    Đã thanh toán
                </button>

                <button type="button"
                        class="order-tab"
                        data-filter="CONFIRMED">
                    Chưa thanh toán
                </button>

                <button type="button"
                        class="order-tab"
                        data-filter="CANCELLED">
                    Đã hủy
                </button>
            </div>

            <!-- Order list -->
            <section class="orders-list" id="ordersList">

                <c:forEach var="order" items="${orders}">

                    <c:set var="status"
                           value="${order.orderStatus.toString()}" />

                    <article class="order-card"
                             data-status="${status}">

                        <!-- Card header -->
                        <div class="order-card-header">

                            <div>
                                <p class="order-number-label">
                                    ORDER NUMBER
                                </p>

                                <p class="order-number">
                                    #<c:out value="${order.orderId}" />
                                </p>

                                <p class="order-date">
                                    <i class="fa-regular fa-calendar"></i>

                                    <c:choose>
                                        <c:when test="${not empty order.orderDate}">
                                            <fmt:formatDate
                                                value="${order.orderDate}"
                                                pattern="dd/MM/yyyy HH:mm" />
                                        </c:when>

                                        <c:otherwise>
                                            Date unavailable
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>

                            <!-- Order status -->
                            <c:choose>

                                <c:when test="${status eq 'CONFIRMED'}">
                                    <span class="order-status status-confirmed">
                                        <i class="fa-regular fa-clock"></i>
                                        Chưa thanh toán
                                    </span>
                                </c:when>

                                <c:when test="${status eq 'COMPLETED'}">
                                    <span class="order-status status-delivered">
                                        <i class="fa-solid fa-circle-check"></i>
                                        Đã thanh toán
                                    </span>
                                </c:when>

                                <c:when test="${status eq 'CANCELLED'}">
                                    <span class="order-status status-cancelled">
                                        <i class="fa-solid fa-circle-xmark"></i>
                                        Đã hủy
                                    </span>
                                </c:when>

                                <c:when test="${status eq 'PENDING'}">
                                    <span class="order-status status-pending">
                                        <i class="fa-regular fa-clock"></i>
                                        Pending
                                    </span>
                                </c:when>

                                <c:when test="${status eq 'PROCESSING'}">
                                    <span class="order-status status-processing">
                                        <i class="fa-solid fa-box"></i>
                                        Processing
                                    </span>
                                </c:when>

                                <c:when test="${status eq 'SHIPPING'}">
                                    <span class="order-status status-shipping">
                                        <i class="fa-solid fa-truck"></i>
                                        Shipping
                                    </span>
                                </c:when>

                                <c:when test="${status eq 'DELIVERED'}">
                                    <span class="order-status status-delivered">
                                        <i class="fa-solid fa-circle-check"></i>
                                        Delivered
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <span class="order-status status-default">
                                        <c:out value="${status}" />
                                    </span>
                                </c:otherwise>

                            </c:choose>

                        </div>

                        <!-- Card body -->
                        <div class="order-card-body">

                            <div class="order-info-grid">

                                <div>
                                    <p class="order-info-label">Customer</p>

                                    <p class="order-info-value">
                                        <c:choose>
                                            <c:when test="${not empty order.customer}">
                                                <c:out value="${order.customer.firstName}" />
                                                <c:out value="${order.customer.lastName}" />
                                            </c:when>

                                            <c:otherwise>
                                                Customer information unavailable
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>

                                <div>
                                    <p class="order-info-label">Products</p>

                                    <p class="order-info-value">
                                        <c:out value="${order.orderItems.size()}" />
                                        item(s)
                                    </p>
                                </div>

                                <div>
                                    <p class="order-info-label">Discount</p>

                                    <p class="order-info-value">
                                        <fmt:formatNumber
                                            value="${order.discountAmount}"
                                            type="number"
                                            groupingUsed="true" /> ₫
                                    </p>
                                </div>

                                <div>
                                    <p class="order-info-label">Total Amount</p>

                                    <p class="order-total">
                                        <fmt:formatNumber
                                            value="${order.totalAmount}"
                                            type="number"
                                            groupingUsed="true" /> ₫
                                    </p>
                                </div>

                            </div>

                        </div>

                        <!-- Card footer -->
                        <div class="order-card-footer">

                            <a class="order-detail-btn"
                               href="${pageContext.request.contextPath}/order-detail?orderId=${order.orderId}">
                                Xem chi tiết
                                <i class="fa-solid fa-arrow-right"></i>
                            </a>

                            <!-- Only unpaid orders can show the payment button -->
                      
<!-- Only unpaid orders can show payment and cancel buttons -->
<c:if test="${status eq 'CONFIRMED'}">

   <form action="${pageContext.request.contextPath}/vnpay-create"
      method="post">
    <input type="hidden"
           name="orderId"
           value="${order.orderId}">

    <button type="submit" class="order-detail-btn">
        Thanh toán ngay
        <i class="fa-solid fa-credit-card"></i>
    </button>
</form>

    <form action="${pageContext.request.contextPath}/cancel-order"
          method="post"
          class="cancel-order-form"
          onsubmit="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này không?');">

        <input type="hidden"
               name="orderId"
               value="${order.orderId}">

        <button type="submit" class="cancel-order-btn">
            <i class="fa-solid fa-xmark"></i>
            Hủy đơn hàng
        </button>

    </form>

</c:if>

                        </div>

                    </article>

                </c:forEach>

            </section>

            <!-- No matching orders for selected tab -->
            <div id="filteredEmpty"
                 class="filtered-empty"
                 style="display: none;">
                Không có đơn hàng trong mục này.
            </div>

        </c:otherwise>

    </c:choose>

</main>

<jsp:include page="/views/common/footer.jsp" />

<script src="${pageContext.request.contextPath}/assets/js/orders.js"></script>

</body>
</html>