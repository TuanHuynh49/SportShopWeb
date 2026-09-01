<%-- report.jsp — Báo cáo doanh thu & Thống kê kinh doanh (REQ-A08) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">📈 BÁO CÁO & THỐNG KÊ DOANH THU</h1>
            <div class="text-xs text-muted">Dữ liệu tổng hợp từ các giao dịch thanh toán thực tế (Payment)</div>
        </div>

        <div class="d-flex gap-2">
            <select class="form-select form-select-sm" style="width: 140px;">
                <option value="this_month">Tháng này</option>
                <option value="last_month">Tháng trước</option>
                <option value="this_year">Năm 2026</option>
            </select>
            <button class="btn btn-sm btn-outline-primary fw-semibold">Xuất Excel</button>
        </div>
    </div>

    <!-- TỔNG HỢP NHANH -->
    <div class="dashboard-stats mb-4">
        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(37, 99, 235, 0.1); color: #2563eb;">💰</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Tổng Doanh Thu Đã Thu</div>
                <div class="stat-card__value">
                    <fmt:formatNumber value="${empty stats.totalRevenue ? 0 : stats.totalRevenue}" pattern="#,###"/>₫
                </div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(16, 185, 129, 0.1); color: #10b981;">📦</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Tổng Số Đơn Hàng</div>
                <div class="stat-card__value">${empty stats.totalOrders ? 0 : stats.totalOrders} đơn</div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(245, 158, 11, 0.1); color: #f59e0b;">🏷️</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Giá Trị Đơn Trung Bình</div>
                <div class="stat-card__value">
                    <c:choose>
                        <c:when test="${stats.totalOrders > 0}">
                            <fmt:formatNumber value="${stats.totalRevenue / stats.totalOrders}" pattern="#,###"/>₫
                        </c:when>
                        <c:otherwise>0₫</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-card__icon" style="background-color: rgba(139, 92, 246, 0.1); color: #8b5cf6;">👥</div>
            <div class="stat-card__body">
                <div class="stat-card__label">Số Khách Mua Hàng</div>
                <div class="stat-card__value">${empty stats.totalCustomers ? 0 : stats.totalCustomers}</div>
            </div>
        </div>
    </div>

    <!-- BẢNG BÁO CÁO CHI TIẾT THEO DANH MỤC -->
    <div class="bg-surface rounded-3 border p-4 mb-4">
        <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">📊 CƠ CẤU DOANH THU THEO DANH MỤC SẢN PHẨM</h2>
        
        <c:choose>
            <c:when test="${not empty revenueByCategory}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Danh Mục</th>
                                <th>Số Lượng Bán</th>
                                <th>Doanh Thu</th>
                                <th>Tỷ Trọng (%)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${revenueByCategory}" var="c">
                                <tr>
                                    <td class="fw-bold">${c.categoryName}</td>
                                    <td>${c.totalQuantity}</td>
                                    <td class="fw-bold text-primary"><fmt:formatNumber value="${c.totalAmount}" pattern="#,###"/>₫</td>
                                    <td>${c.percentage}%</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="fs-2 mb-2">📊</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có dữ liệu giao dịch để phân tích doanh thu</div>
                    <div class="text-xs">Khi có đơn hàng được thanh toán thành công trong bảng <code>payment</code>, biểu đồ và số liệu sẽ tự động tính toán.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>