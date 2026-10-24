<%-- 
    Document   : header
    Created on : 9 Oct 2026, 11:44:38 AM
    Author     : Anh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Khung Header dùng chung - Phong cách FC Barcelona / Nike Store -->
<header class="site-header">
    <div class="header-container">
        
        <!-- BÊN TRÁI: Nút 3 gạch mở danh mục & Nút tìm kiếm -->
        <div class="header-left">
            <button type="button" class="action-btn hamburger-btn" id="openDrawerBtn" onclick="openDrawer()" title="Open Categories Menu">
                <i class="fa-solid fa-bars"></i>
            </button>
            <a href="${pageContext.request.contextPath}/HomeServlet?action=search" class="action-btn" title="Search products">
                <i class="fa-solid fa-magnifying-glass"></i>
            </a>
        </div>

        <!-- Ở GIỮA: Logo thương hiệu SPORTSHOP -->
        <div class="header-center">
            <div class="header-logo">
                <a href="${pageContext.request.contextPath}/HomeServlet">
                    <i class="fa-solid fa-bolt"></i>
                    <span>SPORTSHOP</span>
                </a>
            </div>
        </div>

        <!-- BÊN PHẢI: Tài khoản, Đơn hàng & Giỏ hàng -->
        <div class="header-right header-actions">
            <!-- Nút Quản trị (Dành riêng cho Admin) -->
            <c:if test="${not empty sessionScope.admin}">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="action-btn" title="Admin Dashboard">
                    <i class="fa-solid fa-gauge-high"></i>
                </a>
            </c:if>

            <!-- Nút Tài khoản -->
            <c:choose>
                <c:when test="${not empty sessionScope.customer || not empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/ProfileServlet" class="action-btn" title="My Profile">
                        <i class="fa-regular fa-user"></i>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/LoginServlet" class="action-btn" title="Sign In / Register">
                        <i class="fa-regular fa-user"></i>
                    </a>
                </c:otherwise>
            </c:choose>

            <!-- Nút Đơn hàng -->
            <a href="${pageContext.request.contextPath}/orders" class="action-btn" title="Shopping Order">
                <i class="fa-solid fa-box-open"></i>
            </a>

            <!-- Nút Giỏ hàng -->
            <a href="${pageContext.request.contextPath}/cart" class="action-btn cart-btn" title="Shopping Cart">
                <i class="fa-solid fa-bag-shopping"></i>
                <span class="cart-badge">${not empty sessionScope.cartCount ? sessionScope.cartCount : '0'}</span>
            </a>
        </div>

    </div>
</header>

<!-- Cửa sổ trượt Panel (Side Drawer Menu) tách riêng ra file: /views/common/side-panel.jsp -->
<jsp:include page="/views/common/side-panel.jsp" />
