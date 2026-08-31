<%-- sidebar.jsp — Left Sidebar Navigation động theo dữ liệu Database (MVC JSTL / EL)
     Tự động render danh mục từ Database hoặc hiển thị trạng thái DB trống khi chưa có dữ liệu.
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

            <a href="${pageContext.request.contextPath}/" class="sidebar-item ${empty param.categoryId and empty param.sale ? 'active' : ''}">
                <span class="sidebar-item__icon">🏠</span>
                <span class="sidebar-item__text">Trang chủ</span>
            </a>

            <a href="${pageContext.request.contextPath}/product/list" class="sidebar-item ${empty param.categoryId and not empty param.page ? 'active' : ''}">
                <span class="sidebar-item__icon">👟</span>
                <span class="sidebar-item__text">Tất cả sản phẩm</span>
            </a>

            <a href="${pageContext.request.contextPath}/product/list?sale=true" class="sidebar-item">
                <span class="sidebar-item__icon">⚡</span>
                <span class="sidebar-item__text text-accent fw-bold">Khuyến mãi Hot</span>
            </a>
        </div>

        <%-- Group 2: Danh mục Thể thao (Đọc động từ Database) --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">DANH MỤC THỂ THAO</div>

            <c:choose>
                <%-- KHI DATABASE CÓ DỮ LIỆU DANH MỤC --%>
                <c:when test="${not empty categories}">
                    <c:forEach items="${categories}" var="cat">
                        <div class="sidebar-dropdown">
                            <a href="${pageContext.request.contextPath}/product/list?categoryId=${cat.id}"
                               class="sidebar-item ${param.categoryId == cat.id ? 'active' : ''}">
                                <span class="sidebar-item__icon">${empty cat.icon ? '⚽' : cat.icon}</span>
                                <span class="sidebar-item__text">${cat.name}</span>
                                <c:if test="${not empty cat.subCategories}">
                                    <span class="sidebar-item__arrow">›</span>
                                </c:if>
                            </a>

                            <%-- Flyout Submenu nếu có danh mục con --%>
                            <c:if test="${not empty cat.subCategories}">
                                <div class="sidebar-flyout">
                                    <div class="flyout-header">
                                        <div class="flyout-header__title">${empty cat.icon ? '⚽' : cat.icon} ${cat.name}</div>
                                        <div class="flyout-header__sub">${cat.description}</div>
                                    </div>
                                    <div class="flyout-list">
                                        <c:forEach items="${cat.subCategories}" var="sub">
                                            <a href="${pageContext.request.contextPath}/product/list?categoryId=${sub.id}" class="flyout-item">
                                                <span class="flyout-item__icon">${empty sub.icon ? '•' : sub.icon}</span>
                                                <div>
                                                    <div class="flyout-item__text">${sub.name}</div>
                                                    <c:if test="${not empty sub.description}">
                                                        <div class="flyout-item__desc">${sub.description}</div>
                                                    </c:if>
                                                </div>
                                            </a>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>

                <%-- KHI DATABASE ĐANG ĐỂ TRỐNG (EMPTY STATE) --%>
                <c:otherwise>
                    <div class="px-3 py-2 text-xs text-muted">
                        <em>(Bảng categories đang trống)</em>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- Group 3: Tiện ích Mua hàng --%>
        <div class="sidebar-section">
            <div class="sidebar-section__title">MUA SẮM & ĐƠN HÀNG</div>
            <a href="${pageContext.request.contextPath}/cart" class="sidebar-item">
                <span class="sidebar-item__icon">🛒</span>
                <span class="sidebar-item__text">Giỏ hàng</span>
            </a>
            <a href="${pageContext.request.contextPath}/order/history" class="sidebar-item">
                <span class="sidebar-item__icon">📦</span>
                <span class="sidebar-item__text">Đơn mua của tôi</span>
            </a>
        </div>

        <%-- Group 4: Quản trị (Hiển thị khi đăng nhập Admin) --%>
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
