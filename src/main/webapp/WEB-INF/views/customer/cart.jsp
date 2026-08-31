<%-- cart.jsp — Giỏ hàng động (MVC JSTL / EL) với xử lý Empty State khi giỏ hàng trống --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <h1 class="h4 fw-bold mb-0 text-primary">🛒 GIỎ HÀNG CỦA BẠN</h1>
        <a href="${pageContext.request.contextPath}/product/list" class="text-sm text-accent text-decoration-none">
            ← Tiếp tục mua sắm
        </a>
    </div>

    <c:choose>
        <%-- KHI GIỎ HÀNG CÓ SẢN PHẨM --%>
        <c:when test="${not empty sessionScope.cart and not empty sessionScope.cart.items}">
            <div class="row g-4">
                <div class="col-lg-8">
                    <div class="table-responsive bg-surface rounded-3 border p-3">
                        <table class="table align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>Sản phẩm</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${sessionScope.cart.items}" var="item">
                                    <tr>
                                        <td>
                                            <div class="d-flex align-items-center gap-3">
                                                <img src="${empty item.thumbnailUrl ? pageContext.request.contextPath.concat('/assets/images/no-image.svg') : item.thumbnailUrl}"
                                                     alt="${item.productName}"
                                                     style="width: 50px; height: 50px; object-fit: cover; border-radius: 6px;">
                                                <div>
                                                    <div class="fw-bold text-sm text-primary">${item.productName}</div>
                                                    <div class="text-xs text-muted">Size: ${item.size} | Màu: ${item.color}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="text-sm"><fmt:formatNumber value="${item.unitPrice}" pattern="#,###"/>₫</td>
                                        <td>
                                            <div class="fw-bold text-sm">${item.quantity}</div>
                                        </td>
                                        <td class="fw-bold text-sm text-primary"><fmt:formatNumber value="${item.totalPrice}" pattern="#,###"/>₫</td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-danger py-0 px-2" title="Xóa">✕</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="bg-surface rounded-3 border p-4">
                        <h3 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">TỔNG ĐƠN HÀNG</h3>
                        <div class="d-flex justify-content-between mb-2 text-sm">
                            <span class="text-muted">Tạm tính:</span>
                            <span class="fw-bold"><fmt:formatNumber value="${sessionScope.cart.totalAmount}" pattern="#,###"/>₫</span>
                        </div>
                        <div class="d-flex justify-content-between mb-3 text-sm">
                            <span class="text-muted">Phí giao hàng:</span>
                            <span class="text-success fw-bold">Miễn phí</span>
                        </div>
                        <div class="d-flex justify-content-between mb-4 pt-3 border-top">
                            <span class="fw-bold text-primary">Tổng thanh toán:</span>
                            <span class="fw-bold fs-5 text-accent"><fmt:formatNumber value="${sessionScope.cart.totalAmount}" pattern="#,###"/>₫</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary w-100 py-2 fw-bold">
                            TIẾN HÀNH ĐẶT HÀNG
                        </a>
                    </div>
                </div>
            </div>
        </c:when>

        <%-- KHI GIỎ HÀNG ĐANG TRỐNG (EMPTY STATE) --%>
        <c:otherwise>
            <div class="empty-state-card text-center py-5 px-4 bg-surface rounded-3 border">
                <div class="display-3 text-muted mb-3">🛒</div>
                <h2 class="h5 fw-bold text-primary mb-2">Giỏ hàng của bạn đang trống</h2>
                <p class="text-muted text-sm max-w-md mx-auto mb-4">
                    Chưa có sản phẩm nào được thêm vào giỏ hàng. Hãy dạo quanh cửa hàng để tìm món đồ ưng ý nhé!
                </p>
                <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary px-4">
                    Khám phá sản phẩm ngay
                </a>
            </div>
        </c:otherwise>
    </c:choose>

</div>