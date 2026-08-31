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
        </div>

        <div class="sidebar-section">
            <div class="sidebar-section__title">DANH MỤC THỂ THAO</div>
            <c:choose>
                <c:when test="${not empty categories}">
                    <c:forEach items="${categories}" var="cat">
                        <div class="sidebar-dropdown">
                            <a href="${pageContext.request.contextPath}/product/list?categoryId=${cat.id}" class="sidebar-item">
                                <span class="sidebar-item__icon">${empty cat.icon ? '⚽' : cat.icon}</span>
                                <span class="sidebar-item__text">${cat.name}</span>
                                <c:if test="${not empty cat.subCategories}">
                                    <span class="sidebar-item__arrow">›</span>
                                </c:if>
                            </a>
                            <c:if test="${not empty cat.subCategories}">
                                <div class="sidebar-flyout">
                                    <div class="flyout-header">
                                        <div class="flyout-header__title">${cat.name}</div>
                                    </div>
                                    <div class="flyout-list">
                                        <c:forEach items="${cat.subCategories}" var="sub">
                                            <a href="${pageContext.request.contextPath}/product/list?categoryId=${sub.id}" class="flyout-item">
                                                <span class="flyout-item__text">${sub.name}</span>
                                            </a>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="px-3 py-2 text-xs text-muted">
                        <em>(Bảng categories đang trống)</em>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

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
    </nav>
</aside>
<div class="sidebar-backdrop" id="sidebar-backdrop"></div>
