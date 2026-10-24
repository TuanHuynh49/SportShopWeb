
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout | SportShopWeb</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/checkout.css">

</head>

<body>

<jsp:include page="/views/common/header.jsp" />

<main class="checkout-container">

    <header class="checkout-heading">
        <p class="eyebrow">SPORTSHOPWEB / CHECKOUT</p>
        <h1>Thanh toán đơn hàng</h1>
        <p>Kiểm tra sản phẩm và điền thông tin giao hàng của bạn.</p>
    </header>

    <c:if test="${not empty error}">
        <div class="error-message">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <div class="checkout-layout">

        <div class="checkout-main">

            <!-- SẢN PHẨM ĐÃ CHỌN -->
            <section class="checkout-card">

                <div class="card-heading">
                    <h2>Sản phẩm đã chọn</h2>
                    <p>
                        Có
                        <c:out value="${empty selectedItems ? 0 : selectedItems.size()}"/>
                        loại sản phẩm trong đơn hàng.
                    </p>
                </div>

                <c:choose>
                    <c:when test="${not empty selectedItems}">

                        <div class="product-list">

                            <c:forEach var="item" items="${selectedItems}">
                                <div class="product-item">

                                    <div class="checkout-product-image-wrapper">
                                        <c:choose>
                                            <c:when test="${not empty item.productVariant.product.images
                                                and not empty item.productVariant.product.images[0].imageUrl}">
                                                <img
                                                    src="<c:out value='${item.productVariant.product.images[0].imageUrl}'/>"
                                                    alt="<c:out value='${item.productVariant.variantName}'/>"
                                                    class="checkout-product-image"
                                                    onerror="this.style.display='none';">
                                            </c:when>
                                            <c:otherwise>
                                                <span style="font-size:11px;color:#718096;">
                                                    No image
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="product-info">
                                        <h3>
                                            <c:out value="${item.productVariant.variantName}"/>
                                        </h3>

                                        <p class="product-meta">
                                            Mã phiên bản:
                                            <c:out value="${item.productVariant.variantId}"/>
                                            · Số lượng:
                                            <c:out value="${item.quantity}"/>
                                        </p>

                                        <p class="product-meta">
                                            Đơn giá:
                                            <strong>
                                                <fmt:formatNumber
                                                    value="${item.productVariant.price}"
                                                    type="number"
                                                    groupingUsed="true"
                                                    maxFractionDigits="0"/> ₫
                                            </strong>
                                        </p>
                                    </div>

                                    <div class="product-price">
                                        <span>Thành tiền</span>
                                        <strong>
                                            <fmt:formatNumber
                                                value="${item.productVariant.price * item.quantity}"
                                                type="number"
                                                groupingUsed="true"
                                                maxFractionDigits="0"/> ₫
                                        </strong>
                                    </div>

                                </div>
                            </c:forEach>

                        </div>

                    </c:when>

                    <c:otherwise>
                        <div class="empty-selection">
                            Chưa có sản phẩm được chọn.
                            <br><br>
                            <a class="btn btn-outline"
                               href="${pageContext.request.contextPath}/cart">
                                Quay lại giỏ hàng
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>

            </section>

            <!-- THÔNG TIN GIAO HÀNG -->
            <section class="checkout-card">

                <div class="card-heading">
                    <h2>Thông tin giao hàng</h2>
                    <p>Vui lòng cung cấp thông tin nhận hàng chính xác.</p>
                </div>

                <div class="card-body">

                    <form action="${pageContext.request.contextPath}/checkout"
                          method="post"
                          id="checkoutForm">

                        <input type="hidden" name="action" value="confirm">

                        <!-- Giữ voucher đang áp dụng -->
                        <input type="hidden"
                               name="voucherCode"
                               value="<c:out value='${voucherCode}'/>">

                        <!-- Giữ danh sách sản phẩm đã chọn -->
                        <c:forEach var="item" items="${selectedItems}">
                            <input type="hidden"
                                   name="selectedVariantIds"
                                   value="${item.productVariant.variantId}">
                        </c:forEach>

                        <div class="form-grid">

                            <!-- HỌ TÊN -->
                            <div class="form-group">
                                <label for="fullName">
                                    Họ và tên người nhận
                                    <span class="required">*</span>
                                </label>

                                <input class="form-control"
                                       type="text"
                                       id="fullName"
                                       name="fullName"
                                       value="<c:out value='${not empty fullName ? fullName : param.fullName}'/>"
                                       placeholder="Nhập họ và tên"
                                       autocomplete="name"
                                       required>
                            </div>

                            <!-- SỐ ĐIỆN THOẠI -->
                            <div class="form-group">
                                <label for="phone">
                                    Số điện thoại
                                    <span class="required">*</span>
                                </label>

                                <input class="form-control"
                                       type="tel"
                                       id="phone"
                                       name="phone"
                                       value="<c:out value='${not empty phone ? phone : param.phone}'/>"
                                       placeholder="Nhập số điện thoại"
                                       autocomplete="tel"
                                       pattern="[0-9+\s().-]{9,15}"
                                       required>
                            </div>

                            <!-- ĐỊA CHỈ GIAO HÀNG -->
                            <div class="form-group full-width">

                                <div class="shipping-address">

                                    <h3>Địa chỉ giao hàng</h3>

                                    <p class="address-subtitle">
                                        Chọn địa chỉ nhận hàng của bạn.
                                    </p>

                                    <c:choose>

                                        <c:when test="${not empty addresses}">

                                            <div class="address-list">

                                                <c:forEach var="address"
                                                           items="${addresses}">

                                                    <c:set var="isAddressSelected"
                                                           value="${(not empty selectedAddress and selectedAddress.addressId == address.addressId)
                                                               or (empty selectedAddress and not empty defaultAddress and defaultAddress.addressId == address.addressId)}"/>

                                                    <div class="address-item
                                                         <c:if test='${isAddressSelected}'>selected</c:if>">

                                                        <label>
                                                            <input type="radio"
                                                                   name="addressId"
                                                                   value="${address.addressId}"
                                                                   <c:if test="${isAddressSelected}">
                                                                       checked="checked"
                                                                   </c:if>
                                                                   required>

                                                            <span class="address-info">

                                                                <span class="address-location">
                                                                    <c:out value="${address.province}"/>
                                                                </span>

                                                                <c:if test="${address.defaultAddress}">
                                                                    <span class="default-label">
                                                                        Mặc định
                                                                    </span>
                                                                </c:if>

                                                                <br>

                                                                <c:out value="${address.ward}"/>

                                                                <br>

                                                                <c:out value="${address.detailAddress}"/>

                                                            </span>
                                                        </label>

                                                    </div>

                                                </c:forEach>

                                            </div>

                                            <div class="address-actions">
                                                <a href="${pageContext.request.contextPath}/addresses">
                                                    + Thêm hoặc quản lý địa chỉ
                                                </a>
                                            </div>

                                        </c:when>

                                        <c:otherwise>

                                            <div class="address-empty">
                                                <p>
                                                    Bạn chưa có địa chỉ giao hàng.
                                                </p>

                                                <a class="btn btn-outline"
                                                   href="${pageContext.request.contextPath}/addresses">
                                                    + Thêm địa chỉ giao hàng
                                                </a>
                                            </div>

                                        </c:otherwise>

                                    </c:choose>

                                </div>

                            </div>

                            <!-- GHI CHÚ -->
                            <div class="form-group full-width">
                                <label for="note">Ghi chú đơn hàng</label>

                                <textarea class="form-control"
                                          id="note"
                                          name="note"
                                          placeholder="Ghi chú thêm cho người giao hàng (nếu có)"><c:out value="${not empty note ? note : param.note}"/></textarea>
                            </div>

                            <!-- PHƯƠNG THỨC THANH TOÁN -->
                            <div class="form-group full-width">

                                <label>
                                    Phương thức thanh toán
                                    <span class="required">*</span>
                                </label>

                                <div class="payment-options">

                                    <label class="payment-option">
                                        <input type="radio"
                                               name="paymentMethod"
                                               value="COD"
                                               <c:if test="${empty paymentMethod and empty param.paymentMethod
                                                   or paymentMethod == 'COD'
                                                   or empty paymentMethod and param.paymentMethod == 'COD'}">
                                                   checked="checked"
                                               </c:if>
                                               required>

                                        <span>
                                            <strong>Thanh toán khi nhận hàng (COD)</strong>
                                            <small>
                                                Thanh toán trực tiếp khi nhận sản phẩm.
                                            </small>
                                        </span>
                                    </label>

                                    <label class="payment-option">
                                        <input type="radio"
                                               name="paymentMethod"
                                               value="BANK"
                                               <c:if test="${paymentMethod == 'BANK'
                                                   or empty paymentMethod and param.paymentMethod == 'BANK'}">
                                                   checked="checked"
                                               </c:if>>

                                        <span>
                                            <strong>Chuyển khoản ngân hàng</strong>
                                            <small>
                                                Thanh toán qua VNPAY
                                            </small>
                                        </span>
                                    </label>

                                </div>
                            </div>

                        </div>

                        <button class="btn btn-primary"
                                type="submit"
                                <c:if test="${empty selectedItems or empty addresses}">
                                    disabled="disabled"
                                </c:if>>
                            Xác nhận đặt hàng
                        </button>

                    </form>

                </div>

            </section>

        </div>

        <!-- TÓM TẮT ĐƠN HÀNG -->
        <aside class="checkout-sidebar">

            <section class="checkout-card summary-card">

                <h2>Tóm tắt đơn hàng</h2>

                <div class="summary-row">
                    <span>Số loại sản phẩm</span>
                    <strong>
                        <c:out value="${empty selectedItems ? 0 : selectedItems.size()}"/>
                    </strong>
                </div>

                <div class="summary-row">
                    <span>Phí vận chuyển</span>
                    <strong>Được xác nhận sau</strong>
                </div>

                <div class="summary-divider"></div>

                <div class="summary-total">
                    <span>Tiền hàng</span>
                    <strong>
                        <fmt:formatNumber
                            value="${total != null ? total : 0}"
                            type="number"
                            groupingUsed="true"
                            maxFractionDigits="0"/> ₫
                    </strong>
                </div>

                <!-- VOUCHER -->
                <section class="voucher-card">

                    <div class="voucher-heading">
                        <div>
                            <p class="voucher-eyebrow">SPECIAL OFFERS</p>
                            <h2>Voucher của bạn</h2>
                            <p class="voucher-description">
                                Chọn ưu đãi phù hợp để tiết kiệm hơn.
                            </p>
                        </div>

                        <span class="voucher-icon">%</span>
                    </div>

                    <form action="${pageContext.request.contextPath}/checkout"
                          method="post"
                          id="voucherForm">

                        <input type="hidden" name="action" value="review">

                        <c:forEach var="item" items="${selectedItems}">
                            <input type="hidden"
                                   name="selectedVariantIds"
                                   value="${item.productVariant.variantId}">
                        </c:forEach>

                        <input type="hidden"
                               name="fullName"
                               id="voucherFullName">

                        <input type="hidden"
                               name="phone"
                               id="voucherPhone">

                        <input type="hidden"
                               name="addressId"
                               id="voucherAddressId">

                        <input type="hidden"
                               name="note"
                               id="voucherNote">

                        <input type="hidden"
                               name="paymentMethod"
                               id="voucherPaymentMethod">

                        <label for="voucherCode">Mã giảm giá</label>

                        <div class="voucher-input-group">
                            <input type="text"
                                   id="voucherCode"
                                   name="voucherCode"
                                   value="<c:out value='${voucherCode}'/>"
                                   placeholder="Nhập mã giảm giá">

                            <button type="submit" class="btn-voucher">
                                Áp dụng
                            </button>
                        </div>

                        <c:if test="${not empty voucherError}">
                            <p class="voucher-error">
                                <c:out value="${voucherError}"/>
                            </p>
                        </c:if>

                        <c:if test="${not empty selectedVoucher}">
                            <p class="voucher-success">
                                ✓ Đã áp dụng:
                                <strong>
                                    <c:out value="${selectedVoucher.code}"/>
                                </strong>
                                — Giảm
                                <fmt:formatNumber
                                    value="${selectedVoucher.discount}"
                                    maxFractionDigits="2"/>%
                            </p>
                        </c:if>

                        <div class="voucher-list-heading">
                            <span>Voucher khả dụng</span>

                            <span class="voucher-count">
                                <c:out value="${empty activeVouchers ? 0 : activeVouchers.size()}"/> mã
                            </span>
                        </div>

                        <div class="voucher-list">

                            <c:choose>

                                <c:when test="${not empty activeVouchers}">

                                    <c:forEach var="voucher"
                                               items="${activeVouchers}">

                                        <div class="voucher-item">

                                            <div class="voucher-ticket">
                                                <span>%</span>
                                            </div>

                                            <div class="voucher-info">
                                                <strong>
                                                    <c:out value="${voucher.code}"/>
                                                </strong>

                                                <span>
                                                    Giảm
                                                    <fmt:formatNumber
                                                        value="${voucher.discount}"
                                                        maxFractionDigits="2"/>%
                                                </span>
                                            </div>

                                            <button type="button"
                                                    class="voucher-select-btn"
                                                    data-code="<c:out value='${voucher.code}'/>"
                                                    onclick="selectVoucher(this)">
                                                Chọn
                                            </button>

                                        </div>

                                    </c:forEach>

                                </c:when>

                                <c:otherwise>
                                    <p class="voucher-empty">
                                        Hiện chưa có voucher khả dụng.
                                    </p>
                                </c:otherwise>

                            </c:choose>

                        </div>

                    </form>

                </section>

                <div class="summary-row discount-row">
                    <span>Giảm giá</span>

                    <strong>
                        −<fmt:formatNumber
                            value="${discountAmount != null ? discountAmount : 0}"
                            type="number"
                            groupingUsed="true"
                            maxFractionDigits="0"/> ₫
                    </strong>
                </div>

                <div class="summary-divider"></div>

                <div class="summary-total final-total">
                    <span>Tổng thanh toán</span>

                    <strong>
                        <fmt:formatNumber
                            value="${finalTotal != null ? finalTotal : (total != null ? total : 0)}"
                            type="number"
                            groupingUsed="true"
                            maxFractionDigits="0"/> ₫
                    </strong>
                </div>

                <p class="shipping-note">
                    Tổng tiền chưa bao gồm phí vận chuyển nếu có.
                </p>

                <a class="btn btn-outline"
                   href="${pageContext.request.contextPath}/cart">
                    ← Quay lại giỏ hàng
                </a>

                <p class="secure-note">
                    🔒 Thông tin của bạn được bảo vệ.
                </p>

            </section>

        </aside>

    </div>

</main>

<jsp:include page="/views/common/footer.jsp"/>

<script src="${pageContext.request.contextPath}/assets/js/checkout.js"></script>

</body>
</html>