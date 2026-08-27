<%-- layout-customer.jsp — Khung layout dùng chung cho toàn bộ trang Customer.
     Servlet forward tới trang con, trang con dùng jsp:include để nhúng layout này,
     hoặc cách đơn giản hơn: Servlet setRequest attribute "pageTitle" + "bodyContent"
     rồi forward tới layout → layout include trang con qua c:import.

     Cách dùng: Servlet setAttribute("viewPath", "/WEB-INF/views/customer/home.jsp")
                rồi forward sang layout-customer.jsp.
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

    <%-- Google Fonts (preconnect để tăng tốc) --%>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <%-- Bootstrap 5 CSS (CDN) --%>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">

    <%-- Custom CSS (thứ tự quan trọng: base → layout → components → customer) --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">

    <%-- Extra head content từ trang con (nếu cần) --%>
    ${extraHead}
</head>
<body>

<%-- Toàn bộ trang bọc trong page-wrapper để sticky footer --%>
<div class="page-wrapper">

    <%-- HEADER --%>
    <jsp:include page="/WEB-INF/views/common/header.jsp"/>

    <%-- FLASH MESSAGES — ngay dưới header --%>
    <div class="container" style="padding-top:var(--space-4);">
        <jsp:include page="/WEB-INF/views/common/flash-message.jsp"/>
    </div>

    <%-- MAIN CONTENT — trang con được include qua viewPath attribute --%>
    <main class="page-main" id="main-content" role="main">
        <c:import url="${viewPath}"/>
    </main>

    <%-- FOOTER --%>
    <jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</div><%-- end page-wrapper --%>

<%-- Bootstrap 5 JS Bundle --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc4s9bIOgUxi8T/jzmz7U6F+kI2BKYN2DpJ8SQKnEZr"
        crossorigin="anonymous"></script>

<%-- Custom JS --%>
<script>window.contextPath = '${pageContext.request.contextPath}';</script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/voucher.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/product-filter.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/checkout.js"></script>

<%-- Extra scripts từ trang con (nếu cần thêm page-specific JS) --%>
${extraScripts}

</body>
</html>
