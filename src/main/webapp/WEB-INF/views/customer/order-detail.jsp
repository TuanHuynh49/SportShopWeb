<%-- order-detail.jsp — Chi tiết đơn hàng & Theo dõi vận chuyển (REQ-C06) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h4 fw-bold mb-1 text-primary">📦 CHI TIẾT ĐƠN HÀNG</h1>
            <div class="text-xs text-muted">Mã đơn: #${order != null ? order.orderCode : ''}</div>
        </div>
        <a href="${pageContext.request.contextPath}/order/history" class="text-sm text-accent text-decoration-none">
            ← Danh sách đơn hàng
        </a>
    </div>

    <c:choose>
        <%-- KHI CÓ DỮ LIỆU ĐƠN HÀNG --%>
        <c:when test="${not empty order}">
            <div class="row g-4">
                <div class="col-lg-8">
                    <!-- Danh sách sản phẩm trong đơn -->
                    <div class="table-responsive bg-surface rounded-3 border p-3 mb-4">
                        <h2 class="h6 fw-bold text-primary mb-3">DANH SÁCH SẢN PHẨM</h2>
                        <table class="table align-middle mb-0 text-sm">
                            <thead class="table-light">
                                <tr>
                                    <th>Sản phẩm</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${order.items}" var="item">
                                    <tr>
                                        <td>
                                            <div class="fw-bold text-primary">${item.productName}</div>
                                            <div class="text-xs text-muted">Size: ${item.size}</div>
                                        </td>
                                        <td><fmt:formatNumber value="${item.unitPrice}" pattern="#,###"/>₫</td>
                                        <td class="fw-bold">x${item.quantity}</td>
                                        <td class="fw-bold text-primary"><fmt:formatNumber value="${item.subtotal}" pattern="#,###"/>₫</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="bg-surface rounded-3 border p-4">
                        <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">THÔNG TIN GIAO NHẬN</h2>
                        <div class="d-flex flex-column gap-2 text-sm mb-4">
                            <div><strong>Người nhận:</strong> ${order.customerName}</div>
                            <div><strong>Điện thoại:</strong> ${order.customerPhone}</div>
                            <div><strong>Địa chỉ:</strong> ${order.shippingAddress}</div>
                            <div><strong>Phương thức thanh toán:</strong> ${order.paymentMethod}</div>
                            <div><strong>Trạng thái:</strong> <span class="badge badge-subtle">${order.status}</span></div>
                        </div>

                        <div class="border-top pt-3 d-flex justify-content-between">
                            <span class="fw-bold text-primary">Tổng tiền thanh toán:</span>
                            <span class="fw-bold fs-5 text-accent"><fmt:formatNumber value="${order.totalAmount}" pattern="#,###"/>₫</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>

        <%-- KHI KHÔNG TÌM THẤY ĐƠN HÀNG (EMPTY STATE) --%>
        <c:otherwise>
            <div class="empty-state-card text-center py-5 px-4 bg-surface rounded-3 border">
                <div class="display-3 text-muted mb-3">🔍</div>
                <h2 class="h5 fw-bold text-primary mb-2">Không tìm thấy thông tin đơn hàng</h2>
                <p class="text-muted text-sm max-w-md mx-auto mb-4">
                    Mã đơn hàng không hợp lệ hoặc bạn không có quyền xem đơn hàng này.
                </p>
                <a href="${pageContext.request.contextPath}/order/history" class="btn btn-primary px-4">
                    Quay lại lịch sử đơn hàng
                </a>
            </div>
        </c:otherwise>
    </c:choose>

</div>