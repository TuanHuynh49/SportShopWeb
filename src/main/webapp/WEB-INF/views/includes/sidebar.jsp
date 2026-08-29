<%-- sidebar.jsp in views/includes --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="app-sidebar" id="app-sidebar">
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

    <nav class="sidebar-nav">
        <div class="sidebar-section">
            <div class="sidebar-section__title">KHÁM PHÁ</div>
            <a href="${pageContext.request.contextPath}/" class="sidebar-item active">
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

        <div class="sidebar-section">
            <div class="sidebar-section__title">DANH MỤC THỂ THAO</div>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=1" class="sidebar-item">
                <span class="sidebar-item__icon">👟</span>
                <span class="sidebar-item__text">Giày Đá Bóng</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=2" class="sidebar-item">
                <span class="sidebar-item__icon">👕</span>
                <span class="sidebar-item__text">Áo Thi Đấu CLB</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=3" class="sidebar-item">
                <span class="sidebar-item__icon">🩳</span>
                <span class="sidebar-item__text">Quần Thể Thao</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=4" class="sidebar-item">
                <span class="sidebar-item__icon">⚽</span>
                <span class="sidebar-item__text">Quả Bóng Đá</span>
            </a>
            <a href="${pageContext.request.contextPath}/product/list?categoryId=5" class="sidebar-item">
                <span class="sidebar-item__icon">🧤</span>
                <span class="sidebar-item__text">Găng Tay & Vớ</span>
            </a>
        </div>

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
</aside>
<div class="sidebar-backdrop" id="sidebar-backdrop"></div>
