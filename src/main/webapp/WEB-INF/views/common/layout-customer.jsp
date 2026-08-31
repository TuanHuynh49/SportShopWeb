<%-- layout-customer.jsp — Khung layout 2 cột (Left Sidebar + Streamlined Top Header)
     Dùng chung cho toàn bộ giao diện Customer (MVC Architecture).
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="${not empty pageDescription ? pageDescription : 'SportShop — Website bán đồ thể thao bóng đá chính hãng tại Việt Nam.'}">
    <title>${not empty pageTitle ? pageTitle : 'SportShop'} | SportShop — Đồ Thể Thao Chính Hãng</title>

    <%-- Favicon --%>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/images/icons/favicon.ico" type="image/x-icon">

    <%-- Google Fonts --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <%-- Bootstrap 5 CSS --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">

    <%-- Custom CSS (Design Tokens COLOR_GUIDELINES.md) --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css?v=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css?v=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css?v=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css?v=1.0">

    <%-- Extra head content --%>
    ${extraHead}
</head>
<body>

<%-- Khung App 2 cột: Sidebar bên trái + Main Content bên phải --%>
<div class="app-layout">

    <%-- 1. LEFT SIDEBAR NAVIGATION --%>
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp"/>

    <%-- 2. MAIN WRAPPER (Top Header + Body Content + Footer) --%>
    <div class="app-main-wrapper">

        <%-- STREAMLINED TOP HEADER --%>
        <jsp:include page="/WEB-INF/views/common/header.jsp"/>

        <%-- FLASH MESSAGES --%>
        <div class="container-fluid px-3 px-lg-4" style="padding-top: var(--space-3, 0.75rem);">
            <jsp:include page="/WEB-INF/views/common/flash-message.jsp"/>
        </div>

        <%-- MAIN PAGE CONTENT --%>
        <main class="page-main px-3 px-lg-4" id="main-content" role="main">
            <c:import url="${viewPath}"/>
        </main>

        <%-- FOOTER --%>
        <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

    </div><%-- end app-main-wrapper --%>

</div><%-- end app-layout --%>

<%-- Bootstrap JS (CDN) --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<%-- App Scripts with Cache-Busting Version --%>
<script>window.contextPath = '${pageContext.request.contextPath}';</script>
<script src="${pageContext.request.contextPath}/assets/js/main.js?v=1.0"></script>
<script src="${pageContext.request.contextPath}/assets/js/cart.js?v=1.0"></script>
<script src="${pageContext.request.contextPath}/assets/js/validation.js?v=1.0"></script>
<script src="${pageContext.request.contextPath}/assets/js/voucher.js?v=1.0"></script>
<script src="${pageContext.request.contextPath}/assets/js/product-filter.js?v=1.0"></script>
<script src="${pageContext.request.contextPath}/assets/js/checkout.js?v=1.0"></script>

<%-- Extra scripts --%>
${extraScripts}

</body>
</html>
