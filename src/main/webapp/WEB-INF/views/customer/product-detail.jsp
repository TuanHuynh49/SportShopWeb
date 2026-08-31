<%-- product-detail.jsp — Chi tiết sản phẩm động (MVC JSTL / EL) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <!-- BREADCRUMB -->
    <nav class="breadcrumb-nav mb-4" aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/product/list" class="text-decoration-none">Sản phẩm</a></li>
            <li class="breadcrumb-item active" aria-current="page">${product != null ? product.name : 'Chi tiết'}</li>
        </ol>
    </nav>

    <c:choose>
        <%-- KHI CÓ THÔNG TIN SẢN PHẨM TỪ DATABASE --%>
        <c:when test="${not empty product}">
            <div class="row g-4 mb-5">
                <!-- Gallery Ảnh Sản phẩm -->
                <div class="col-lg-6">
                    <div class="product-gallery bg-surface p-3 rounded-3 border text-center">
                        <img src="${empty product.thumbnailUrl ? pageContext.request.contextPath.concat('/assets/images/no-image.svg') : product.thumbnailUrl}"
                             alt="${product.name}"
                             class="img-fluid rounded-3 mb-3"
                             style="max-height: 450px; object-fit: contain;">
                    </div>
                </div>

                <!-- Thông tin & Mua hàng -->
                <div class="col-lg-6">
                    <div class="product-info bg-surface p-4 rounded-3 border">
                        <div class="text-xs text-muted text-uppercase fw-bold mb-1">${product.brandName} • ${product.categoryName}</div>
                        <h1 class="h3 fw-bold text-primary mb-3">${product.name}</h1>

                        <div class="product-price-box mb-4 p-3 bg-subtle rounded-3 d-flex align-items-baseline gap-3">
                            <span class="fs-3 fw-bold text-accent">
                                <fmt:formatNumber value="${product.effectivePrice}" pattern="#,###"/>₫
                            </span>
                            <c:if test="${product.hasDiscount()}">
                                <span class="text-muted text-decoration-line-through">
                                    <fmt:formatNumber value="${product.price}" pattern="#,###"/>₫
                                </span>
                                <span class="badge badge-sale">-${product.getDiscountPercent()}%</span>
                            </c:if>
                        </div>

                        <!-- Chọn Biến thể Kích thước (Variants) -->
                        <c:if test="${not empty product.variants}">
                            <div class="mb-4">
                                <div class="fw-bold text-sm text-primary mb-2">Chọn Size giày / Kích thước:</div>
                                <div class="d-flex flex-wrap gap-2">
                                    <c:forEach items="${product.variants}" var="v">
                                        <button type="button" class="btn btn-outline-secondary btn-sm px-3 py-2 ${v.inStock ? '' : 'disabled'}">
                                            ${v.size} ${v.inStock ? '' : '(Hết hàng)'}
                                        </button>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                        <div class="d-flex gap-3 mb-4">
                            <button type="button" class="btn btn-primary btn-lg flex-grow-1 fw-bold">
                                🛒 THÊM VÀO GIỎ HÀNG
                            </button>
                            <button type="button" class="btn btn-outline-accent btn-lg px-4">
                                Mua Ngay
                            </button>
                        </div>

                        <div class="border-top pt-3 text-xs text-muted d-flex flex-column gap-2">
                            <div>✓ 100% Hàng chính hãng, cam kết chất lượng</div>
                            <div>✓ Miễn phí giao hàng toàn quốc từ 500k</div>
                            <div>✓ Đổi trả trong vòng 7 ngày</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Mô tả chi tiết -->
            <div class="bg-surface p-4 rounded-3 border mb-5">
                <h3 class="h5 fw-bold text-primary mb-3 pb-2 border-bottom">MÔ TẢ SẢN PHẨM</h3>
                <div class="text-secondary line-height-relaxed">
                    ${empty product.description ? 'Đang cập nhật nội dung mô tả cho sản phẩm này.' : product.description}
                </div>
            </div>
        </c:when>

        <%-- KHI KHÔNG TÌM THẤY SẢN PHẨM TRONG DATABASE (EMPTY STATE) --%>
        <c:otherwise>
            <div class="empty-state-card text-center py-5 px-4 bg-surface rounded-3 border">
                <div class="display-3 text-muted mb-3">👟</div>
                <h2 class="h5 fw-bold text-primary mb-2">Không tìm thấy thông tin sản phẩm trong cơ sở dữ liệu</h2>
                <p class="text-muted text-sm max-w-md mx-auto mb-4">
                    Sản phẩm này có thể chưa được tạo hoặc ID không tồn tại trong bảng <code>products</code>.
                </p>
                <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary px-4">
                    Quay lại danh sách sản phẩm
                </a>
            </div>
        </c:otherwise>
    </c:choose>

</div>