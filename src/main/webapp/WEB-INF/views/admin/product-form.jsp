<%-- product-form.jsp — Form thêm/chỉnh sửa sản phẩm Admin (REQ-A02) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">${not empty product ? 'CHỈNH SỬA SẢN PHẨM' : 'THÊM SẢN PHẨM MỚI'}</h1>
            <div class="text-xs text-muted">Nhập thông tin chi tiết, hình ảnh và thiết lập giá bán</div>
        </div>
        <a href="${pageContext.request.contextPath}/admin/product" class="btn btn-sm btn-outline-secondary">
            ← Quay lại danh sách
        </a>
    </div>

    <form action="${pageContext.request.contextPath}/admin/product" method="post" class="row g-4">
        <div class="col-lg-8">
            <div class="bg-surface rounded-3 border p-4 mb-4">
                <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">Thông Tin Cơ Bản</h2>
                <div class="mb-3">
                    <label class="form-label text-sm fw-semibold">Tên sản phẩm *</label>
                    <input type="text" name="name" class="form-control form-control-sm" placeholder="Ví dụ: Giày Đá Bóng Nike Zoom Mercurial Vapor 15" required>
                </div>
                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Danh mục *</label>
                        <select name="categoryId" class="form-select form-select-sm" required>
                            <option value="">-- Chọn danh mục --</option>
                            <c:forEach items="${categories}" var="c">
                                <option value="${c.id}">${c.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Thương hiệu *</label>
                        <select name="brandId" class="form-select form-select-sm" required>
                            <option value="">-- Chọn thương hiệu --</option>
                            <option value="1">Nike</option>
                            <option value="2">Adidas</option>
                            <option value="3">Puma</option>
                            <option value="4">Mizuno</option>
                        </select>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label text-sm fw-semibold">Mô tả sản phẩm</label>
                    <textarea name="description" class="form-control form-control-sm" rows="5" placeholder="Mô tả công nghệ đệm, chất liệu da, mặt sân thi đấu phù hợp..."></textarea>
                </div>
            </div>

            <div class="bg-surface rounded-3 border p-4">
                <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">Giá Bán</h2>
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Giá niêm yết (VNĐ) *</label>
                        <input type="number" name="price" class="form-control form-control-sm" placeholder="Ví dụ: 2500000" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Giá khuyến mãi (VNĐ - Tùy chọn)</label>
                        <input type="number" name="salePrice" class="form-control form-control-sm" placeholder="Ví dụ: 1950000">
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="bg-surface rounded-3 border p-4 mb-4">
                <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">Hình Ảnh Đại Diện</h2>
                <div class="mb-3">
                    <label class="form-label text-sm fw-semibold">URL Hình Ảnh</label>
                    <input type="url" name="thumbnailUrl" class="form-control form-control-sm" placeholder="https://...">
                </div>
                <div class="p-3 bg-subtle rounded-3 text-center border text-xs text-muted">
                    Xem trước ảnh sản phẩm sẽ xuất hiện ở đây khi nhập URL hợp lệ.
                </div>
            </div>

            <div class="bg-surface rounded-3 border p-4">
                <button type="submit" class="btn btn-primary w-100 py-2 fw-bold mb-2">
                    LƯU SẢN PHẨM
                </button>
                <a href="${pageContext.request.contextPath}/admin/product" class="btn btn-outline-secondary w-100">
                    Hủy bỏ
                </a>
            </div>
        </div>
    </form>

</div>