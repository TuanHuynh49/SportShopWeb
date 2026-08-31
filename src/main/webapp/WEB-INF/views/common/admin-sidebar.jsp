<%-- admin-sidebar.jsp — Left Sidebar Admin (Đồng bộ chuẩn giao diện với Customer)
     Tuân thủ 8 Requirements Admin trong OR-Mapping (REQ-A01 -> REQ-A08)
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="app-sidebar" id="app-sidebar">
    <%-- Sidebar Header Brand --%>
    <div class="sidebar-header">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-brand">
            <div class="sidebar-brand__logo" style="background-color: var(--color-accent, #2563eb);">A</div>
            <div>
                <div class="sidebar-brand__text">SPORT<span>ADMIN</span></div>
                <div class="sidebar-brand__sub">Portal Quản Trị</div>
            </div>
        </a>
        <button type="button" class="sidebar-close-btn d-lg-none" id="sidebar-close-btn" aria-label="Đóng menu">✕</button>
    </div>

    <%-- Sidebar Navigation List --%>
    <nav class="sidebar-nav">
        <%-- Group 1: Tổng quan & Báo cáo (REQ-A08) --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">TỔNG QUAN & BÁO CÁO</div>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="sidebar-item ${empty param.section ? 'active' : ''}">
                <span class="sidebar-item__icon">📊</span>
                <span class="sidebar-item__text">Dashboard Thống Kê</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/dashboard?section=report" class="sidebar-item">
                <span class="sidebar-item__icon">📈</span>
                <span class="sidebar-item__text">Báo Cáo Doanh Thu</span>
            </a>
        </div>

        <%-- Group 2: Quản lý Sản phẩm & Kho (REQ-A01, REQ-A02, REQ-A03) --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">SẢN PHẨM & TỒN KHO</div>
            <a href="${pageContext.request.contextPath}/product/list" class="sidebar-item">
                <span class="sidebar-item__icon">👟</span>
                <span class="sidebar-item__text">Danh Sách Sản Phẩm</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/dashboard?section=category" class="sidebar-item">
                <span class="sidebar-item__icon">🗂</span>
                <span class="sidebar-item__text">Danh Mục & Thương Hiệu</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/dashboard?section=inventory" class="sidebar-item">
                <span class="sidebar-item__icon">📦</span>
                <span class="sidebar-item__text">Tồn Kho & Biến Thể</span>
            </a>
        </div>

        <%-- Group 3: Quản lý Đơn hàng & Khách hàng (REQ-A04, REQ-A05) --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">KINH DOANH & ĐƠN HÀNG</div>
            <a href="${pageContext.request.contextPath}/order/history" class="sidebar-item">
                <span class="sidebar-item__icon">🧾</span>
                <span class="sidebar-item__text">Quản Lý Đơn Hàng</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/dashboard?section=customer" class="sidebar-item">
                <span class="sidebar-item__icon">👥</span>
                <span class="sidebar-item__text">Danh Sách Khách Hàng</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/dashboard?section=voucher" class="sidebar-item">
                <span class="sidebar-item__icon">🎫</span>
                <span class="sidebar-item__text">Mã Giảm Giá (Voucher)</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/dashboard?section=review" class="sidebar-item">
                <span class="sidebar-item__icon">⭐</span>
                <span class="sidebar-item__text">Duyệt Đánh Giá (Review)</span>
            </a>
        </div>

        <%-- Group 4: Trở về trang khách --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">HỆ THỐNG</div>
            <a href="${pageContext.request.contextPath}/" class="sidebar-item text-accent fw-bold">
                <span class="sidebar-item__icon">🌐</span>
                <span class="sidebar-item__text">Xem Website Cửa Hàng</span>
            </a>
        </div>
    </nav>

    <%-- Sidebar Footer User Status --%>
    <div class="sidebar-footer">
        <div class="sidebar-user">
            <div class="sidebar-user__avatar" style="background-color: var(--color-primary, #0f172a);">
                ${empty sessionScope.authUser ? 'A' : sessionScope.authUser.fullName.substring(0, 1).toUpperCase()}
            </div>
            <div class="sidebar-user__info">
                <div class="sidebar-user__name">${empty sessionScope.authUser ? 'Quản Trị Viên' : sessionScope.authUser.fullName}</div>
                <div class="sidebar-user__role text-accent">Administrator</div>
            </div>
        </div>
    </div>
</aside>

<div class="sidebar-backdrop" id="sidebar-backdrop"></div>
