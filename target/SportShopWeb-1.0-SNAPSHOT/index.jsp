<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SportShop — Đồ Thể Thao & Bóng Đá Chính Hãng</title>
    
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    
    <!-- Custom Design Tokens & CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">
    
    <style>
        /* Preview Floating Bar */
        .preview-bar {
            background: #0f172a;
            color: #ffffff;
            padding: 8px 16px;
            font-size: 13px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            border-bottom: 1px solid #334155;
            position: sticky;
            top: 0;
            z-index: 1100;
        }
        .preview-bar a {
            color: #94a3b8;
            text-decoration: none;
            padding: 4px 10px;
            border-radius: 4px;
            font-weight: 500;
            transition: all 0.2s;
        }
        .preview-bar a:hover, .preview-bar a.active {
            color: #ffffff;
            background: rgba(255,255,255,0.1);
        }
        .preview-bar .badge-mode {
            background: var(--color-accent);
            color: #fff;
            padding: 2px 8px;
            border-radius: 4px;
            font-weight: 700;
            font-size: 11px;
        }
    </style>
</head>
<body>

    <!-- PREVIEW QUICK NAVIGATION BAR -->
    <div class="preview-bar">
        <div class="d-flex align-items-center gap-2">
            <span class="badge-mode">UI SHOWCASE</span>
            <span class="text-white fw-bold">Sprint 1 Preview Hub</span>
        </div>
        <div class="d-flex gap-1 flex-wrap">
            <a href="${pageContext.request.contextPath}/" class="active">🏠 Trang chủ</a>
            <a href="${pageContext.request.contextPath}/preview/product-list.html">👟 Danh sách SP</a>
            <a href="${pageContext.request.contextPath}/preview/product-detail.html">🔍 Chi tiết SP</a>
            <a href="${pageContext.request.contextPath}/preview/cart.html">🛒 Giỏ hàng</a>
            <a href="${pageContext.request.contextPath}/preview/checkout.html">💳 Thanh toán</a>
            <a href="${pageContext.request.contextPath}/preview/order-history.html">📦 Đơn hàng</a>
            <a href="${pageContext.request.contextPath}/preview/admin-dashboard.html" style="color: #93c5fd;">⚡ Admin Dashboard</a>
        </div>
    </div>

    <!-- SITE HEADER -->
    <header class="site-header">
        <div class="top-bar">
            Miễn phí vận chuyển cho đơn hàng từ 500.000₫ • Cam kết 100% chính hãng • Hotline: 1900 6868
        </div>
        
        <div class="container navbar">
            <!-- Logo -->
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">
                <div class="navbar-brand__logo">S</div>
                <div>
                    <div class="navbar-brand__text">SPORT<span class="text-accent">SHOP</span></div>
                    <div class="navbar-brand__sub">Athletic Gear</div>
                </div>
            </a>

            <!-- Search -->
            <form class="navbar-search" action="${pageContext.request.contextPath}/preview/product-list.html" method="get">
                <input type="search" class="navbar-search__input" placeholder="Tìm giày đá bóng, áo CLB, phụ kiện...">
                <button type="submit" class="navbar-search__btn">🔍</button>
            </form>

            <!-- Nav Action Links -->
            <div class="navbar-nav">
                <a href="${pageContext.request.contextPath}/preview/order-history.html" class="nav-link">
                    <span>Đơn mua</span>
                </a>
                <a href="${pageContext.request.contextPath}/preview/cart.html" class="nav-link">
                    <span>Giỏ hàng</span>
                    <span class="cart-badge">3</span>
                </a>
            </div>
        </div>

        <!-- Category Menu Bar -->
        <div class="category-nav">
            <div class="container category-nav__inner">
                <a href="${pageContext.request.contextPath}/preview/product-list.html" class="category-nav__link active">Tất cả sản phẩm</a>
                <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=giay" class="category-nav__link">Giày Đá Bóng</a>
                <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=ao" class="category-nav__link">Áo Thi Đấu CLB</a>
                <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=quan" class="category-nav__link">Quần Thể Thao</a>
                <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=bong" class="category-nav__link">Quả Bóng Đá</a>
                <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=phukien" class="category-nav__link">Găng Tay & Vớ</a>
                <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=sale" class="category-nav__link text-accent fw-bold">Khuyến Mãi</a>
            </div>
        </div>
    </header>

    <!-- HERO BANNER (Stealth Athletic) -->
    <section class="hero">
        <div class="container">
            <div class="row align-items-center py-4">
                <div class="col-lg-6 hero-content">
                    <div class="hero-content__badge">Bộ sưu tập mùa giải 2026</div>
                    <h1 class="hero-content__title">
                        BỨT PHÁ TỐC ĐỘ<br>
                        <span>CHINH PHỤC SÂN CỎ</span>
                    </h1>
                    <p class="hero-content__desc">
                        Trang bị giày đá bóng và trang phục thi đấu chính hãng từ Nike, Adidas, Puma với công nghệ đệm êm tối tân và độ bám sân vượt trội.
                    </p>
                    <div class="hero-cta">
                        <a href="${pageContext.request.contextPath}/preview/product-list.html" class="btn btn-primary btn-lg" style="background:#fff; color:#0f172a; border-color:#fff;">Khám phá ngay →</a>
                        <a href="${pageContext.request.contextPath}/preview/product-list.html?sale=true" class="btn btn-outline-secondary btn-lg" style="color:#fff; border-color: rgba(255,255,255,0.3); background: transparent;">Săn Voucher 30%</a>
                    </div>
                </div>
                <div class="col-lg-6 text-center d-none d-lg-block">
                    <img src="${pageContext.request.contextPath}/assets/images/products/shoe-1.svg" 
                         alt="Giày bóng đá" 
                         class="img-fluid rounded-4 shadow-lg mx-auto"
                         style="max-height: 340px; object-fit: contain;">
                </div>
            </div>
        </div>
    </section>

    <!-- MAIN BODY CONTENT -->
    <main class="page-main py-5">
        <div class="container">

            <!-- 1. CATEGORY CARDS -->
            <section class="mb-5">
                <div class="section-header">
                    <div>
                        <h2 class="section-header__title">Danh Mục Nổi Bật</h2>
                    </div>
                    <a href="${pageContext.request.contextPath}/preview/product-list.html" class="section-header__link">Xem tất cả →</a>
                </div>
                
                <div class="category-grid">
                    <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=giay" class="category-card">
                        <div class="category-card__icon">👟</div>
                        <div class="category-card__name">Giày Đá Bóng</div>
                        <div class="category-card__count">128 sản phẩm</div>
                    </a>
                    <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=ao" class="category-card">
                        <div class="category-card__icon">👕</div>
                        <div class="category-card__name">Áo Thi Đấu</div>
                        <div class="category-card__count">95 sản phẩm</div>
                    </a>
                    <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=quan" class="category-card">
                        <div class="category-card__icon">🩳</div>
                        <div class="category-card__name">Quần Thể Thao</div>
                        <div class="category-card__count">64 sản phẩm</div>
                    </a>
                    <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=bong" class="category-card">
                        <div class="category-card__icon">⚽</div>
                        <div class="category-card__name">Quả Bóng Đá</div>
                        <div class="category-card__count">42 sản phẩm</div>
                    </a>
                    <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=gangtay" class="category-card">
                        <div class="category-card__icon">🧤</div>
                        <div class="category-card__name">Găng Thủ Môn</div>
                        <div class="category-card__count">31 sản phẩm</div>
                    </a>
                    <a href="${pageContext.request.contextPath}/preview/product-list.html?cat=phukien" class="category-card">
                        <div class="category-card__icon">🎒</div>
                        <div class="category-card__name">Túi & Phụ Kiện</div>
                        <div class="category-card__count">58 sản phẩm</div>
                    </a>
                </div>
            </section>

            <!-- 2. HIGHLIGHT PRODUCTS -->
            <section class="mb-5">
                <div class="section-header">
                    <div>
                        <h2 class="section-header__title">Sản Phẩm Đang Ưa Chuộng</h2>
                    </div>
                    <a href="${pageContext.request.contextPath}/preview/product-list.html" class="section-header__link">Xem tất cả 120+ sản phẩm →</a>
                </div>

                <div class="product-grid">
                    <!-- Item 1 -->
                    <div class="product-card">
                        <div class="product-card__badge">
                            <span class="badge badge-discount">-35%</span>
                        </div>
                        <div class="product-card__img-wrap">
                            <img src="${pageContext.request.contextPath}/assets/images/products/shoe-1.svg" 
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/no-image.svg'" 
                                 alt="Nike Mercurial" 
                                 class="product-card__img">
                            <div class="product-card__actions">
                                <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__action-btn">Chi tiết</a>
                                <a href="${pageContext.request.contextPath}/preview/cart.html" class="product-card__action-btn">Thêm giỏ</a>
                            </div>
                        </div>
                        <div class="product-card__body">
                            <div class="product-card__brand">NIKE</div>
                            <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__name">Nike Mercurial Superfly 9 Elite FG</a>
                            <div class="product-card__price-row">
                                <span class="product-card__price">2.450.000₫</span>
                                <span class="product-card__price-original">3.800.000₫</span>
                            </div>
                        </div>
                    </div>

                    <!-- Item 2 -->
                    <div class="product-card">
                        <div class="product-card__badge">
                            <span class="badge badge-discount">-20%</span>
                        </div>
                        <div class="product-card__img-wrap">
                            <img src="${pageContext.request.contextPath}/assets/images/products/shoe-2.svg" 
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/no-image.svg'" 
                                 alt="Adidas Predator" 
                                 class="product-card__img">
                            <div class="product-card__actions">
                                <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__action-btn">Chi tiết</a>
                                <a href="${pageContext.request.contextPath}/preview/cart.html" class="product-card__action-btn">Thêm giỏ</a>
                            </div>
                        </div>
                        <div class="product-card__body">
                            <div class="product-card__brand">ADIDAS</div>
                            <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__name">Adidas Predator Accuracy.1 Low FG</a>
                            <div class="product-card__price-row">
                                <span class="product-card__price">2.190.000₫</span>
                                <span class="product-card__price-original">2.750.000₫</span>
                            </div>
                        </div>
                    </div>

                    <!-- Item 3 -->
                    <div class="product-card">
                        <div class="product-card__img-wrap">
                            <img src="${pageContext.request.contextPath}/assets/images/products/ball-1.svg" 
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/no-image.svg'" 
                                 alt="Bóng Adidas Champions League" 
                                 class="product-card__img">
                            <div class="product-card__actions">
                                <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__action-btn">Chi tiết</a>
                                <a href="${pageContext.request.contextPath}/preview/cart.html" class="product-card__action-btn">Thêm giỏ</a>
                            </div>
                        </div>
                        <div class="product-card__body">
                            <div class="product-card__brand">ADIDAS</div>
                            <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__name">Quả Bóng Đá Adidas UEFA Champions League Pro 2026</a>
                            <div class="product-card__price-row">
                                <span class="product-card__price">650.000₫</span>
                                <span class="product-card__price-original">780.000₫</span>
                            </div>
                        </div>
                    </div>

                    <!-- Item 4 -->
                    <div class="product-card">
                        <div class="product-card__badge">
                            <span class="badge badge-discount">-25%</span>
                        </div>
                        <div class="product-card__img-wrap">
                            <img src="${pageContext.request.contextPath}/assets/images/products/jersey-1.svg" 
                                 onerror="this.src='${pageContext.request.contextPath}/assets/images/no-image.svg'" 
                                 alt="Áo Real Madrid" 
                                 class="product-card__img">
                            <div class="product-card__actions">
                                <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__action-btn">Chi tiết</a>
                                <a href="${pageContext.request.contextPath}/preview/cart.html" class="product-card__action-btn">Thêm giỏ</a>
                            </div>
                        </div>
                        <div class="product-card__body">
                            <div class="product-card__brand">ADIDAS</div>
                            <a href="${pageContext.request.contextPath}/preview/product-detail.html" class="product-card__name">Áo Đấu CLB Real Madrid Sân Nhà 2025/2026 Player Issue</a>
                            <div class="product-card__price-row">
                                <span class="product-card__price">450.000₫</span>
                                <span class="product-card__price-original">600.000₫</span>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- 3. BRAND TRUST & VALUE PROPOSITIONS -->
            <section class="row g-3 my-4">
                <div class="col-md-3 col-6">
                    <div class="p-3 bg-surface border rounded-3 d-flex align-items-center gap-3">
                        <span class="fs-4">🚚</span>
                        <div>
                            <div class="fw-bold text-sm text-primary">Giao Hỏa Tốc</div>
                            <div class="text-muted text-xs">Nội thành 2h có hàng</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="p-3 bg-surface border rounded-3 d-flex align-items-center gap-3">
                        <span class="fs-4">🔄</span>
                        <div>
                            <div class="fw-bold text-sm text-primary">Đổi Trả 7 Ngày</div>
                            <div class="text-muted text-xs">Đổi size miễn phí tận nhà</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="p-3 bg-surface border rounded-3 d-flex align-items-center gap-3">
                        <span class="fs-4">🛡</span>
                        <div>
                            <div class="fw-bold text-sm text-primary">Chính Hãng 100%</div>
                            <div class="text-muted text-xs">Bảo hành chính hãng</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 col-6">
                    <div class="p-3 bg-surface border rounded-3 d-flex align-items-center gap-3">
                        <span class="fs-4">💳</span>
                        <div>
                            <div class="fw-bold text-sm text-primary">Thanh Toán Linh Hoạt</div>
                            <div class="text-muted text-xs">COD hoặc Chuyển khoản QR</div>
                        </div>
                    </div>
                </div>
            </section>

        </div>
    </main>

    <!-- FOOTER -->
    <footer class="site-footer">
        <div class="container">
            <div class="footer-grid">
                <div>
                    <div class="footer-brand__logo">
                        <div class="footer-brand__icon">S</div>
                        <div>
                            <div class="footer-brand__name">SportShop</div>
                            <div class="footer-brand__tagline">Athletic Store</div>
                        </div>
                    </div>
                    <p class="footer-brand__desc">
                        Hệ thống bán lẻ trang phục, giày và phụ kiện bóng đá chính hãng số 1 Việt Nam.
                    </p>
                </div>

                <div>
                    <div class="footer-col__title">Danh Mục</div>
                    <ul class="footer-col__list">
                        <li><a href="${pageContext.request.contextPath}/preview/product-list.html?cat=giay">Giày Sân Cỏ Nhân Tạo</a></li>
                        <li><a href="${pageContext.request.contextPath}/preview/product-list.html?cat=ao">Áo Câu Lạc Bộ</a></li>
                        <li><a href="${pageContext.request.contextPath}/preview/product-list.html?cat=bong">Quả Bóng Đá</a></li>
                    </ul>
                </div>

                <div>
                    <div class="footer-col__title">Hỗ Trợ</div>
                    <ul class="footer-col__list">
                        <li><a href="#">Bảng quy đổi size giày</a></li>
                        <li><a href="#">Chính sách đổi trả</a></li>
                        <li><a href="${pageContext.request.contextPath}/preview/order-history.html">Tra cứu đơn hàng</a></li>
                    </ul>
                </div>

                <div>
                    <div class="footer-col__title">Liên Hệ</div>
                    <div class="footer-col__contact-item">
                        <span>📍</span>
                        <span>123 Đường Thể Thao, Quận 1, TP.HCM</span>
                    </div>
                    <div class="footer-col__contact-item">
                        <span>📞</span>
                        <span>1900 6868 (8:00 - 22:00)</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="container footer-bottom">
            <span>© 2026 SportShop. Thiết kế chuẩn Design Tokens & Color Guidelines.</span>
            <div>
                <a href="${pageContext.request.contextPath}/preview/admin-dashboard.html" class="badge badge-dark text-decoration-none">Trang Quản Trị Admin →</a>
            </div>
        </div>
    </footer>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
