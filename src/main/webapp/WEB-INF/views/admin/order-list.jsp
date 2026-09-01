<%-- order-list.jsp — Quản lý danh sách đơn hàng Admin (REQ-A04) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">🧾 QUẢN LÝ ĐƠN HÀNG</h1>
            <div class="text-xs text-muted">Duyệt đơn, cập nhật trạng thái vận chuyển và ghi nhận lịch sử (OrderStatusHistory)</div>
        </div>
        <div class="d-flex gap-2">
            <button class="btn btn-sm btn-outline-secondary">Lọc Nhanh</button>
            <button class="btn btn-sm btn-outline-primary">Xuất Danh Sách</button>
        </div>
    </div>

    <!-- TABS TRẠNG THÁI ĐƠN -->
    <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
            <a class="nav-link active fw-semibold" href="#">Tất cả đơn</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-secondary" href="#">Chờ xác nhận</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-secondary" href="#">Đang giao hàng</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-secondary" href="#">Đã hoàn thành</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-secondary" href="#">Đã hủy</a>
        </li>
    </ul>

    <!-- BẢNG ĐƠN HÀNG -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <c:when test="${not empty orders}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Mã Đơn</th>
                                <th>Khách Hàng</th>
                                <th>Ngày Đặt</th>
                                <th>Tổng Tiền</th>
                                <th>Thanh Toán</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orders}" var="o">
                                <tr>
                                    <td class="fw-bold">#${o.orderCode}</td>
                                    <td>
                                        <div class="fw-semibold">${o.customerName}</div>
                                        <div class="text-xs text-muted">${o.customerPhone}</div>
                                    </td>
                                    <td>${o.orderDate}</td>
                                    <td class="fw-bold text-primary"><fmt:formatNumber value="${o.totalAmount}" pattern="#,###"/>₫</td>
                                    <td><span class="badge bg-light text-dark border">${o.paymentMethod}</span></td>
                                    <td><span class="badge badge-subtle">${o.status}</span></td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/admin/order/detail?id=${o.id}" class="btn btn-sm btn-outline-primary py-0 px-2">Chi tiết</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">🧾</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có đơn hàng nào trong hệ thống</div>
                    <div class="text-xs">Khi có khách hàng đặt hàng qua Website, đơn hàng mới sẽ tự động hiển thị tại đây để duyệt và xử lý.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>