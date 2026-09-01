<%-- product-list.jsp — Quản lý danh sách sản phẩm Admin (REQ-A02) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">👟 QUẢN LÝ SẢN PHẨM</h1>
            <div class="text-xs text-muted">Toàn bộ sản phẩm thể thao đang kinh doanh trong hệ thống</div>
        </div>
        <a href="${pageContext.request.contextPath}/admin/product/new" class="btn btn-sm btn-primary fw-bold">
            + Thêm Sản Phẩm Mới
        </a>
    </div>

    <!-- THANH TÌM KIẾM & BỘ LỌC -->
    <div class="bg-surface p-3 rounded-3 border mb-4">
        <form action="${pageContext.request.contextPath}/admin/product" method="get" class="row g-2 align-items-center">
            <div class="col-md-5">
                <input type="text" name="keyword" class="form-control form-control-sm"
                       placeholder="Tìm kiếm theo tên sản phẩm hoặc mã SKU..." value="${param.keyword}">
            </div>
            <div class="col-md-3">
                <select name="categoryId" class="form-select form-select-sm">
                    <option value="">Tất cả danh mục</option>
                    <c:forEach items="${categories}" var="c">
                        <option value="${c.id}">${c.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-sm btn-primary w-100">Tìm kiếm</button>
            </div>
            <div class="col-md-2">
                <a href="${pageContext.request.contextPath}/admin/product" class="btn btn-sm btn-outline-secondary w-100">Xóa lọc</a>
            </div>
        </form>
    </div>

    <!-- BẢNG DỮ LIỆU SẢN PHẨM -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <%-- KHI CÓ SẢN PHẨM TRONG DATABASE --%>
            <c:when test="${not empty products}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Ảnh</th>
                                <th>Tên Sản Phẩm</th>
                                <th>Danh Mục</th>
                                <th>Thương Hiệu</th>
                                <th>Giá Bán</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${products}" var="p">
                                <tr>
                                    <td style="width: 50px;">
                                        <img src="${empty p.thumbnailUrl ? pageContext.request.contextPath.concat('/assets/images/no-image.svg') : p.thumbnailUrl}"
                                             alt="${p.name}" style="width: 44px; height: 44px; object-fit: cover; border-radius: 6px; border: 1px solid #e2e8f0;">
                                    </td>
                                    <td>
                                        <div class="fw-bold text-primary">${p.name}</div>
                                        <div class="text-xs text-muted">ID: #${p.id}</div>
                                    </td>
                                    <td><span class="badge bg-light text-dark border">${p.categoryName}</span></td>
                                    <td>${p.brandName}</td>
                                    <td class="fw-bold text-primary">
                                        <fmt:formatNumber value="${p.price}" pattern="#,###"/>₫
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.active}"><span class="badge bg-success">Đang bán</span></c:when>
                                            <c:otherwise><span class="badge bg-secondary">Tạm ẩn</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/admin/product/edit?id=${p.id}" class="btn btn-sm btn-outline-primary py-0 px-2">Sửa</a>
                                        <button class="btn btn-sm btn-outline-danger py-0 px-2 ms-1">Ẩn</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>

            <%-- KHI CHƯA CÓ SẢN PHẨM TRONG DATABASE (EMPTY STATE) --%>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">👟</div>
                    <div class="fw-bold text-sm text-primary mb-1">Hiện chưa có sản phẩm nào trong cơ sở dữ liệu</div>
                    <div class="text-xs mb-3">Hãy tạo sản phẩm thể thao đầu tiên bằng cách nhấn vào nút bên dưới.</div>
                    <a href="${pageContext.request.contextPath}/admin/product/new" class="btn btn-sm btn-primary">
                        + Thêm Sản Phẩm Mới
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>