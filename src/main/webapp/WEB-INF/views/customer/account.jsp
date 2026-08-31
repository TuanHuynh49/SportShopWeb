<%-- account.jsp — Quản lý tài khoản cá nhân (REQ-C10) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <h1 class="h4 fw-bold mb-0 text-primary">👤 HỒ SƠ TÀI KHOẢN</h1>
    </div>

    <div class="row g-4">
        <div class="col-lg-6">
            <div class="bg-surface rounded-3 border p-4">
                <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">THÔNG TIN CÁ NHÂN</h2>
                <form action="${pageContext.request.contextPath}/account/update" method="post">
                    <div class="mb-3">
                        <label class="form-label text-sm fw-semibold">Họ và tên</label>
                        <input type="text" name="fullName" class="form-control form-control-sm"
                               value="${sessionScope.authUser != null ? sessionScope.authUser.fullName : ''}" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-sm fw-semibold">Email đăng nhập</label>
                        <input type="email" class="form-control form-control-sm"
                               value="${sessionScope.authUser != null ? sessionScope.authUser.email : ''}" readonly disabled>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-sm fw-semibold">Số điện thoại</label>
                        <input type="tel" name="phone" class="form-control form-control-sm"
                               value="${sessionScope.authUser != null ? sessionScope.authUser.phone : ''}">
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm fw-bold">Cập Nhật Thông Tin</button>
                </form>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="bg-surface rounded-3 border p-4">
                <h2 class="h6 fw-bold text-primary mb-3 pb-2 border-bottom">ĐỔI MẬT KHẨU</h2>
                <form action="${pageContext.request.contextPath}/account/password" method="post">
                    <div class="mb-3">
                        <label class="form-label text-sm fw-semibold">Mật khẩu hiện tại</label>
                        <input type="password" name="currentPassword" class="form-control form-control-sm" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-sm fw-semibold">Mật khẩu mới</label>
                        <input type="password" name="newPassword" class="form-control form-control-sm" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label text-sm fw-semibold">Nhập lại mật khẩu mới</label>
                        <input type="password" name="confirmPassword" class="form-control form-control-sm" required>
                    </div>
                    <button type="submit" class="btn btn-outline-primary btn-sm fw-bold">Đổi Mật Khẩu</button>
                </form>
            </div>
        </div>
    </div>

</div>