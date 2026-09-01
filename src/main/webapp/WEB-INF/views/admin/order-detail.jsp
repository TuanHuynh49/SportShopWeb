<%-- order-detail.jsp — Chi tiết đơn hàng Quản trị (REQ-A04) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">📦 CHI TIẾT & XỬ LÝ ĐƠN HÀNG</h1>
            <div class="text-xs text-muted">Mã đơn hàng: #${not empty order ? order.orderCode : ''}</div>
        </div>
        <a href="${pageContext.request.contextPath}/admin/order" class="btn btn-sm btn-outline-secondary">
            ← Quay lại danh sách đơn
        </a>
    </div>

    <c:choose>
        <c:when test="${not empty order}">
            <div class="row g-4">
                <div class="col-lg-8">
                    <!-- Danh sách sản phẩm trong đơn -->
                    <div class="bg-surface rounded-3 border p-4 mb-4">
                        <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">Sản Phẩm Trong Đơn</h2>
                        <table class="table align-middle mb-0 text-sm">
                            <thead class="table-light">
                                <tr>
                                    <th>Sản Phẩm</th>
                                    <th>Đơn Giá</th>
                                    <th>Số Lượng</th>
                                    <th>Thành Tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${order.items}" var="item">
                                    <tr>
                                        <td>
                                            <div class="fw-bold">${item.productName}</div>
                                            <div class="text-xs text-muted">Size: ${item.size}</div>
                                        </td>
                                        <td><fmt:formatNumber value="${item.unitPrice}" pattern="#,###"/>₫</td>
                                        <td>x${item.quantity}</td>
                                        <td class="fw-bold text-primary"><fmt:formatNumber value="${item.subtotal}" pattern="#,###"/>₫</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="bg-surface rounded-3 border p-4 mb-4">
                        <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">Cập Nhật Trạng Thái (REQ-A04)</h2>
                        <form action="${pageContext.request.contextPath}/admin/order/update" method="post">
                            <div class="mb-3">
                                <label class="form-label text-sm fw-semibold">Trạng thái hiện tại</label>
                                <select name="status" class="form-select form-select-sm">
                                    <option value="PENDING">Chờ xác nhận</option>
                                    <option value="PROCESSING">Đang đóng gói</option>
                                    <option value="SHIPPING">Đang giao hàng</option>
                                    <option value="DELIVERED">Đã giao thành công</option>
                                    <option value="CANCELLED">Hủy đơn hàng</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label text-sm fw-semibold">Ghi chú trạng thái (Note)</label>
                                <textarea name="note" class="form-control form-control-sm" rows="2" placeholder="Ghi nhận vào OrderStatusHistory..."></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary btn-sm w-100 fw-bold">Cập Nhật Trạng Thái</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="text-center py-5 bg-surface rounded-3 border text-muted">
                <div class="display-4 mb-2">🔍</div>
                <div class="fw-bold text-sm text-primary mb-1">Không tìm thấy thông tin đơn hàng</div>
                <div class="text-xs mb-3">Mã đơn hàng không hợp lệ hoặc chưa được khởi tạo trong CSDL.</div>
                <a href="${pageContext.request.contextPath}/admin/order" class="btn btn-sm btn-primary">
                    Quay lại danh sách đơn
                </a>
            </div>
        </c:otherwise>
    </c:choose>

</div>