<%-- layout-admin.jsp — Khung layout dùng chung cho toàn bộ trang Admin.
     Sử dụng tương tự layout-customer.jsp: Servlet set viewPath → forward tới đây.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="noindex, nofollow">
    <title>${not empty pageTitle ? pageTitle : 'Admin'} | SportShop Admin</title>

    <%-- Bootstrap 5 --%>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">

    <%-- Custom CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">

    ${extraHead}
</head>
<body style="background-color:var(--color-bg);">

<div class="admin-layout">

    <%-- SIDEBAR --%>
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp"/>

    <%-- CONTENT AREA --%>
    <div class="admin-content">

        <%-- Topbar --%>
        <div class="admin-topbar">
            <div class="admin-topbar__left">
                <%-- Mobile sidebar toggle --%>
                <button id="admin-sidebar-toggle"
                        aria-label="Toggle sidebar"
                        style="background:none;border:none;cursor:pointer;font-size:1.25rem;color:var(--color-text);padding:4px;">
                    ☰
                </button>
                <div>
                    <div class="admin-topbar__title">${not empty pageTitle ? pageTitle : 'Dashboard'}</div>
                    <div class="admin-topbar__breadcrumb">${not empty breadcrumb ? breadcrumb : 'Admin'}</div>
                </div>
            </div>
            <div class="admin-topbar__right">
                <%-- Notification bell (placeholder) --%>
                <button style="background:none;border:none;cursor:pointer;font-size:1.25rem;color:var(--color-text-muted);position:relative;"
                        aria-label="Thông báo">
                    🔔
                </button>
                <%-- Admin user --%>
                <div class="admin-user" tabindex="0" role="button" aria-label="Tài khoản admin">
                    <div class="admin-user__avatar">
                        <c:choose>
                            <c:when test="${not empty sessionScope.adminUser}">
                                ${fn:substring(sessionScope.adminUser.fullName, 0, 1)}
                            </c:when>
                            <c:otherwise>A</c:otherwise>
                        </c:choose>
                    </div>
                    <div>
                        <div class="admin-user__name">
                            <c:choose>
                                <c:when test="${not empty sessionScope.adminUser}">${sessionScope.adminUser.fullName}</c:when>
                                <c:otherwise>Admin</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="admin-user__role">Quản trị viên</div>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/home" target="_blank"
                   style="font-size:var(--fs-small);color:var(--color-text-muted);">
                    🌐 Xem shop
                </a>
            </div>
        </div>

        <%-- Flash messages --%>
        <div style="padding:var(--space-4) var(--space-6) 0;">
            <jsp:include page="/WEB-INF/views/common/flash-message.jsp"/>
        </div>

        <%-- PAGE CONTENT --%>
        <main class="admin-main" id="admin-main-content" role="main">
            <c:import url="${viewPath}"/>
        </main>

    </div><%-- end admin-content --%>

</div><%-- end admin-layout --%>

<%-- Bootstrap JS --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc4s9bIOgUxi8T/jzmz7U6F+kI2BKYN2DpJ8SQKnEZr"
        crossorigin="anonymous"></script>

<%-- Chart.js (cho dashboard/report — lazy load) --%>
<c:if test="${loadChartJs}">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.2/dist/chart.umd.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/admin/admin-chart.js"></script>
</c:if>

<script>window.contextPath = '${pageContext.request.contextPath}';</script>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin/admin-product.js"></script>

<%-- Active nav item highlight theo URL --%>
<script>
(function(){
    var path = window.location.pathname;
    document.querySelectorAll('.admin-nav-item').forEach(function(el){
        if(el.getAttribute('href') && path.includes(el.getAttribute('href').split('?')[0])){
            el.classList.add('active');
        }
    });
})();
</script>

${extraScripts}

</body>
</html>
