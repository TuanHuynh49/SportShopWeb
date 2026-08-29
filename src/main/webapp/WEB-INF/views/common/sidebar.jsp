<%-- sidebar.jsp — Left Sidebar Navigation
     Thiết kế: Tối giản (White Clean / Dark Slate border), icon trực quan, hover hiệu ứng nhẹ.
     Hỗ trợ RBAC: Phân tách danh mục khám phá, mua sắm và liên kết quản trị.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="app-sidebar" id="app-sidebar">
    <%-- Sidebar Header / Logo --%>
    <div class="sidebar-header">
        <a href="${pageContext.request.contextPath}/" class="sidebar-brand">
            <div class="sidebar-brand__logo">S</div>
            <div>
                <div class="sidebar-brand__text">SPORT<span>SHOP</span></div>
                <div class="sidebar-brand__sub">Athletic Gear</div>
            </div>
        </a>
        <button type="button" class="sidebar-close-btn d-lg-none" id="sidebar-close-btn" aria-label="Đóng menu">✕</button>
    </div>

    <%-- Sidebar Navigation List --%>
    <nav class="sidebar-nav">
        <%-- Group 1: Khám phá & Mua sắm --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">KHÁM PHÁ</div>
            <a href="${pageContext.request.contextPath}/" class="sidebar-item ${empty param.categoryId and empty param.brandId and empty param.sale ? 'active' : ''}">
                <span class="sidebar-item__icon">🏠</span>
                <span class="sidebar-item__text">Trang chủ</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list" class="sidebar-item">
                <span class="sidebar-item__icon">👟</span>
                <span class="sidebar-item__text">Tất cả sản phẩm</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?brandId=sale" class="sidebar-item">
                <span class="sidebar-item__icon">⚡</span>
                <span class="sidebar-item__text text-accent fw-bold">Khuyến mãi Hot</span>
            </a>
        </div>

        <%-- Group 2: Danh mục Thể thao --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">DANH MỤC THỂ THAO</div>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=1" class="sidebar-item ${param.categoryId == '1' ? 'active' : ''}">
                <span class="sidebar-item__icon">👟</span>
                <span class="sidebar-item__text">Giày Đá Bóng</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=2" class="sidebar-item ${param.categoryId == '2' ? 'active' : ''}">
                <span class="sidebar-item__icon">👕</span>
                <span class="sidebar-item__text">Áo Thi Đấu CLB</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=3" class="sidebar-item ${param.categoryId == '3' ? 'active' : ''}">
                <span class="sidebar-item__icon">🩳</span>
                <span class="sidebar-item__text">Quần Thể Thao</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=4" class="sidebar-item ${param.categoryId == '4' ? 'active' : ''}">
                <span class="sidebar-item__icon">⚽</span>
                <span class="sidebar-item__text">Quả Bóng Đá</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=5" class="sidebar-item ${param.categoryId == '5' ? 'active' : ''}">
                <span class="sidebar-item__icon">🧤</span>
                <span class="sidebar-item__text">Găng Tay & Vớ</span>
            </a>
        </div>

        <%-- Group 3: Tiện ích Mua hàng --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">MUA SẮM & ĐƠN HÀNG</div>
            <a href="${pageContext.request.contextPath}/cart" class="sidebar-item">
                <span class="sidebar-item__icon">🛒</span>
                <span class="sidebar-item__text">Giỏ hàng</span>
            </a>
            <a href="${pageContext.request.contextPath}/checkout" class="sidebar-item">
                <span class="sidebar-item__icon">💳</span>
                <span class="sidebar-item__text">Thanh toán</span>
            </a>
            <a href="${pageContext.request.contextPath}/order/history" class="sidebar-item">
                <span class="sidebar-item__icon">📦</span>
                <span class="sidebar-item__text">Đơn mua của tôi</span>
            </a>
        </div>

        <%-- Group 4: Quản trị (Dành cho Admin) --%>
        <c:if test="${not empty sessionScope.authUser and sessionScope.authUser.admin}">
            <div class="sidebar-section">
                <div class="sidebar-section__title">HỆ THỐNG QUẢN TRỊ</div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-item" style="color: var(--color-accent); font-weight: var(--fw-bold);">
                    <span class="sidebar-item__icon">📊</span>
                    <span class="sidebar-item__text">Admin Dashboard</span>
                    <span class="badge badge-accent ms-auto">ADMIN</span>
                </a>
            </div>
        </c:if>
    </nav>

    <%-- Sidebar Footer User Status --%>
    <div class="sidebar-footer">
        <c:choose>
            <c:when test="${not empty sessionScope.authUser}">
                <div class="sidebar-user">
                    <div class="sidebar-user__avatar">
                        ${sessionScope.authUser.fullName.substring(0, 1).toUpperCase()}
                    </div>
                    <div class="sidebar-user__info">
                        <div class="sidebar-user__name">${sessionScope.authUser.fullName}</div>
                        <div class="sidebar-user__role">${sessionScope.authUser.role.displayName}</div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="sidebar-guest-box">
                    <div class="text-xs text-muted mb-2">Đăng nhập để nhận ưu đãi thành viên</div>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-primary w-100">Đăng Nhập</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</aside>

<%-- Backdrop cho mobile khi sidebar mở --%>
<div class="sidebar-backdrop" id="sidebar-backdrop"></div>
