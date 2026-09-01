<%-- voucher-list.jsp — Quản lý mã giảm giá Admin (REQ-A06) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">🎫 QUẢN LÝ MÃ GIẢM GIÁ (VOUCHER)</h1>
            <div class="text-xs text-muted">Mã khuyến mãi, hạn mức sử dụng và theo dõi lịch sử áp dụng (VoucherUsage)</div>
        </div>
        <a href="${pageContext.request.contextPath}/admin/voucher/new" class="btn btn-sm btn-primary fw-bold">
            + Tạo Voucher Mới
        </a>
    </div>

    <!-- BẢNG VOUCHER -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <c:when test="${not empty vouchers}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Mã Code</th>
                                <th>Loại Giảm</th>
                                <th>Mức Giảm</th>
                                <th>Đơn Tối Thiểu</th>
                                <th>Thời Hạn</th>
                                <th>Lượt Dùng</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${vouchers}" var="v">
                                <tr>
                                    <td class="fw-bold"><code>${v.code}</code></td>
                                    <td>${v.discountType}</td>
                                    <td class="fw-bold text-primary">${v.discountValue}</td>
                                    <td><fmt:formatNumber value="${v.minOrderAmount}" pattern="#,###"/>₫</td>
                                    <td>${v.startDate} → ${v.endDate}</td>
                                    <td>${v.usageLimit}</td>
                                    <td><span class="badge bg-success">Áp dụng</span></td>
                                    <td class="text-end">
                                        <button class="btn btn-sm btn-outline-danger py-0 px-2">Vô hiệu hóa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">🎫</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có mã giảm giá nào trong hệ thống</div>
                    <div class="text-xs mb-3">Tạo voucher ưu đãi để thúc đẩy doanh số bán hàng trong các chiến dịch Flash Sale.</div>
                    <a href="${pageContext.request.contextPath}/admin/voucher/new" class="btn btn-sm btn-primary">
                        + Tạo Voucher Mới
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>