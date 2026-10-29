<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="jakarta.tags.core" %>
        <%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>

            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Quản lý sản phẩm</title>

                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-products.css">
            </head>

            <body>
                <!-- Header dùng chung -->
                <jsp:include page="/views/common/header.jsp" />

                <main class="page-container">

                    <header class="page-header">
                        <div>
                            <h1>Quản lý sản phẩm</h1>
                            <p class="subtitle">Quản lý sản phẩm, giá bán và tồn kho.</p>
                        </div>

                        <div class="page-header-actions">
                            <a class="button" href="${pageContext.request.contextPath}/admin/albums">
                                Quản lý Albums
                            </a>
                            <a class="button button-primary"
                                href="${pageContext.request.contextPath}/admin/product-actions?action=create">
                                + Thêm sản phẩm
                            </a>
                        </div>
                    </header>

                    <!-- Thông báo sản phẩm -->
                    <c:if test="${param.createSuccess == '1'}">
                        <div class="message success">Thêm sản phẩm và biến thể thành công.</div>
                    </c:if>

                    <c:if test="${param.updateSuccess == '1'}">
                        <div class="message success">Cập nhật sản phẩm thành công.</div>
                    </c:if>

                    <c:if test="${param.deleteSuccess == '1'}">
                        <div class="message success">
                            Đã xử lý yêu cầu xóa sản phẩm. Sản phẩm có thể vẫn xuất hiện trong danh sách
                            vì chức năng hiện tại đặt tồn kho các biến thể về 0.
                        </div>
                    </c:if>

                    <c:if test="${param.deleteError == '1'}">
                        <div class="message error">Không thể xử lý yêu cầu xóa sản phẩm.</div>
                    </c:if>

                    <!-- Thông báo biến thể -->
                    <c:if test="${param.variantMessage == 'created'}">
                        <div class="message success">Thêm biến thể thành công.</div>
                    </c:if>

                    <c:if test="${param.variantMessage == 'updated'}">
                        <div class="message success">Cập nhật biến thể thành công.</div>
                    </c:if>

                    <c:if test="${param.variantMessage == 'deleted'}">
                        <div class="message success">Xóa biến thể thành công.</div>
                    </c:if>

                    <c:if test="${param.variantMessage == 'invalid'}">
                        <div class="message error">Dữ liệu biến thể không hợp lệ. Vui lòng kiểm tra giá và số lượng.
                        </div>
                    </c:if>

                    <c:if test="${param.variantMessage == 'failed'}">
                        <div class="message error">
                            Thao tác biến thể thất bại. Biến thể có thể không tồn tại hoặc không thuộc sản phẩm đã chọn.
                        </div>
                    </c:if>

                    <c:if test="${param.variantMessage == 'invalidProduct'}">
                        <div class="message error">Sản phẩm không hợp lệ.</div>
                    </c:if>

                    <c:if test="${param.variantMessage == 'invalidAction'}">
                        <div class="message error">Thao tác biến thể không hợp lệ.</div>
                    </c:if>

                    <c:if test="${param.variantMessage == 'error'}">
                        <div class="message error">Đã xảy ra lỗi khi xử lý biến thể.</div>
                    </c:if>

                    <!-- Bộ lọc -->
                    <section class="filter-card">
                        <h2 class="section-title">Tìm kiếm và lọc</h2>

                        <form class="filter-form" action="${pageContext.request.contextPath}/admin/products"
                            method="get">

                            <div class="form-group">
                                <label for="keyword">Tên sản phẩm</label>
                                <input type="text" id="keyword" name="keyword" value="<c:out value='${keyword}'/>"
                                    placeholder="Nhập tên sản phẩm">
                            </div>

                            <div class="form-group">
                                <label for="brandId">Thương hiệu</label>
                                <select id="brandId" name="brandId">
                                    <option value="0">Tất cả thương hiệu</option>

                                    <c:forEach var="brand" items="${brands}">
                                        <option value="${brand.brandId}" <c:if
                                            test="${selectedBrandId == brand.brandId}">selected</c:if>>
                                            <c:out value="${brand.name}" />
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="categoryId">Danh mục</label>
                                <select id="categoryId" name="categoryId">
                                    <option value="0">Tất cả danh mục</option>

                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}" <c:if
                                            test="${selectedCategoryId == category.categoryId}">selected</c:if>>
                                            <c:out value="${category.name}" />
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="filter-actions">
                                <button type="submit" class="button button-primary">Lọc</button>
                                <a class="button" href="${pageContext.request.contextPath}/admin/products">
                                    Xóa bộ lọc
                                </a>
                            </div>
                        </form>
                    </section>

                    <!-- Danh sách sản phẩm -->
                    <section class="table-card">
                        <div class="table-heading">
                            <div>
                                <h2>Danh sách sản phẩm</h2>
                                <p class="subtitle">
                                    Tổng số kết quả: <strong>${totalProducts}</strong>
                                </p>
                            </div>
                        </div>

                        <div class="table-responsive">
                            <table class="product-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Ảnh</th>
                                        <th>Tên sản phẩm</th>
                                        <th>Thương hiệu</th>
                                        <th>Danh mục</th>
                                        <th>Mô tả</th>
                                        <th>Biến thể</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty products}">
                                            <tr>
                                                <td colspan="8" class="empty-state">
                                                    Không tìm thấy sản phẩm.
                                                </td>
                                            </tr>
                                        </c:when>

                                        <c:otherwise>
                                            <c:forEach var="product" items="${products}">
                                                <tr>
                                                    <td>
                                                        <c:out value="${product.productId}" />
                                                    </td>

                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty product.images and not empty product.images[0].imageUrl}">
                                                                <img src="${product.images[0].imageUrl}" alt="<c:out value='${product.productName}'/>"
                                                                     class="product-thumb">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="product-no-thumb">
                                                                    No img
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>

                                                    <td class="product-name">
                                                        <c:out value="${product.productName}" />
                                                    </td>

                                                    <td>
                                                        <c:out value="${product.brand.name}" />
                                                    </td>

                                                    <td>
                                                        <c:out value="${product.category.name}" />
                                                    </td>

                                                    <td class="description-cell">
                                                        <c:out value="${product.description}" />
                                                    </td>

                                                    <td>
                                                        <a class="button button-small"
                                                            href="${pageContext.request.contextPath}/admin/products?manageVariants=${product.productId}&amp;keyword=${keyword}&amp;brandId=${selectedBrandId}&amp;categoryId=${selectedCategoryId}&amp;page=${currentPage}">
                                                            Quản lý biến thể
                                                        </a>
                                                    </td>

                                                    <td>
                                                        <div class="actions">
                                                            <a class="button button-small"
                                                                href="${pageContext.request.contextPath}/admin/product-actions?action=edit&amp;productId=${product.productId}">
                                                                Sửa
                                                            </a>

                                                            <form class="inline-form"
                                                                action="${pageContext.request.contextPath}/admin/products"
                                                                method="post"
                                                                onsubmit="return confirm('Bạn có chắc muốn xử lý xóa sản phẩm này?');">

                                                                <input type="hidden" name="action" value="delete">

                                                                <input type="hidden" name="productId"
                                                                    value="${product.productId}">

                                                                <button type="submit"
                                                                    class="button button-danger button-small">
                                                                    Xóa
                                                                </button>
                                                            </form>
                                                        </div>
                                                    </td>
                                                </tr>

                                                <!-- Phần quản lý biến thể -->
                                                <c:if test="${manageVariants == product.productId}">
                                                    <tr>
                                                        <td colspan="8">
                                                            <div class="variant-panel">
                                                                <h3>
                                                                    Quản lý biến thể:
                                                                    <c:out value="${product.productName}" />
                                                                </h3>

                                                                <h4>Các biến thể hiện có</h4>

                                                                <div class="table-responsive">
                                                                    <table class="variant-table">
                                                                        <thead>
                                                                            <tr>
                                                                                <th>ID</th>
                                                                                <th>Tên biến thể</th>
                                                                                <th>Size</th>
                                                                                <th>Màu</th>
                                                                                <th>Giá</th>
                                                                                <th>Tồn kho</th>
                                                                                <th>Thao tác</th>
                                                                            </tr>
                                                                        </thead>

                                                                        <tbody>
                                                                            <c:choose>
                                                                                <c:when test="${empty managedVariants}">
                                                                                    <tr>
                                                                                        <td colspan="7"
                                                                                            class="empty-state">
                                                                                            Sản phẩm chưa có biến thể.
                                                                                        </td>
                                                                                    </tr>
                                                                                </c:when>

                                                                                <c:otherwise>
                                                                                    <c:forEach var="variant"
                                                                                        items="${managedVariants}">
                                                                                        <tr>
                                                                                            <td>
                                                                                                <c:out
                                                                                                    value="${variant.variantId}" />
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:out
                                                                                                    value="${variant.variantName}" />
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:out
                                                                                                    value="${variant.size}" />
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:out
                                                                                                    value="${variant.color}" />
                                                                                            </td>
                                                                                            <td>
                                                                                                <fmt:formatNumber
                                                                                                    value="${variant.price}"
                                                                                                    groupingUsed="true" />
                                                                                                đ
                                                                                            </td>
                                                                                            <td>
                                                                                                <c:out
                                                                                                    value="${variant.quantity}" />
                                                                                            </td>

                                                                                            <td>
                                                                                                <details>
                                                                                                    <summary
                                                                                                        class="edit-summary">
                                                                                                        Sửa</summary>

                                                                                                    <form
                                                                                                        class="variant-form"
                                                                                                        action="${pageContext.request.contextPath}/admin/product-variants"
                                                                                                        method="post">

                                                                                                        <input
                                                                                                            type="hidden"
                                                                                                            name="action"
                                                                                                            value="update">
                                                                                                        <input
                                                                                                            type="hidden"
                                                                                                            name="productId"
                                                                                                            value="${product.productId}">
                                                                                                        <input
                                                                                                            type="hidden"
                                                                                                            name="variantId"
                                                                                                            value="${variant.variantId}">

                                                                                                        <div
                                                                                                            class="form-group">
                                                                                                            <label>Tên
                                                                                                                biến
                                                                                                                thể</label>
                                                                                                            <input
                                                                                                                type="text"
                                                                                                                name="variantName"
                                                                                                                value="<c:out value='${variant.variantName}'/>"
                                                                                                                required>
                                                                                                        </div>

                                                                                                        <div
                                                                                                            class="form-group">
                                                                                                            <label>Size</label>
                                                                                                            <input
                                                                                                                type="text"
                                                                                                                name="size"
                                                                                                                value="<c:out value='${variant.size}'/>">
                                                                                                        </div>

                                                                                                        <div
                                                                                                            class="form-group">
                                                                                                            <label>Màu</label>
                                                                                                            <input
                                                                                                                type="text"
                                                                                                                name="color"
                                                                                                                value="<c:out value='${variant.color}'/>">
                                                                                                        </div>

                                                                                                        <div
                                                                                                            class="form-group">
                                                                                                            <label>Giá</label>
                                                                                                            <input
                                                                                                                type="number"
                                                                                                                name="price"
                                                                                                                min="0"
                                                                                                                step="1"
                                                                                                                value="${variant.price}"
                                                                                                                required>
                                                                                                        </div>

                                                                                                        <div
                                                                                                            class="form-group">
                                                                                                            <label>Tồn
                                                                                                                kho</label>
                                                                                                            <input
                                                                                                                type="number"
                                                                                                                name="quantity"
                                                                                                                min="0"
                                                                                                                step="1"
                                                                                                                value="${variant.quantity}"
                                                                                                                required>
                                                                                                        </div>

                                                                                                        <button
                                                                                                            type="submit"
                                                                                                            class="button button-primary">
                                                                                                            Lưu thay đổi
                                                                                                        </button>
                                                                                                    </form>
                                                                                                </details>

                                                                                                <form
                                                                                                    class="inline-form"
                                                                                                    action="${pageContext.request.contextPath}/admin/product-variants"
                                                                                                    method="post"
                                                                                                    onsubmit="return confirm('Bạn có chắc muốn xóa biến thể này?');">

                                                                                                    <input type="hidden"
                                                                                                        name="action"
                                                                                                        value="delete">
                                                                                                    <input type="hidden"
                                                                                                        name="productId"
                                                                                                        value="${product.productId}">
                                                                                                    <input type="hidden"
                                                                                                        name="variantId"
                                                                                                        value="${variant.variantId}">

                                                                                                    <button
                                                                                                        type="submit"
                                                                                                        class="button button-danger button-small">
                                                                                                        Xóa
                                                                                                    </button>
                                                                                                </form>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </c:forEach>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </tbody>
                                                                    </table>
                                                                </div>

                                                                <h4>Thêm biến thể mới</h4>

                                                                <form class="variant-form"
                                                                    action="${pageContext.request.contextPath}/admin/product-variants"
                                                                    method="post">

                                                                    <input type="hidden" name="action" value="create">
                                                                    <input type="hidden" name="productId"
                                                                        value="${product.productId}">

                                                                    <div class="form-group">
                                                                        <label>Tên biến thể</label>
                                                                        <input type="text" name="variantName"
                                                                            placeholder="Ví dụ: Áo size M" required>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label>Size</label>
                                                                        <input type="text" name="size"
                                                                            placeholder="S, M, L...">
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label>Màu</label>
                                                                        <input type="text" name="color"
                                                                            placeholder="Đen, trắng...">
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label>Giá (VND)</label>
                                                                        <input type="number" name="price" min="0"
                                                                            step="1" required>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label>Tồn kho</label>
                                                                        <input type="number" name="quantity" min="0"
                                                                            step="1" value="0" required>
                                                                    </div>

                                                                    <button type="submit" class="button button-primary">
                                                                        Thêm biến thể
                                                                    </button>
                                                                </form>

                                                                <div class="panel-footer">
                                                                    <a class="button"
                                                                        href="${pageContext.request.contextPath}/admin/products?keyword=${keyword}&amp;brandId=${selectedBrandId}&amp;categoryId=${selectedCategoryId}&amp;page=${currentPage}">
                                                                        Đóng phần quản lý biến thể
                                                                    </a>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- Phân trang -->
                        <nav class="pagination" aria-label="Phân trang">
                            <c:if test="${currentPage > 1}">
                                <a class="button"
                                    href="${pageContext.request.contextPath}/admin/products?keyword=${keyword}&amp;brandId=${selectedBrandId}&amp;categoryId=${selectedCategoryId}&amp;page=${currentPage - 1}">
                                    ← Trang trước
                                </a>
                            </c:if>

                            <span class="page-info">
                                Trang <strong>${currentPage}</strong> / ${totalPages}
                            </span>

                            <c:if test="${currentPage < totalPages}">
                                <a class="button"
                                    href="${pageContext.request.contextPath}/admin/products?keyword=${keyword}&amp;brandId=${selectedBrandId}&amp;categoryId=${selectedCategoryId}&amp;page=${currentPage + 1}">
                                    Trang sau →
                                </a>
                            </c:if>
                        </nav>
                    </section>

                </main>

                <!-- Footer dùng chung -->
                <jsp:include page="/views/common/footer.jsp" />
            </body>

            </html>