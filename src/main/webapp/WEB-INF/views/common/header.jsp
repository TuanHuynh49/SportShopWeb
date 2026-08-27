<%-- header.jsp — Navbar Customer (sticky, responsive)
     Include trong layout-customer.jsp.
     Data: ${sessionScope.user} (Customer), ${sessionScope.cart} (giỏ hàng)
--%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<header class="site-header" id="site-header">
    <%-- Top promotional bar --%>
    <div class="top-bar">
        ⚡ Miễn phí vận chuyển đơn hàng từ 500.000₫ &nbsp;|&nbsp;
        🏆 Hàng chính hãng 100% &nbsp;|&nbsp;
        📞 Hotline: <strong>1800 xxxx</strong>
    </div>

    <%-- Main navbar --%>
    <nav class="container navbar" role="navigation" aria-label="Menu chính">

        <%-- Logo --%>
        <a href="${pageContext.request.contextPath}/home" class="navbar-brand" aria-label="SportShop - Trang chủ">
            <div class="navbar-brand__logo">S</div>
            <div>
                <div class="navbar-brand__text">SportShop</div>
                <div class="navbar-brand__sub">Đồ thể thao</div>
            </div>
        </a>

        <%-- Search --%>
        <form class="navbar-search" action="${pageContext.request.contextPath}/product/list" method="get" role="search">
            <input type="search"
                   class="navbar-search__input"
                   name="keyword"
                   id="search-input"
                   placeholder="Tìm giày, áo, bóng..."
                   value="${param.keyword}"
                   autocomplete="off"
                   aria-label="Tìm kiếm sản phẩm">
            <button type="submit" class="navbar-search__btn" aria-label="Tìm kiếm">🔍</button>
        </form>

        <%-- Mobile toggle --%>
        <button class="navbar-toggler" id="navbar-toggler" aria-label="Menu" aria-expanded="false" aria-controls="navbar-nav">
            <span></span><span></span><span></span>
        </button>

        <%-- Nav links --%>
        <div class="navbar-nav" id="navbar-nav">

            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <%-- Đã đăng nhập --%>
                    <a href="${pageContext.request.contextPath}/account" class="nav-link" id="nav-account">
                        <span class="nav-link__icon">👤</span>
                        <span>${sessionScope.user.fullName}</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/order/history" class="nav-link" id="nav-orders">
                        <span class="nav-link__icon">📦</span>
                        <span>Đơn hàng</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="nav-link" id="nav-logout">
                        <span class="nav-link__icon">🚪</span>
                        <span>Đăng xuất</span>
                    </a>
                </c:when>
                <c:otherwise>
                    <%-- Chưa đăng nhập --%>
                    <a href="${pageContext.request.contextPath}/login" class="nav-link" id="nav-login">
                        <span class="nav-link__icon">👤</span>
                        <span>Đăng nhập</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/register" class="nav-link" id="nav-register">
                        <span class="nav-link__icon">✏</span>
                        <span>Đăng ký</span>
                    </a>
                </c:otherwise>
            </c:choose>

            <%-- Giỏ hàng --%>
            <a href="${pageContext.request.contextPath}/cart" class="nav-link" id="nav-cart" aria-label="Giỏ hàng">
                <span class="nav-link__icon" style="position:relative;">
                    🛒
                    <c:set var="cartCount" value="${empty sessionScope.cart ? 0 : sessionScope.cart.cartItemCount}"/>
                    <span class="cart-badge" id="cart-item-count"
                          style="display:${cartCount > 0 ? 'flex' : 'none'}">
                        ${cartCount}
                    </span>
                </span>
                <span>Giỏ hàng</span>
            </a>

        </div><%-- end navbar-nav --%>
    </nav>

    <%-- Category navigation bar --%>
    <div class="category-nav">
        <div class="container category-nav__inner">
            <a href="${pageContext.request.contextPath}/product/list" class="category-nav__link">Tất cả</a>
            <c:forEach var="cat" items="${requestScope.navCategories}">
                <a href="${pageContext.request.contextPath}/product/list?categoryId=${cat.categoryId}"
                   class="category-nav__link">${cat.name}</a>
            </c:forEach>
            <c:if test="${empty requestScope.navCategories}">
                <%-- Fallback tĩnh cho Sprint 0/1 (Sprint 3 nối DB thật) --%>
                <a href="${pageContext.request.contextPath}/product/list?categoryId=1" class="category-nav__link">Giày đá bóng</a>
                <a href="${pageContext.request.contextPath}/product/list?categoryId=2" class="category-nav__link">Áo thi đấu</a>
                <a href="${pageContext.request.contextPath}/product/list?categoryId=3" class="category-nav__link">Quần thể thao</a>
                <a href="${pageContext.request.contextPath}/product/list?categoryId=4" class="category-nav__link">Bóng đá</a>
                <a href="${pageContext.request.contextPath}/product/list?categoryId=5" class="category-nav__link">Phụ kiện</a>
                <a href="${pageContext.request.contextPath}/product/list?categoryId=6" class="category-nav__link">Bảo hộ</a>
            </c:if>
        </div>
    </div>

</header>
