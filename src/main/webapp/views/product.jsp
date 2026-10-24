<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${product.productName} - SPORTSHOP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/product.css">
</head>
<body>

    <jsp:include page="/views/common/header.jsp" />

    <main class="pd-main">

        <nav class="pd-breadcrumb">
            <a href="${pageContext.request.contextPath}/">Trang chủ</a>
            <span>&rsaquo;</span>
            <span class="pd-current">${product.productName}</span>
        </nav>

        <section class="pd-gallery">
            <div class="pd-thumbnails">
                <c:forEach var="img" items="${product.images}" varStatus="status">
                    <img src="${img.imageUrl}" alt="${product.productName}" 
                         class="pd-thumb-img ${status.first ? 'active' : ''}" 
                         onclick="changeMainImage(this)">
                </c:forEach>
                <c:if test="${empty product.images}">
                    <img src="${pageContext.request.contextPath}/assets/images/no-image.png" alt="Chưa có ảnh" class="pd-thumb-img active">
                </c:if>
            </div>
            <div class="pd-main-photo-wrapper">
                <c:choose>
                    <c:when test="${not empty product.images}">
                        <img id="mainImage" src="${product.images[0].imageUrl}" alt="${product.productName}">
                    </c:when>
                    <c:otherwise>
                        <img id="mainImage" src="${pageContext.request.contextPath}/assets/images/no-image.png" alt="Chưa có ảnh">
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <aside class="pd-info">

            <p class="pd-brand">${product.brand.name}</p>
            <h1 class="pd-title">${product.productName}</h1>

            <c:if test="${not empty variants}">
                <p class="pd-price">
                    <c:if test="${variants.size() > 1}"><span class="pd-from">Từ</span></c:if>
                    <fmt:formatNumber value="${minPrice}" pattern="#,###"/> đ
                </p>
            </c:if>

            <p class="pd-stock ${inStock ? 'in' : 'out'}">
                <c:choose>
                    <c:when test="${not inStock}">Hết hàng</c:when>
                    <c:when test="${totalStock <= 5}">Chỉ còn ${totalStock} sản phẩm</c:when>
                    <c:otherwise>Còn hàng</c:otherwise>
                </c:choose>
            </p>

            <%-- Thông báo --%>
            <c:if test="${param.added == 1}">
                <p class="pd-notice ok">
                    Đã thêm vào giỏ hàng.
                    <a href="${pageContext.request.contextPath}/CartServlet">Xem giỏ hàng</a>
                </p>
            </c:if>
            <c:if test="${param.error == 'soldout'}">
                <p class="pd-notice err">Phiên bản này vừa hết hàng.</p>
            </c:if>
            <c:if test="${param.error == 'notenough'}">
                <p class="pd-notice err">Không đủ hàng. Tối đa còn ${param.max} sản phẩm cho phiên bản này.</p>
            </c:if>

            <c:choose>
                <c:when test="${not inStock}">
                    <button type="button" class="pd-add" disabled>HẾT HÀNG</button>
                </c:when>

                <c:otherwise>
                    <form class="pd-form" action="${pageContext.request.contextPath}/cart" method="post">
                        <input type="hidden" name="action" value="add">

                        <fieldset class="pd-field">
                            <legend>Chọn size / màu</legend>
                            <ul class="pd-options">
                                <c:forEach var="v" items="${variants}">
                                    <li>
                                        <label class="pd-option">
                                            <input type="radio" name="variantId" value="${v.variantId}"
                                                   ${v.quantity <= 0 ? 'disabled' : ''} required>
                                            <span class="pd-chip">
                                                <strong>${v.size}</strong>
                                                <small>${v.color}</small>
                                                <small><fmt:formatNumber value="${v.price}" pattern="#,###"/> đ</small>
                                                <c:if test="${v.quantity <= 0}"><em>Hết hàng</em></c:if>
                                                <c:if test="${v.quantity > 0 && v.quantity <= 5}">
                                                    <em class="low">Còn ${v.quantity}</em>
                                                </c:if>
                                            </span>
                                        </label>
                                    </li>
                                </c:forEach>
                            </ul>
                        </fieldset>

                        <div class="pd-field pd-qty">
                            <label for="quantity">Số lượng</label>
                            <select id="quantity" name="quantity">
                                <c:forEach var="q" begin="1" end="10">
                                    <option value="${q}">${q}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="pd-add">THÊM VÀO GIỎ HÀNG</button>
                    </form>
                </c:otherwise>
            </c:choose>

            <%-- Accordion (HTML thuần, không cần JS phức tạp) --%>
            <div class="pd-accordion">
                <details open>
                    <summary>Mô tả</summary>
                    <div class="pd-panel">
                        <p class="pd-desc">${product.description}</p>
                    </div>
                </details>

                <details>
                    <summary>Thông tin sản phẩm</summary>
                    <div class="pd-panel">
                        <ul class="pd-specs">
                            <li><span>Thương hiệu</span> ${product.brand.name}</li>
                            <li><span>Danh mục</span> ${product.category.name}</li>
                            <li><span>Mã sản phẩm</span> #${product.productId}</li>
                        </ul>
                    </div>
                </details>
            </div>

        </aside>

    </main>

    <jsp:include page="/views/common/footer.jsp" />

    <script>
        function changeMainImage(element) {
            const mainImage = document.getElementById('mainImage');
            mainImage.src = element.src;
            
            // Xóa và thiết lập class active cho thumbnail được chọn
            document.querySelectorAll('.pd-thumb-img').forEach(img => img.classList.remove('active'));
            element.classList.add('active');
        }
    </script>
</body>
</html>