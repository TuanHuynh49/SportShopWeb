<%-- voucher-form.jsp — Form tạo mới mã giảm giá Admin (REQ-A06) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">🎫 TẠO MÃ GIẢM GIÁ (VOUCHER) MỚI</h1>
            <div class="text-xs text-muted">Thiết lập mức giảm giá phần trăm hoặc số tiền cố định và thời hạn áp dụng</div>
        </div>
        <a href="${pageContext.request.contextPath}/admin/voucher" class="btn btn-sm btn-outline-secondary">
            ← Quay lại danh sách
        </a>
    </div>

    <form action="${pageContext.request.contextPath}/admin/voucher" method="post" class="row g-4">
        <div class="col-lg-8">
            <div class="bg-surface rounded-3 border p-4 mb-4">
                <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">Thông Tin Voucher</h2>
                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Mã Code Voucher *</label>
                        <input type="text" name="code" class="form-control form-control-sm text-uppercase" placeholder="Ví dụ: SALE50K, FOOTBALL2026" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Loại giảm giá *</label>
                        <select name="discountType" class="form-select form-select-sm" required>
                            <option value="PERCENT">Giảm theo phần trăm (%)</option>
                            <option value="FIXED">Giảm số tiền cố định (VNĐ)</option>
                        </select>
                    </div>
                </div>

                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Mức giảm *</label>
                        <input type="number" name="discountValue" class="form-control form-control-sm" placeholder="Ví dụ: 10 (%) hoặc 50000 (VNĐ)" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Đơn hàng tối thiểu (VNĐ)</label>
                        <input type="number" name="minOrderAmount" class="form-control form-control-sm" placeholder="Ví dụ: 300000">
                    </div>
                </div>

                <div class="row g-3 mb-3">
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Ngày bắt đầu *</label>
                        <input type="date" name="startDate" class="form-control form-control-sm" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label text-sm fw-semibold">Ngày kết thúc *</label>
                        <input type="date" name="endDate" class="form-control form-control-sm" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label text-sm fw-semibold">Giới hạn số lượt dùng</label>
                    <input type="number" name="usageLimit" class="form-control form-control-sm" value="100">
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="bg-surface rounded-3 border p-4">
                <button type="submit" class="btn btn-primary w-100 py-2 fw-bold mb-2">
                    LƯU VOUCHER
                </button>
                <a href="${pageContext.request.contextPath}/admin/voucher" class="btn btn-outline-secondary w-100">
                    Hủy bỏ
                </a>
            </div>
        </div>
    </form>

</div>