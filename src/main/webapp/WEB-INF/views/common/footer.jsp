<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<footer class="site-footer" role="contentinfo">
    <div class="container">
        <div class="footer-grid">

            <%-- Column 1: Brand & About --%>
            <div class="footer-col">
                <div class="footer-brand__logo">
                    <div class="footer-brand__icon">S</div>
                    <div>
                        <div class="footer-brand__name">SportShop</div>
                        <div class="footer-brand__tagline">Đồ thể thao chính hãng</div>
                    </div>
                </div>
                <p class="footer-brand__desc">
                    Chuyên cung cấp đồ thể thao bóng đá chính hãng từ các thương hiệu hàng đầu thế giới.
                    Đồng hành cùng mọi đam mê thể thao của bạn.
                </p>
                <div class="footer-socials">
                    <a href="#" class="footer-socials__link" aria-label="Facebook">f</a>
                    <a href="#" class="footer-socials__link" aria-label="Instagram">📷</a>
                    <a href="#" class="footer-socials__link" aria-label="YouTube">▶</a>
                    <a href="#" class="footer-socials__link" aria-label="TikTok">♪</a>
                </div>
            </div>

            <%-- Column 2: Navigation --%>
            <div class="footer-col">
                <div class="footer-col__title">Mua sắm</div>
                <ul class="footer-col__list">
                    <li><a href="${pageContext.request.contextPath}/product/list">Tất cả sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/product/list?categoryId=1">Giày đá bóng</a></li>
                    <li><a href="${pageContext.request.contextPath}/product/list?categoryId=2">Áo thi đấu</a></li>
                    <li><a href="${pageContext.request.contextPath}/product/list?categoryId=3">Quần thể thao</a></li>
                    <li><a href="${pageContext.request.contextPath}/product/list?categoryId=4">Bóng đá</a></li>
                    <li><a href="${pageContext.request.contextPath}/product/list?brandId=sale">Khuyến mãi</a></li>
                </ul>
            </div>

            <%-- Column 3: Account --%>
            <div class="footer-col">
                <div class="footer-col__title">Tài khoản</div>
                <ul class="footer-col__list">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <li><a href="${pageContext.request.contextPath}/account">Hồ sơ cá nhân</a></li>
                            <li><a href="${pageContext.request.contextPath}/order/history">Lịch sử đơn hàng</a></li>
                            <li><a href="${pageContext.request.contextPath}/address">Địa chỉ giao hàng</a></li>
                            <li><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
                            <li><a href="${pageContext.request.contextPath}/register">Đăng ký</a></li>
                        </c:otherwise>
                    </c:choose>
                    <li><a href="${pageContext.request.contextPath}/cart">Giỏ hàng</a></li>
                </ul>
            </div>

            <%-- Column 4: Contact --%>
            <div class="footer-col">
                <div class="footer-col__title">Liên hệ</div>
                <div class="footer-col__contact-item">
                    <span class="icon">📍</span>
                    <span>123 Đường Thể Thao, Quận 1, TP.HCM</span>
                </div>
                <div class="footer-col__contact-item">
                    <span class="icon">📞</span>
                    <span>1800 xxxx (Miễn phí)</span>
                </div>
                <div class="footer-col__contact-item">
                    <span class="icon">✉</span>
                    <span>support@sportshop.vn</span>
                </div>
                <div class="footer-col__contact-item">
                    <span class="icon">🕐</span>
                    <span>8:00 – 22:00, Thứ 2 – CN</span>
                </div>
            </div>

        </div><%-- end footer-grid --%>
    </div>

    <%-- Bottom bar --%>
    <div class="container footer-bottom">
        <span>© <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy"/> SportShop. Mọi quyền được bảo lưu.</span>
        <div style="display:flex;gap:1rem;">
            <a href="#">Chính sách bảo mật</a>
            <a href="#">Điều khoản sử dụng</a>
            <a href="#">Chính sách đổi trả</a>
        </div>
    </div>

    <%-- Back to top button --%>
    <button id="back-to-top"
            title="Về đầu trang"
            aria-label="Về đầu trang"
            style="display:none;position:fixed;bottom:2rem;right:2rem;width:44px;height:44px;
                   background:var(--color-primary);color:#fff;border:none;border-radius:50%;
                   cursor:pointer;font-size:1.25rem;align-items:center;justify-content:center;
                   box-shadow:var(--shadow-lg);z-index:var(--z-overlay);transition:all var(--transition-fast);">
        ↑
    </button>

</footer>
