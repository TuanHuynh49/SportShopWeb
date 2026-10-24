<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Lớp nền tối mờ khi mở panel -->
<div class="drawer-overlay" id="drawerOverlay" onclick="closeDrawer()"></div>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/drawer.css">
<!-- Cửa sổ trượt danh mục -->
<aside class="drawer-menu" id="drawerMenu">
    
    <!-- Tiêu đề & nút đóng -->
    <div class="drawer-header">
        <a href="${pageContext.request.contextPath}/HomeServlet" class="drawer-logo">
            <i class="fa-solid fa-bolt"></i>
            <span>SPORTSHOP</span>
        </a>
        <button type="button" class="drawer-close-btn" onclick="closeDrawer()">
            <i class="fa-solid fa-xmark"></i>
        </button>
    </div>

    <!-- Thanh tìm kiếm nhanh dùng HTML Form thuần -->
    <div class="drawer-search-box">
        <form action="${pageContext.request.contextPath}/HomeServlet#products" method="get">
            <div class="drawer-search-input-wrapper">
                <input type="text" name="keyword" value="${param.keyword}" placeholder="Tìm sản phẩm...">
                <button type="submit">Tìm</button>
            </div>
        </form>
    </div>

    <!-- Nội dung cuộn -->
    <div class="drawer-body">
        
        <!-- Tab danh mục chính (Kiểm tra active bằng JSTL c:if) -->
        <div class="drawer-tabs">
            <a href="${pageContext.request.contextPath}/AlbumServlet" 
               class="drawer-tab ${empty param.cat && empty param.tag ? 'active' : ''}">
                LOOKBOOK
            </a>

            <c:forEach var="cat" items="${categoryList}">
                <a href="${pageContext.request.contextPath}/HomeServlet?cat=${cat.categoryId}#products" 
                   class="drawer-tab ${param.cat == cat.categoryId ? 'active' : ''}">
                    ${cat.name}
                </a>
            </c:forEach>

            <a href="${pageContext.request.contextPath}/HomeServlet?tag=sale#products" 
               class="drawer-tab ${param.tag == 'sale' ? 'active' : ''}">
                SALE
            </a>
        </div>

        <!-- Ảnh Album/Lookbook (Load bằng JSTL, không dùng JS) -->
        <c:if test="${not empty albumList}">
            <div class="drawer-cards-grid">
                <c:forEach var="alb" items="${albumList}">
                    <a href="${pageContext.request.contextPath}/AlbumServlet?id=${alb.albumId}" class="drawer-card-item">
                        <div class="drawer-card-thumb">
                            <c:choose>
                                <c:when test="${not empty alb.imageUrl}">
                                    <img src="${alb.imageUrl}" alt="${alb.name}">
                                </c:when>
                                <c:otherwise>
                                    <div class="no-thumb-placeholder">
                                        <i class="fa-solid fa-shirt"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <span>${alb.name}</span>
                    </a>
                </c:forEach>
            </div>
        </c:if>

        <!-- |01| BỘ SƯU TẬP (LOOKBOOK) -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|01|</span>
                <span class="drawer-section-title">COLLECTIONS & LOOKBOOK</span>
            </div>
            <ul class="drawer-link-list">
                <li>
                    <a href="${pageContext.request.contextPath}/AlbumServlet" 
                       class="${empty param.id ? 'active-link' : ''}">
                        ALL COLLECTIONS
                    </a>
                </li>
                <c:forEach var="alb" items="${albumList}">
                    <li>
                        <a href="${pageContext.request.contextPath}/AlbumServlet?id=${alb.albumId}" 
                           class="${param.id == alb.albumId ? 'active-link' : ''}">
                            ${alb.name}
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <!-- |02| CATEGORIES -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|02|</span>
                <span class="drawer-section-title">CATEGORIES</span>
            </div>
            <ul class="drawer-link-list">
                <c:forEach var="cat" items="${categoryList}">
                    <li>
                        <a href="${pageContext.request.contextPath}/HomeServlet?cat=${cat.categoryId}#products" 
                           class="${param.cat == cat.categoryId ? 'active-link' : ''}">
                            ${cat.name}
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <!-- |03| BRANDS -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|03|</span>
                <span class="drawer-section-title">BRANDS</span>
            </div>
            <ul class="drawer-link-list">
                <c:forEach var="brand" items="${brandList}">
                    <li>
                        <a href="${pageContext.request.contextPath}/HomeServlet?brand=${brand.brandId}#products" 
                           class="${param.brand == brand.brandId ? 'active-link' : ''}">
                            ${brand.name}
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <!-- |04| EXPLORE -->
        <div class="drawer-section">
            <div class="drawer-section-heading">
                <span class="drawer-idx">|04|</span>
                <span class="drawer-section-title">EXPLORE</span>
            </div>
            <ul class="drawer-link-list">
                <li><a href="${pageContext.request.contextPath}/HomeServlet">HOME PAGE</a></li>
            </ul>
        </div>

    </div>
</aside>

<!-- JavaScript điều khiển đóng mở Side Panel -->
<script src="${pageContext.request.contextPath}/assets/js/drawer.js"></script>