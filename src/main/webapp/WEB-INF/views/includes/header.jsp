<%-- header.jsp in views/includes --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="site-header site-header--streamlined" id="site-header">
    <div class="container-fluid px-3 px-lg-4 header-inner">
        <div class="header-left d-flex align-items-center gap-3">
            <button class="sidebar-toggle-btn" id="sidebar-toggle" aria-label="Mở Menu Sidebar" title="Mở danh mục điều hướng">
                <span>☰</span>
            </button>
            <a href="${pageContext.request.contextPath}/" class="navbar-brand m-0" aria-label="SportShop - Trang chủ">
                <div class="navbar-brand__logo">S</div>
                <div>
                    <div class="navbar-brand__text">SPORT<span class="text-accent">SHOP</span></div>
                    <div class="navbar-brand__sub d-none d-sm-block">Athletic Gear</div>
                </div>
            </a>
        </div>

        <div class="header-center flex-grow-1 mx-3 mx-md-5">
            <form class="navbar-search" action="${pageContext.request.contextPath}/product/list" method="get" role="search">
                <input type="search"
                       class="navbar-search__input"
                       name="keyword"
                       id="search-input"
                       placeholder="Tìm kiếm giày bóng đá, áo CLB, quả bóng..."
                       value="${param.keyword}"
                       autocomplete="off"
                       aria-label="Tìm kiếm sản phẩm">
                <button type="submit" class="navbar-search__btn" aria-label="Tìm kiếm">🔍</button>
            </form>
        </div>

        <div class="header-right d-flex align-items-center gap-2">
            <c:choose>
                <c:when test="${not empty sessionScope.authUser and sessionScope.authUser.admin}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-sm btn-outline-primary d-none d-sm-inline-flex" id="nav-admin-panel">
                        <span class="badge badge-accent me-1">ADMIN</span>
                        <span>Quản Trị</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-secondary text-error" title="Đăng xuất">
                        <span>Đăng xuất</span>
                    </a>
                </c:when>
                <c:when test="${not empty sessionScope.authUser}">
                    <a href="${pageContext.request.contextPath}/account" class="nav-link d-none d-sm-flex align-items-center" id="nav-account">
                        <span>👤 <strong>${sessionScope.authUser.fullName}</strong></span>
                    </a>
                    <a href="${pageContext.request.contextPath}/order/history" class="nav-link d-none d-md-flex" id="nav-orders">
                        <span>Đơn mua</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-secondary" title="Đăng xuất">
                        <span>Đăng xuất</span>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-outline-secondary" id="btn-header-login">
                        Đăng nhập
                    </a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-sm btn-primary d-none d-sm-inline-flex" id="btn-header-register">
                        Đăng ký
                    </a>
                </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/cart" class="btn btn-sm btn-outline-secondary d-flex align-items-center gap-1 ms-1" id="nav-cart" aria-label="Giỏ hàng">
                <span>🛒</span>
                <span class="d-none d-md-inline">Giỏ hàng</span>
                <c:set var="cartCount" value="${empty sessionScope.cart ? 0 : sessionScope.cart.cartItemCount}"/>
                <span class="cart-badge" id="cart-item-count" style="display:${cartCount > 0 ? 'inline-flex' : 'none'}">
                    ${cartCount}
                </span>
            </a>
        </div>
    </div>
</header>
