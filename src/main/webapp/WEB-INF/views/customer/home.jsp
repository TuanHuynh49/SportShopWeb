<%-- home.jsp — Trang chủ động (MVC JSTL / EL) với xử lý Empty-State chuẩn khi DB trống --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <!-- HERO BANNER -->
    <section class="hero mb-5">
        <div class="container">
            <div class="row align-items-center py-4">
                <div class="col-lg-7 hero-content">
                    <div class="hero-content__badge">Mùa giải bóng đá 2026</div>
                    <h1 class="hero-content__title">
                        BỨT PHÁ TỐC ĐỘ<br>
                        <span>CHINH PHỤC SÂN CỎ</span>
                    </h1>
                    <p class="hero-content__desc">
                        Trang bị giày đá bóng và trang phục thi đấu chính hãng từ Nike, Adidas, Puma với công nghệ đệm êm tối tân và độ bám sân vượt trội.
                    </p>
                    <div class="hero-cta">
                        <a href="${pageContext.request.contextPath}/product/list" class="btn btn-lg btn-light fw-bold text-dark">
                            Khám Phá Bộ Sưu Tập →
                        </a>
                        <a href="${pageContext.request.contextPath}/product/list?sale=true" class="btn btn-lg btn-outline-light">
                            Ưu Đãi Flash Sale ⚡
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- SECTION: SẢN PHẨM NỔI BẬT (FEATURED PRODUCTS) -->
    <section class="mb-5">
        <div class="d-flex align-items-center justify-content-between mb-4">
            <div>
                <h2 class="h4 fw-bold text-primary mb-1">🔥 SẢN PHẨM NỔI BẬT</h2>
                <div class="text-xs text-muted text-uppercase tracking-wider">Top mẫu bán chạy được tin dùng nhất</div>
            </div>
            <a href="${pageContext.request.contextPath}/product/list" class="text-accent fw-bold text-sm text-decoration-none">
                Xem tất cả →
            </a>
        </div>

        <c:choose>
            <%-- KHI CƠ SỞ DỮ LIỆU CÓ SẢN PHẨM --%>
            <c:when test="${not empty featuredProducts}">
                <div class="row g-3 g-lg-4">
                    <c:forEach items="${featuredProducts}" var="p">
                        <div class="col-6 col-md-4 col-lg-3">
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
            </c:when>

            <%-- KHI CƠ SỞ DỮ LIỆU ĐANG ĐỂ TRỐNG (EMPTY STATE) --%>
            <c:otherwise>
                <div class="empty-state-card text-center py-5 px-3 bg-surface rounded-3 border">
                    <div class="display-4 text-muted mb-3">📦</div>
                    <h3 class="h5 fw-bold text-primary mb-2">Chưa có sản phẩm nổi bật trong cơ sở dữ liệu</h3>
                    <p class="text-muted text-sm max-w-md mx-auto mb-4">
                        Dữ liệu đang được thiết lập kết nối từ Database. Khi bạn thêm sản phẩm vào bảng <code>products</code>, hệ thống sẽ tự động hiển thị tại đây.
                    </p>
                    <a href="${pageContext.request.contextPath}/product/list" class="btn btn-sm btn-outline-primary">
                        Xem danh mục sản phẩm
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- SECTION: SẢN PHẨM MỚI NHẤT (NEW ARRIVALS) -->
    <section class="mb-5">
        <div class="d-flex align-items-center justify-content-between mb-4">
            <div>
                <h2 class="h4 fw-bold text-primary mb-1">⚡ HÀNG MỚI VỀ</h2>
                <div class="text-xs text-muted text-uppercase tracking-wider">Cập nhật xu hướng & công nghệ mới nhất</div>
            </div>
            <a href="${pageContext.request.contextPath}/product/list?sort=newest" class="text-accent fw-bold text-sm text-decoration-none">
                Xem tất cả →
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty newArrivals}">
                <div class="row g-3 g-lg-4">
                    <c:forEach items="${newArrivals}" var="p">
                        <div class="col-6 col-md-4 col-lg-3">
                            <div class="product-card h-100">
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
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>

            <c:otherwise>
                <div class="empty-state-card text-center py-5 px-3 bg-surface rounded-3 border">
                    <div class="display-4 text-muted mb-3">👟</div>
                    <h3 class="h5 fw-bold text-primary mb-2">Chưa có sản phẩm mới về trong cơ sở dữ liệu</h3>
                    <p class="text-muted text-sm max-w-md mx-auto">
                        Toàn bộ truy vấn đều kết nối trực tiếp qua DAO/JDBC. Sẵn sàng nhận dữ liệu từ MySQL Database.
                    </p>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- SECTION: CHÍNH SÁCH CAM KẾT -->
    <section class="py-4 px-3 bg-surface rounded-3 border mb-5">
        <div class="row g-4 text-center text-md-start">
            <div class="col-md-3 d-flex align-items-center gap-3">
                <div class="fs-2 text-primary">🚚</div>
                <div>
                    <div class="fw-bold text-sm text-primary">Giao Hàng Toàn Quốc</div>
                    <div class="text-muted text-xs">Miễn phí cho đơn từ 500k</div>
                </div>
            </div>
            <div class="col-md-3 d-flex align-items-center gap-3">
                <div class="fs-2 text-primary">🛡️</div>
                <div>
                    <div class="fw-bold text-sm text-primary">100% Chính Hãng</div>
                    <div class="text-muted text-xs">Cam kết hoàn tiền gấp đôi</div>
                </div>
            </div>
            <div class="col-md-3 d-flex align-items-center gap-3">
                <div class="fs-2 text-primary">🔄</div>
                <div>
                    <div class="fw-bold text-sm text-primary">Đổi Trả 7 Ngày</div>
                    <div class="text-muted text-xs">Thủ tục nhanh chóng, tiện lợi</div>
                </div>
            </div>
            <div class="col-md-3 d-flex align-items-center gap-3">
                <div class="fs-2 text-primary">💳</div>
                <div>
                    <div class="fw-bold text-sm text-primary">Thanh Toán Linh Hoạt</div>
                    <div class="text-muted text-xs">COD hoặc Chuyển khoản QR</div>
                </div>
            </div>
        </div>
    </section>

</div>