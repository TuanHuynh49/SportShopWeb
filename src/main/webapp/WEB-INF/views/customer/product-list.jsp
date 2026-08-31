<%-- product-list.jsp — Danh sách sản phẩm động kèm bộ lọc & phân trang (MVC JSTL / EL) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <!-- BREADCRUMB -->
    <nav class="breadcrumb-nav mb-4" aria-label="breadcrumb">
        <ol class="breadcrumb mb-0">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/" class="text-decoration-none">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Sản phẩm</li>
        </ol>
    </nav>

    <div class="row g-4">
        <!-- BỘ LỌC BÊN TRÁI (FILTER SIDEBAR) -->
        <div class="col-lg-3">
            <div class="filter-sidebar bg-surface p-4 rounded-3 border sticky-top" style="top: 80px; z-index: 10;">
                <div class="d-flex align-items-center justify-content-between mb-3 pb-2 border-bottom">
                    <h3 class="h6 fw-bold mb-0 text-primary">🔍 BỘ LỌC SẢN PHẨM</h3>
                    <a href="${pageContext.request.contextPath}/product/list" class="text-xs text-muted text-decoration-none">Xóa lọc</a>
                </div>

                <form action="${pageContext.request.contextPath}/product/list" method="get">
                    <!-- Danh mục -->
                    <div class="filter-group mb-4">
                        <div class="fw-bold text-xs text-muted text-uppercase mb-2">Danh mục</div>
                        <c:choose>
                            <c:when test="${not empty categories}">
                                <div class="d-flex flex-column gap-1">
                                    <c:forEach items="${categories}" var="cat">
                                        <a href="${pageContext.request.contextPath}/product/list?categoryId=${cat.id}"
                                           class="filter-item text-sm text-secondary text-decoration-none py-1 ${param.categoryId == cat.id ? 'fw-bold text-accent' : ''}">
                                            ${cat.name}
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-xs text-muted">Chưa có danh mục nào trong DB</div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Sắp xếp -->
                    <div class="filter-group mb-4">
                        <div class="fw-bold text-xs text-muted text-uppercase mb-2">Sắp xếp theo giá</div>
                        <select name="sort" class="form-select form-select-sm" onchange="this.form.submit()">
                            <option value="newest" ${empty param.sort || param.sort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                            <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá: Thấp đến Cao</option>
                            <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá: Cao đến Thấp</option>
                        </select>
                    </div>
                </form>
            </div>
        </div>

        <!-- DANH SÁCH SẢN PHẨM CHÍNH -->
        <div class="col-lg-9">
            <!-- Header bar kết quả -->
            <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
                <div class="text-sm text-muted">
                    Hiển thị <strong>${productList != null ? productList.size() : 0}</strong> trên tổng số <strong>${totalCount != null ? totalCount : 0}</strong> sản phẩm
                </div>
            </div>

            <c:choose>
                <%-- KHI CÓ SẢN PHẨM TRONG DATABASE --%>
                <c:when test="${not empty productList}">
                    <div class="row g-3 g-lg-4 mb-4">
                        <c:forEach items="${productList}" var="p">
                            <div class="col-6 col-md-4">
                                <div class="product-card h-100">
                                    <c:if test="${p.hasDiscount()}">
                                        <div class="product-card__badge badge-sale">-${p.getDiscountPercent()}%</div>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" class="product-card__img-wrap">
                                        <img src="${empty p.thumbnailUrl ? pageContext.request.contextPath.concat('/assets/images/no-image.svg') : p.thumbnailUrl}"
                                             alt="${p.name}"
                                             class="product-card__img"
                                             loading="lazy">
                                    </a>
                                    <div class="product-card__body">
                                        <div class="product-card__brand">${p.brandName}</div>
                                        <h3 class="product-card__title">
                                            <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}">${p.name}</a>
                                        </h3>
                                        <div class="product-card__price">
                                            <span class="product-card__price--current">
                                                <fmt:formatNumber value="${p.effectivePrice}" pattern="#,###"/>₫
                                            </span>
                                            <c:if test="${p.hasDiscount()}">
                                                <span class="product-card__price--old">
                                                    <fmt:formatNumber value="${p.price}" pattern="#,###"/>₫
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- PHÂN TRANG -->
                    <c:if test="${totalPage > 1}">
                        <nav class="d-flex justify-content-center mt-5">
                            <ul class="pagination pagination-sm">
                                <c:forEach begin="1" end="${totalPage}" var="i">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/product/list?page=${i}&categoryId=${param.categoryId}&brandId=${param.brandId}&keyword=${param.keyword}&sort=${param.sort}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:if>
                </c:when>

                <%-- KHI KHÔNG CÓ SẢN PHẨM NÀO TRONG DATABASE (EMPTY STATE) --%>
                <c:otherwise>
                    <div class="empty-state-card text-center py-5 px-4 bg-surface rounded-3 border">
                        <div class="display-3 text-muted mb-3">🔍</div>
                        <h3 class="h5 fw-bold text-primary mb-2">Không tìm thấy sản phẩm nào trong cơ sở dữ liệu</h3>
                        <p class="text-muted text-sm max-w-md mx-auto mb-4">
                            Hiện tại bảng <code>products</code> chưa có bản ghi nào hoặc không có sản phẩm nào khớp với bộ lọc hiện tại.
                        </p>
                        <a href="${pageContext.request.contextPath}/product/list" class="btn btn-sm btn-primary">
                            Xem tất cả sản phẩm
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</div>