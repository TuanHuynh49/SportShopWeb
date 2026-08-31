<%-- dashboard.jsp — Admin Dashboard View Động (MVC JSTL / EL)
     Tự động hiển thị số liệu từ Database và xử lý Empty State khi chưa có dữ liệu.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <!-- KPI STATS CARDS (Đọc từ Database qua AdminDAO) -->
    <div class="dashboard-stats mb-4">
        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(37, 99, 235, 0.1); color: #2563eb;">💰</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Tổng Doanh Thu</div>
                <div class="stat-card__value">
                    <fmt:formatNumber value="${empty stats.totalRevenue ? 0 : stats.totalRevenue}" pattern="#,###"/>₫
                </div>
                <div class="stat-card__change text-muted">Từ đơn hàng thanh toán thành công</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(16, 185, 129, 0.1); color: #10b981;">📦</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Tổng Đơn Hàng</div>
                <div class="stat-card__value">
                    ${empty stats.totalOrders ? 0 : stats.totalOrders} đơn
                </div>
                <div class="stat-card__change text-muted">
                    ${empty stats.pendingOrders ? 0 : stats.pendingOrders} đơn đang chờ duyệt
                </div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(245, 158, 11, 0.1); color: #f59e0b;">👟</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Tổng Sản Phẩm</div>
                <div class="stat-card__value">
                    ${empty stats.totalProducts ? 0 : stats.totalProducts} mẫu
                </div>
                <div class="stat-card__change text-muted">Sản phẩm đang kinh doanh</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(139, 92, 246, 0.1); color: #8b5cf6;">👥</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Khách Hàng</div>
                <div class="stat-card__value">
                    ${empty stats.totalCustomers ? 0 : stats.totalCustomers}
                </div>
                <div class="stat-card__change text-muted">Tài khoản thành viên đã đăng ký</div>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <!-- DANH SÁCH ĐƠN HÀNG CẦN XỬ LÝ (ĐỌC ĐỘNG) -->
        <div class="col-lg-8">
            <div class="bg-surface rounded-3 border p-4">
                <div class="d-flex align-items-center justify-content-between mb-3 pb-2 border-bottom">
                    <h3 class="h6 fw-bold text-primary mb-0">📋 ĐƠN HÀNG MỚI NHẤT</h3>
                    <a href="${pageContext.request.contextPath}/order/history" class="text-xs text-accent fw-bold text-decoration-none">
                        Xem tất cả →
                    </a>
                </div>

                <c:choose>
                    <%-- KHI CÓ ĐƠN HÀNG TRONG DATABASE --%>
                    <c:when test="${not empty recentOrders}">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0 text-sm">
                                <thead class="table-light">
                                    <tr>
                                        <th>Mã Đơn</th>
                                        <th>Khách Hàng</th>
                                        <th>Tổng Tiền</th>
                                        <th>Phương Thức</th>
                                        <th>Trạng Thái</th>
                                        <th>Hành Động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentOrders}" var="o">
                                        <tr>
                                            <td class="fw-bold">#${o.orderCode}</td>
                                            <td>${o.customerName}</td>
                                            <td class="fw-bold text-primary"><fmt:formatNumber value="${o.totalAmount}" pattern="#,###"/>₫</td>
                                            <td><span class="badge bg-light text-dark border">${o.paymentMethod}</span></td>
                                            <td><span class="badge badge-subtle">${o.status}</span></td>
                                            <td><button class="btn btn-sm btn-primary py-0 px-2">Duyệt</button></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>

                    <%-- KHI CHƯA CÓ ĐƠN HÀNG TRONG DATABASE (EMPTY STATE) --%>
                    <c:otherwise>
                        <div class="text-center py-4 text-muted">
                            <div class="fs-3 mb-2">🧾</div>
                            <div class="fw-bold text-sm text-primary mb-1">Hiện chưa có đơn hàng nào trong cơ sở dữ liệu</div>
                            <div class="text-xs">Khi khách hàng đặt hàng qua Website, đơn hàng mới sẽ tự động hiển thị tại đây để duyệt.</div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- THAO TÁC NHANH & CẢNH BÁO TỒN KHO -->
        <div class="col-lg-4">
            <div class="bg-surface rounded-3 border p-4 mb-4">
                <h3 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">⚡ THAO TÁC QUẢN TRỊ</h3>
                <div class="d-grid gap-2">
                    <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary btn-sm fw-bold text-start">
                        + Quản Lý & Thêm Sản Phẩm
                    </a>
                    <a href="${pageContext.request.contextPath}/" target="_blank" class="btn btn-outline-primary btn-sm fw-bold text-start">
                        🌐 Xem Website Cửa Hàng
                    </a>
                </div>
            </div>

            <div class="bg-surface rounded-3 border p-4">
                <h3 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">⚠️ CẢNH BÁO TỒN KHO</h3>
                <c:choose>
                    <c:when test="${not empty lowStockItems}">
                        <div class="d-flex flex-column gap-3 text-sm">
                            <c:forEach items="${lowStockItems}" var="item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <div class="fw-bold">${item.name}</div>
                                        <div class="text-xs text-muted">SKU: ${item.sku}</div>
                                    </div>
                                    <span class="badge bg-danger">Còn ${item.quantity} chiếc</span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-3 text-muted text-xs">
                            <div class="fs-4 mb-1">✅</div>
                            <div>Không có biến thể sản phẩm nào dưới mức tồn kho an toàn.</div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

</div>