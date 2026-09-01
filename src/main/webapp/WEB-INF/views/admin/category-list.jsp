<%-- category-list.jsp — Quản lý danh mục thể thao Admin (REQ-A01) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">🗂 QUẢN LÝ DANH MỤC THỂ THAO</h1>
            <div class="text-xs text-muted">Hệ thống phân cấp danh mục (Giày, Áo, Quần, Bóng, Phụ kiện)</div>
        </div>
        <button type="button" class="btn btn-sm btn-primary fw-bold" data-bs-toggle="modal" data-bs-target="#modalAddCategory">
            + Thêm Danh Mục Mới
        </button>
    </div>

    <!-- TABS DANH MỤC / THƯƠNG HIỆU -->
    <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
            <a class="nav-link active fw-semibold" href="${pageContext.request.contextPath}/admin/category">Danh Mục Sản Phẩm</a>
        </li>
        <li class="nav-item">
            <a class="nav-link text-secondary" href="${pageContext.request.contextPath}/admin/brand">Thương Hiệu (Brands)</a>
        </li>
    </ul>

    <!-- BẢNG DANH MỤC -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <c:when test="${not empty categories}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Icon</th>
                                <th>Tên Danh Mục</th>
                                <th>Danh Mục Con (Subcategories)</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${categories}" var="cat">
                                <tr>
                                    <td style="font-size: 1.25rem;">${empty cat.icon ? '⚽' : cat.icon}</td>
                                    <td class="fw-bold text-primary">${cat.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty cat.subCategories}">
                                                <div class="d-flex flex-wrap gap-1">
                                                    <c:forEach items="${cat.subCategories}" var="sub">
                                                        <span class="badge bg-subtle text-primary border">${sub.name}</span>
                                                    </c:forEach>
                                                </div>
                                            </c:when>
                                            <c:otherwise><span class="text-muted text-xs">Không có</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="badge bg-success">Hoạt động</span></td>
                                    <td class="text-end">
                                        <button class="btn btn-sm btn-outline-primary py-0 px-2">Sửa</button>
                                        <button class="btn btn-sm btn-outline-danger py-0 px-2 ms-1">Xóa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">🗂</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có danh mục nào trong cơ sở dữ liệu</div>
                    <div class="text-xs mb-3">Tạo danh mục phân cấp để sắp xếp sản phẩm thuận tiện cho khách hàng.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>