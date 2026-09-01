<%-- brand-list.jsp — Quản lý thương hiệu thể thao Admin (REQ-A01) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">🏷️ QUẢN LÝ THƯƠNG HIỆU THỂ THAO</h1>
            <div class="text-xs text-muted">Các thương hiệu chính hãng: Nike, Adidas, Puma, Mizuno, v.v.</div>
        </div>
        <button type="button" class="btn btn-sm btn-primary fw-bold">
            + Thêm Thương Hiệu Mới
        </button>
    </div>

    <!-- TABS DANH MỤC / THƯƠNG HIỆU -->
    <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
            <a class="nav-link text-secondary" href="${pageContext.request.contextPath}/admin/category">Danh Mục Sản Phẩm</a>
        </li>
        <li class="nav-item">
            <a class="nav-link active fw-semibold" href="${pageContext.request.contextPath}/admin/brand">Thương Hiệu (Brands)</a>
        </li>
    </ul>

    <!-- BẢNG THƯƠNG HIỆU -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <c:when test="${not empty brands}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Logo</th>
                                <th>Tên Thương Hiệu</th>
                                <th>Mô Tả</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${brands}" var="b">
                                <tr>
                                    <td><img src="${b.logoUrl}" alt="${b.name}" style="height: 30px;"></td>
                                    <td class="fw-bold">${b.name}</td>
                                    <td>${b.description}</td>
                                    <td><span class="badge bg-success">Hoạt động</span></td>
                                    <td class="text-end">
                                        <button class="btn btn-sm btn-outline-primary py-0 px-2">Sửa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">🏷️</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có thương hiệu nào trong cơ sở dữ liệu</div>
                    <div class="text-xs mb-3">Thêm thương hiệu để gán cho các mặt hàng thể thao.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>