<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Albums & Lookbooks | SportShop Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body class="admin-body">
    <div class="admin-layout">

        <!-- Sidebar -->
        <aside class="admin-sidebar">
            <div class="admin-brand">
                <span class="admin-brand-mark">S</span>
                <div>
                    <div class="admin-brand-name">SPORTSHOP</div>
                    <span class="admin-brand-caption">Administration</span>
                </div>
            </div>

            <div class="admin-nav-label">Workspace</div>

            <nav class="admin-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard">
                    <span class="admin-nav-icon">▦</span>Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/admin/products">
                    <span class="admin-nav-icon">▣</span>Products
                </a>
                <a class="active" href="${pageContext.request.contextPath}/admin/albums">
                    <span class="admin-nav-icon">☷</span>Albums
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders">
                    <span class="admin-nav-icon">⇄</span>Orders
                </a>
                <a href="${pageContext.request.contextPath}/admin/customers">
                    <span class="admin-nav-icon">♙</span>Customers
                </a>
            </nav>

            <div class="admin-sidebar-bottom">
                <nav class="admin-nav">
                    <a href="${pageContext.request.contextPath}/HomeServlet">
                        <span class="admin-nav-icon">↗</span>Back to Store
                    </a>
                </nav>
            </div>
        </aside>

        <!-- Main content -->
        <main class="admin-main">
            <header class="admin-topbar">
                <div>
                    <h1 class="admin-topbar-title">Albums & Lookbooks</h1>
                    <p class="admin-topbar-subtitle">Workspace / Album Management</p>
                </div>
                <div class="admin-topbar-actions">
                    <span class="admin-date-label">ADMIN</span>
                </div>
            </header>

            <section class="admin-content">

                <!-- Alert Messages -->
                <c:if test="${param.createSuccess == '1'}">
                    <div class="admin-alert success" style="margin-bottom: 20px; padding: 12px 16px; background: #ecfdf5; color: #065f46; border: 1px solid #a7f3d0; border-radius: 6px;">
                        <i class="fa-solid fa-circle-check"></i> Thêm mới Album Lookbook thành công! Dữ liệu đã đồng bộ lên Trang chủ.
                    </div>
                </c:if>

                <c:if test="${param.updateSuccess == '1'}">
                    <div class="admin-alert success" style="margin-bottom: 20px; padding: 12px 16px; background: #ecfdf5; color: #065f46; border: 1px solid #a7f3d0; border-radius: 6px;">
                        <i class="fa-solid fa-circle-check"></i> Cập nhật Album thành công!
                    </div>
                </c:if>

                <c:if test="${param.deleteSuccess == '1'}">
                    <div class="admin-alert success" style="margin-bottom: 20px; padding: 12px 16px; background: #ecfdf5; color: #065f46; border: 1px solid #a7f3d0; border-radius: 6px;">
                        <i class="fa-solid fa-circle-check"></i> Xóa Album thành công!
                    </div>
                </c:if>

                <c:if test="${param.deleteError == '1'}">
                    <div class="admin-alert error" style="margin-bottom: 20px; padding: 12px 16px; background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; border-radius: 6px;">
                        <i class="fa-solid fa-triangle-exclamation"></i> Không thể xóa Album. Vui lòng thử lại.
                    </div>
                </c:if>

                <!-- Page Header & Action -->
                <div class="page-heading-row" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
                    <div>
                        <h2 class="admin-page-title" style="margin: 0; font-size: 20px;">Lookbook Collections</h2>
                        <p style="margin: 4px 0 0; color: #64748b; font-size: 14px;">
                            Quản lý các bộ sưu tập phối đồ hiển thị trên Hero banner Trang chủ và chuyên trang Lookbook.
                        </p>
                    </div>
                    <a class="admin-btn admin-btn-primary" href="${pageContext.request.contextPath}/admin/albums?action=create" style="display: inline-flex; align-items: center; gap: 8px;">
                        <i class="fa-solid fa-plus"></i> Add Album
                    </a>
                </div>

                <!-- Table -->
                <div class="admin-card" style="padding: 0; overflow: hidden; background: #fff; border-radius: 8px; border: 1px solid #e2e8f0;">
                    <div class="table-responsive">
                        <table class="admin-table" style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr style="background: #f8fafc; border-bottom: 1px solid #e2e8f0; text-align: left;">
                                    <th style="padding: 14px 16px; width: 60px;">ID</th>
                                    <th style="padding: 14px 16px; width: 120px;">Cover</th>
                                    <th style="padding: 14px 16px;">Album Name</th>
                                    <th style="padding: 14px 16px;">Description</th>
                                    <th style="padding: 14px 16px; width: 120px; text-align: center;">Items in Set</th>
                                    <th style="padding: 14px 16px; width: 220px; text-align: right;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty albums}">
                                        <c:forEach var="item" items="${albums}">
                                            <tr style="border-bottom: 1px solid #f1f5f9; transition: background 0.15s;">
                                                <td style="padding: 14px 16px; font-weight: 600; color: #475569;">
                                                    #${item.albumId}
                                                </td>
                                                <td style="padding: 14px 16px;">
                                                    <c:choose>
                                                        <c:when test="${not empty item.imageUrl}">
                                                            <img src="${item.imageUrl}" alt="${item.name}" 
                                                                 style="width: 80px; height: 50px; object-fit: cover; border-radius: 6px; border: 1px solid #e2e8f0;">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div style="width: 80px; height: 50px; background: #e2e8f0; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: #94a3b8; font-size: 18px;">
                                                                <i class="fa-solid fa-image"></i>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="padding: 14px 16px;">
                                                    <div style="font-weight: 600; color: #0f172a; margin-bottom: 2px;">
                                                        ${item.name}
                                                    </div>
                                                </td>
                                                <td style="padding: 14px 16px; color: #64748b; font-size: 13px; max-width: 320px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                    ${item.description}
                                                </td>
                                                <td style="padding: 14px 16px; text-align: center;">
                                                    <span style="display: inline-block; padding: 3px 8px; background: #f1f5f9; color: #334155; border-radius: 9999px; font-size: 12px; font-weight: 600;">
                                                        ${not empty item.items ? item.items.size() : 0} items
                                                    </span>
                                                </td>
                                                <td style="padding: 14px 16px; text-align: right;">
                                                    <div style="display: inline-flex; align-items: center; gap: 8px;">
                                                        <!-- View on Storefront -->
                                                        <a href="${pageContext.request.contextPath}/AlbumServlet?id=${item.albumId}" 
                                                           target="_blank"
                                                           class="admin-btn admin-btn-secondary" 
                                                           title="View Lookbook"
                                                           style="padding: 6px 10px; font-size: 12px;">
                                                            <i class="fa-solid fa-arrow-up-right-from-square"></i>
                                                        </a>

                                                        <!-- Edit -->
                                                        <a href="${pageContext.request.contextPath}/admin/albums?action=edit&albumId=${item.albumId}" 
                                                           class="admin-btn admin-btn-secondary" 
                                                           title="Edit"
                                                           style="padding: 6px 10px; font-size: 12px;">
                                                            <i class="fa-solid fa-pen-to-square"></i>
                                                        </a>

                                                        <!-- Delete -->
                                                        <form method="post" action="${pageContext.request.contextPath}/admin/albums" 
                                                              style="display: inline; margin: 0;"
                                                              onsubmit="return confirm('Bạn có chắc chắn muốn xóa Album [${item.name}] không?');">
                                                            <input type="hidden" name="action" value="delete">
                                                            <input type="hidden" name="albumId" value="${item.albumId}">
                                                            <button type="submit" class="admin-btn admin-btn-danger" 
                                                                    title="Delete"
                                                                    style="padding: 6px 10px; font-size: 12px; background: #fee2e2; color: #dc2626; border: 1px solid #fecaca; cursor: pointer; border-radius: 4px;">
                                                                <i class="fa-solid fa-trash-can"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="6" style="padding: 48px 16px; text-align: center; color: #94a3b8;">
                                                <i class="fa-solid fa-layer-group" style="font-size: 32px; margin-bottom: 12px; display: block;"></i>
                                                Chưa có Album Lookbook nào. Hãy nhấn <strong>+ Add Album</strong> để tạo mới.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

            </section>
        </main>
    </div>
</body>
</html>
