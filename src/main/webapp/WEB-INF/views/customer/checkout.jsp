<%-- checkout.jsp — Trang đặt hàng & thanh toán (MVC JSTL / EL)
     Tuân thủ REQ-C04, REQ-C05, REQ-C08 trong OR-Mapping
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <h1 class="h4 fw-bold mb-0 text-primary">💳 THANH TOÁN & ĐẶT HÀNG</h1>
        <a href="${pageContext.request.contextPath}/cart" class="text-sm text-accent text-decoration-none">
            ← Quay lại giỏ hàng
        </a>
    </div>

    <c:choose>
        <%-- KHI CÓ SẢN PHẨM TRONG GIỎ HÀNG ĐỂ CHECKOUT --%>
        <c:when test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
            <form action="${pageContext.request.contextPath}/checkout" method="post">
                <div class="row g-4">
                    <!-- Cột trái: Thông tin giao hàng & Phương thức thanh toán -->
                    <div class="col-lg-7">
                        <div class="bg-surface rounded-3 border p-4 mb-4">
                            <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">1. THÔNG TIN NGƯỜI NHẬN HÀNG</h2>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label text-sm fw-semibold">Họ và tên *</label>
                                    <input type="text" name="customerName" class="form-control form-control-sm"
                                           value="${sessionScope.authUser != null ? sessionScope.authUser.fullName : ''}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label text-sm fw-semibold">Số điện thoại *</label>
                                    <input type="tel" name="customerPhone" class="form-control form-control-sm"
                                           value="${sessionScope.authUser != null ? sessionScope.authUser.phone : ''}" required>
                                </div>
                                <div class="col-12">
                                    <label class="form-label text-sm fw-semibold">Email nhận thông báo</label>
                                    <input type="email" name="customerEmail" class="form-control form-control-sm"
                                           value="${sessionScope.authUser != null ? sessionScope.authUser.email : ''}">
                                </div>
                                <div class="col-12">
                                    <label class="form-label text-sm fw-semibold">Địa chỉ giao hàng chi tiết *</label>
                                    <input type="text" name="shippingAddress" class="form-control form-control-sm"
                                           placeholder="Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố" required>
                                </div>
                                <div class="col-12">
                                    <label class="form-label text-sm fw-semibold">Ghi chú đơn hàng (Tùy chọn)</label>
                                    <textarea name="notes" class="form-control form-control-sm" rows="2" placeholder="Ví dụ: Giao giờ hành chính, gọi trước khi giao..."></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="bg-surface rounded-3 border p-4">
                            <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">2. PHƯƠNG THỨC THANH TOÁN (REQ-C05)</h2>
                            <div class="d-flex flex-column gap-3">
                                <div class="form-check p-3 border rounded-3 bg-subtle">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="payCOD" value="COD" checked>
                                    <label class="form-check-label ms-2" for="payCOD">
                                        <div class="fw-bold text-sm text-primary">💵 Thanh toán khi nhận hàng (COD)</div>
                                        <div class="text-xs text-muted">Nhận hàng, kiểm tra sản phẩm trước khi thanh toán tiền mặt cho shipper</div>
                                    </label>
                                </div>
                                <div class="form-check p-3 border rounded-3">
                                    <input class="form-check-input" type="radio" name="paymentMethod" id="payQR" value="VIETQR">
                                    <label class="form-check-label ms-2" for="payQR">
                                        <div class="fw-bold text-sm text-primary">📱 Chuyển khoản VietQR / MoMo</div>
                                        <div class="text-xs text-muted">Quét mã QR tự động xác nhận đơn hàng ngay lập tức</div>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Cột phải: Tóm tắt đơn hàng & Voucher -->
                    <div class="col-lg-5">
                        <div class="bg-surface rounded-3 border p-4 sticky-top" style="top: 80px;">
                            <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">TÓM TẮT ĐƠN HÀNG</h2>

                            <!-- Danh sách sản phẩm -->
                            <div class="d-flex flex-column gap-3 mb-3 pb-3 border-bottom max-h-60 overflow-auto">
                                <c:forEach items="${sessionScope.cart.items}" var="item">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="d-flex align-items-center gap-2">
                                            <div class="fw-bold text-xs text-muted">x${item.quantity}</div>
                                            <div>
                                                <div class="fw-semibold text-xs text-primary">${item.productName}</div>
                                                <div class="text-xs text-muted">Size: ${item.size}</div>
                                            </div>
                                        </div>
                                        <div class="text-xs fw-bold"><fmt:formatNumber value="${item.totalPrice}" pattern="#,###"/>₫</div>
                                    </div>
                                </c:forEach>
                            </div>

                            <!-- Mã giảm giá Voucher (REQ-C08) -->
                            <div class="mb-3">
                                <label class="form-label text-xs fw-semibold text-muted text-uppercase">Mã giảm giá (Voucher)</label>
                                <div class="input-group input-group-sm">
                                    <input type="text" name="voucherCode" class="form-control text-uppercase" placeholder="Nhập mã voucher...">
                                    <button class="btn btn-outline-secondary" type="button">Áp dụng</button>
                                </div>
                            </div>

                            <!-- Tổng tiền -->
                            <div class="d-flex justify-content-between mb-2 text-sm">
                                <span class="text-muted">Tạm tính:</span>
                                <span class="fw-bold"><fmt:formatNumber value="${sessionScope.cart.totalAmount}" pattern="#,###"/>₫</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2 text-sm">
                                <span class="text-muted">Phí vận chuyển:</span>
                                <span class="text-success fw-bold">Miễn phí</span>
                            </div>
                            <div class="d-flex justify-content-between mb-4 pt-3 border-top">
                                <span class="fw-bold text-primary">Tổng thanh toán:</span>
                                <span class="fw-bold fs-5 text-accent"><fmt:formatNumber value="${sessionScope.cart.totalAmount}" pattern="#,###"/>₫</span>
                            </div>

                            <button type="submit" class="btn btn-primary w-100 py-3 fw-bold">
                                🚀 XÁC NHẬN ĐẶT HÀNG
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </c:when>

        <%-- KHI GIỎ HÀNG ĐANG TRỐNG --%>
        <c:otherwise>
            <div class="empty-state-card text-center py-5 px-4 bg-surface rounded-3 border">
                <div class="display-3 text-muted mb-3">🛒</div>
                <h2 class="h5 fw-bold text-primary mb-2">Giỏ hàng của bạn đang trống</h2>
                <p class="text-muted text-sm max-w-md mx-auto mb-4">
                    Bạn cần có ít nhất 1 sản phẩm trong giỏ hàng để tiến hành thanh toán.
                </p>
                <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary px-4">
                    Khám phá sản phẩm ngay
                </a>
            </div>
        </c:otherwise>
    </c:choose>

</div>