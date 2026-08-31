<%-- layout-admin.jsp — Khung layout Admin đồng bộ cấu trúc 2 cột với Customer
     - Cột trái: Left Sidebar Navigation cố định, hỗ trợ thu gọn/mở rộng.
     - Cột phải: Top Header tinh gọn (Hamburger, Breadcrumbs, User Avatar) + Nội dung chính.
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
    <title>${not empty pageTitle ? pageTitle : 'Quản Trị Hệ Thống'} | SportShop Admin</title>

    <%-- Google Fonts & Bootstrap 5 --%>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@500;600;700;800&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">

    <%-- Custom CSS --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css?v=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css?v=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css?v=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css?v=1.0">

    ${extraHead}
</head>
<body>

<div class="app-layout" id="app-layout">

    <%-- 1. LEFT SIDEBAR NAVIGATION CHO ADMIN --%>
    <jsp:include page="/WEB-INF/views/common/admin-sidebar.jsp"/>

    <%-- 2. MAIN WRAPPER (TOPBAR + CONTENT) --%>
    <div class="app-main-wrapper">

        <%-- Top Header Admin Tinh Gọn --%>
        <header class="site-header site-header--streamlined" id="site-header">
            <div class="container-fluid px-3 px-lg-4 header-inner">
                <%-- Trái: Hamburger Toggle + Tiêu đề Phân Hệ --%>
                <div class="header-left d-flex align-items-center gap-3">
                    <button class="sidebar-toggle-btn" id="sidebar-toggle" aria-label="Mở/Đóng Menu" title="Mở/Đóng Menu">
                        <span>☰</span>
                    </button>
                    <div>
                        <div class="fw-bold text-sm text-primary">${not empty pageTitle ? pageTitle : 'Dashboard Quản Trị'}</div>
                        <div class="text-xs text-muted">SportShop Admin Portal • Mùa giải 2026</div>
                    </div>
                </div>

                <%-- Phải: Nút Xem Shop + Thông tin Admin User --%>
                <div class="header-right d-flex align-items-center gap-3">
                    <a href="${pageContext.request.contextPath}/" target="_blank" class="btn btn-sm btn-outline-secondary d-none d-sm-inline-flex align-items-center gap-1">
                        <span>🌐</span> Xem Website Khách
                    </a>

                    <div class="dropdown">
                        <div class="d-flex align-items-center gap-2" role="button" data-bs-toggle="dropdown">
                            <div class="avatar-circle" style="width: 34px; height: 34px; border-radius: 8px; background: #0f172a; color: #fff; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 0.85rem;">
                                ${empty sessionScope.authUser ? 'A' : sessionScope.authUser.fullName.substring(0, 1).toUpperCase()}
                            </div>
                            <div class="d-none d-md-block text-start">
                                <div class="fw-bold text-xs text-primary">${empty sessionScope.authUser ? 'Quản Trị Viên' : sessionScope.authUser.fullName}</div>
                                <div class="text-xs text-accent">ADMINISTRATOR</div>
                            </div>
                        </div>
                    </div>

                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-danger" title="Đăng Xuất">
                        Đăng Xuất
                    </a>
                </div>
            </div>
        </header>

        <%-- MAIN CONTENT BODY --%>
        <main class="app-content p-3 p-lg-4" id="main-content" role="main">
            <jsp:include page="${viewPath}"/>
        </main>

        <%-- FOOTER GỌN NHẸ CHO ADMIN --%>
        <footer class="py-3 px-4 border-top bg-surface text-center text-xs text-muted">
            SportShop Admin Management System © 2026 — Kiến trúc chuẩn OR-Mapping & MVC Jakarta EE
        </footer>

    </div><%-- end app-main-wrapper --%>

</div><%-- end app-layout --%>

<%-- JavaScripts --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/main.js?v=1.0"></script>

${extraScripts}

</body>
</html>
