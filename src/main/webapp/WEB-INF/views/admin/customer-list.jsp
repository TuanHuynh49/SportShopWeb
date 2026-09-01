<%-- customer-list.jsp — Quản lý danh sách khách hàng Admin (REQ-A05) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">👥 QUẢN LÝ KHÁCH HÀNG & THÀNH VIÊN</h1>
            <div class="text-xs text-muted">Danh sách tài khoản khách hàng, trạng thái hoạt động và quyền hạn</div>
        </div>
    </div>

    <!-- BẢNG KHÁCH HÀNG -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <c:when test="${not empty customers}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Họ Và Tên</th>
                                <th>Email</th>
                                <th>Số Điện Thoại</th>
                                <th>Vai Trò</th>
                                <th>Trạng Thái</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${customers}" var="u">
                                <tr>
                                    <td>
                                        <div class="d-flex align-items-center gap-2">
                                            <div class="avatar-circle" style="width: 32px; height: 32px; border-radius: 50%; background: #e2e8f0; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 0.8rem;">
                                                ${u.fullName.substring(0, 1).toUpperCase()}
                                            </div>
                                            <div class="fw-bold text-primary">${u.fullName}</div>
                                        </div>
                                    </td>
                                    <td>${u.email}</td>
                                    <td>${empty u.phone ? 'Chưa cập nhật' : u.phone}</td>
                                    <td><span class="badge bg-light text-dark border">${u.role.displayName}</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${u.status == 'ACTIVE'}"><span class="badge bg-success">Hoạt động</span></c:when>
                                            <c:otherwise><span class="badge bg-danger">Bị khóa</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <c:choose>
                                            <c:when test="${u.status == 'ACTIVE'}">
                                                <button class="btn btn-sm btn-outline-danger py-0 px-2">Khóa</button>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-sm btn-outline-success py-0 px-2">Mở Khóa</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">👥</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có khách hàng nào đăng ký</div>
                    <div class="text-xs">Khi có người dùng đăng ký tài khoản qua form Register, thông tin sẽ xuất hiện tại đây.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>