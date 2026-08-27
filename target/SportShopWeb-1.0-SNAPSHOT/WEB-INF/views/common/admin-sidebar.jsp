<%-- admin-sidebar.jsp — Sidebar Admin với menu điều hướng
     Include trong layout-admin.jsp.
     Data: ${sessionScope.adminUser}
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<aside class="admin-sidebar" id="admin-sidebar" role="navigation" aria-label="Menu quản trị">

    <%-- Brand --%>
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-sidebar__brand">
        <div class="admin-sidebar__brand-icon">S</div>
        <div>
            <div class="admin-sidebar__brand-name">SportShop Admin</div>
            <div class="admin-sidebar__brand-sub">Quản trị hệ thống</div>
        </div>
    </a>

    <%-- TỔNG QUAN --%>
    <div class="admin-sidebar__section">
        <div class="admin-sidebar__section-title">Tổng quan</div>
        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="admin-nav-item" id="nav-dashboard">
            <span class="admin-nav-item__icon">📊</span>
            Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/admin/report"
           class="admin-nav-item" id="nav-report">
            <span class="admin-nav-item__icon">📈</span>
            Báo cáo & Thống kê
        </a>
    </div>

    <hr class="admin-sidebar__divider">

    <%-- SẢN PHẨM --%>
    <div class="admin-sidebar__section">
        <div class="admin-sidebar__section-title">Sản phẩm</div>
        <a href="${pageContext.request.contextPath}/admin/product"
           class="admin-nav-item" id="nav-product">
            <span class="admin-nav-item__icon">👟</span>
            Sản phẩm
        </a>
        <a href="${pageContext.request.contextPath}/admin/category"
           class="admin-nav-item" id="nav-category">
            <span class="admin-nav-item__icon">🗂</span>
            Danh mục
        </a>
        <a href="${pageContext.request.contextPath}/admin/brand"
           class="admin-nav-item" id="nav-brand">
            <span class="admin-nav-item__icon">🏷</span>
            Thương hiệu
        </a>
        <a href="${pageContext.request.contextPath}/admin/inventory"
           class="admin-nav-item" id="nav-inventory">
            <span class="admin-nav-item__icon">📦</span>
            Tồn kho
        </a>
    </div>

    <hr class="admin-sidebar__divider">

    <%-- KINH DOANH --%>
    <div class="admin-sidebar__section">
        <div class="admin-sidebar__section-title">Kinh doanh</div>
        <a href="${pageContext.request.contextPath}/admin/order"
           class="admin-nav-item" id="nav-order">
            <span class="admin-nav-item__icon">🧾</span>
            Đơn hàng
            <%-- Badge đơn chờ xử lý (Sprint 5 gắn dynamic) --%>
            <c:if test="${pendingOrderCount > 0}">
                <span class="admin-nav-item__badge">${pendingOrderCount}</span>
            </c:if>
        </a>
        <a href="${pageContext.request.contextPath}/admin/customer"
           class="admin-nav-item" id="nav-customer">
            <span class="admin-nav-item__icon">👥</span>
            Khách hàng
        </a>
        <a href="${pageContext.request.contextPath}/admin/voucher"
           class="admin-nav-item" id="nav-voucher">
            <span class="admin-nav-item__icon">🎫</span>
            Voucher
        </a>
    </div>

    <hr class="admin-sidebar__divider">

    <%-- NỘI DUNG --%>
    <div class="admin-sidebar__section">
        <div class="admin-sidebar__section-title">Nội dung</div>
        <a href="${pageContext.request.contextPath}/admin/review"
           class="admin-nav-item" id="nav-review">
            <span class="admin-nav-item__icon">⭐</span>
            Đánh giá
        </a>
    </div>

    <hr class="admin-sidebar__divider">

    <%-- USER INFO --%>
    <div class="admin-sidebar__section" style="margin-top:auto;">
        <a href="${pageContext.request.contextPath}/logout" class="admin-nav-item" id="nav-admin-logout">
            <span class="admin-nav-item__icon">🚪</span>
            Đăng xuất
            <c:if test="${not empty sessionScope.adminUser}">
                &nbsp;<span style="font-size:var(--fs-caption);color:rgba(255,255,255,.4);">
                    (${sessionScope.adminUser.fullName})
                </span>
            </c:if>
        </a>
    </div>

</aside>
