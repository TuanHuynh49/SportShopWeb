<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${album.name} - Lookbook SPORTSHOP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/album.css">
</head>
<body>

    <jsp:include page="/views/common/header.jsp" />

    <main class="album-container">

        <!-- Breadcrumb điều hướng -->
        <div class="album-breadcrumb">
            <a href="${pageContext.request.contextPath}/HomeServlet">Home</a> &rsaquo; 
            <a href="${pageContext.request.contextPath}/AlbumServlet">Lookbook & Outfits</a> &rsaquo; 
            <span>${album.name}</span>
        </div>

        <!-- Khối thông tin tổng quan của Album -->
        <section class="album-detail-hero">
            <div class="album-detail-meta">
                <div>
                    <h1 class="album-detail-title">${album.name}</h1>
                    <p class="album-detail-desc">${album.description}</p>
                </div>

                <!-- Tính tổng giá tiền của cả bộ phối đồ -->
                <c:set var="totalPrice" value="0" />
                <c:forEach var="v" items="${album.items}">
                    <c:set var="totalPrice" value="${totalPrice + v.price}" />
                </c:forEach>

                <div class="album-price-box">
                    <span class="album-price-label">Complete Set Value</span>
                    <span class="album-price-val">
                        <fmt:formatNumber value="${totalPrice}" pattern="#,###" /> đ
                    </span>
                </div>
            </div>
        </section>

        <!-- Danh sách các món đồ trong Album -->
        <h2 class="album-items-section-title">
            <i class="fa-solid fa-shirt"></i>
            <span>Items in this Set (${not empty album.items ? album.items.size() : 0} items)</span>
        </h2>

        <c:if test="${empty album.items}">
            <div class="album-empty-state">
                <i class="fa-solid fa-shirt"></i>
                <p>No products currently assigned to this lookbook set.</p>
            </div>
        </c:if>

        <ul class="album-items-grid">
            <c:forEach var="item" items="${album.items}">
                <li class="album-item-card">
                    <div class="album-item-thumb">
                        <c:choose>
                            <c:when test="${not empty item.product && not empty item.product.images}">
                                <img src="${item.product.images[0].imageUrl}" alt="${item.product.productName}" loading="lazy">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/assets/images/no-image.png" alt="No image" loading="lazy">
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Thông tin chi tiết biến thể -->
                    <div class="album-item-info">
                        <div>
                            <h3 class="album-item-name">${item.product.productName}</h3>
                            
                            <div class="album-item-badges">
                                <c:if test="${not empty item.size}">
                                    <span class="badge-tag">Size: ${item.size}</span>
                                </c:if>
                                <c:if test="${not empty item.color}">
                                    <span class="badge-tag">Color: ${item.color}</span>
                                </c:if>
                            </div>

                            <div class="album-item-price">
                                <fmt:formatNumber value="${item.price}" pattern="#,###" /> đ
                            </div>
                        </div>

                        <!-- Nút xem chi tiết sản phẩm gốc -->
                        <a href="${pageContext.request.contextPath}/ProductDetailServlet?id=${item.product.productId}" class="album-item-btn">
                            View Product
                        </a>
                    </div>
                </li>
            </c:forEach>
        </ul>

    </main>

    <jsp:include page="/views/common/footer.jsp" />

</body>
</html>
