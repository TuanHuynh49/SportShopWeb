<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sổ địa chỉ - SPORTSHOP</title>
    <!-- Nhúng CSS dùng chung và CSS chuyên biệt cho Sổ địa chỉ -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/address.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>

<body>

    <!-- Nhúng Header dùng chung -->
    <jsp:include page="/views/common/header.jsp" />

    <!-- Phần nội dung chính của Trang Sổ địa chỉ -->
    <main class="address-page-wrapper">

        <!-- Thanh điều hướng liên kết -->
        <div class="address-nav-bar">
            <a href="${pageContext.request.contextPath}/ProfileServlet" class="btn-back-link">
                <i class="fa-solid fa-arrow-left"></i> Quay lại Hồ sơ cá nhân
            </a>
            <a href="${pageContext.request.contextPath}/checkout" class="btn-checkout-link">
                Tiếp tục thanh toán <i class="fa-solid fa-arrow-right"></i>
            </a>
        </div>

        <!-- Tiêu đề trang -->
        <div class="address-heading">
            <p class="address-eyebrow">TÀI KHOẢN CỦA BẠN</p>
            <h1 class="address-title">Sổ địa chỉ giao hàng</h1>
            <p class="address-subtitle">Quản lý và cập nhật địa chỉ để quá trình đặt hàng diễn ra thuận tiện và nhanh chóng.</p>
        </div>

        <!-- Bố cục lưới 2 cột: Cột trái (Danh sách địa chỉ) & Cột phải (Thêm địa chỉ mới) -->
        <div class="address-layout-grid">

            <!-- CỘT 1: DANH SÁCH ĐỊA CHỈ ĐÃ LƯU -->
            <div class="address-card-panel">
                <h2 class="address-card-title">
                    <i class="fa-solid fa-map-location-dot"></i> Địa chỉ đã lưu
                </h2>

                <c:choose>
                    <c:when test="${empty addresses}">
                        <div class="empty-address-box">
                            <i class="fa-solid fa-location-dot empty-address-icon"></i>
                            <p>Bạn chưa lưu địa chỉ nhận hàng nào.</p>
                            <p style="font-size: 0.85rem; margin-top: 4px;">Hãy thêm địa chỉ đầu tiên ở khung bên cạnh để thuận tiện mua sắm.</p>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <c:forEach var="address" items="${addresses}">
                            <div class="address-item-card ${address.defaultAddress ? 'is-default' : ''}">
                                
                                <div class="address-item-header">
                                    <div class="address-location-title">
                                        <i class="fa-solid fa-location-dot"></i>
                                        <span>${address.province}, ${address.ward}</span>
                                    </div>
                                    <c:if test="${address.defaultAddress}">
                                        <span class="address-badge-default">
                                            <i class="fa-solid fa-circle-check"></i> Mặc định
                                        </span>
                                    </c:if>
                                </div>

                                <p class="address-detail-text">${address.detailAddress}</p>

                                <!-- Checkbox ẩn để bật/tắt form sửa bằng CSS thuần -->
                                <input type="checkbox" id="toggle-edit-${address.addressId}" class="address-edit-toggle" hidden>

                                <!-- Các nút thao tác -->
                                <div class="address-actions">
                                    <label for="toggle-edit-${address.addressId}" class="btn-address-edit">
                                        <i class="fa-solid fa-pen-to-square"></i> Sửa địa chỉ
                                    </label>

                                    <c:if test="${not address.defaultAddress}">
                                        <form action="${pageContext.request.contextPath}/addresses"
                                              method="post"
                                              style="display:inline">
                                            <input type="hidden" name="action" value="setDefault">
                                            <input type="hidden" name="addressId" value="${address.addressId}">
                                            <button type="submit" class="btn-address-default">
                                                <i class="fa-solid fa-star"></i> Đặt làm mặc định
                                            </button>
                                        </form>
                                    </c:if>
                                                   class="address-form-input"
                                                   value="${address.province}"
                                                   required>
                                        </div>

                                        <div class="address-form-group">
                                            <label class="address-form-label">Phường / Xã</label>
                                            <input type="text"
                                                   name="ward"
                                                   class="address-form-input"
                                                   value="${address.ward}"
                                                   required>
                                        </div>

                                        <div class="address-form-group">
                                            <label class="address-form-label">Địa chỉ chi tiết</label>
                                            <input type="text"
                                                   name="detailAddress"
                                                   class="address-form-input"
                                                   value="${address.detailAddress}"
                                                   required>
                                        </div>

                                        <label class="address-checkbox-label">
                                            <input type="checkbox"
                                                   name="isDefault"
                                                   value="true"
                                                   <c:if test="${address.defaultAddress}">checked</c:if>>
                                            <span>Đặt làm địa chỉ mặc định</span>
                                        </label>

                                        <div style="margin-top: 18px;">
                                            <button type="submit" class="btn-address-default" style="padding: 10px 18px;">
                                                <i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi
                                            </button>
                                            <label for="toggle-edit-${address.addressId}" class="btn-address-cancel">
                                                Hủy
                                            </label>
                                        </div>
                                    </form>
                                </div>

                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- CỘT 2: FORM THÊM ĐỊA CHỈ MỚI -->
            <div class="address-card-panel">
                <h2 class="address-card-title">
                    <i class="fa-solid fa-plus-circle"></i> Thêm địa chỉ mới
                </h2>

                <form action="${pageContext.request.contextPath}/addresses" method="post">
                    <input type="hidden" name="action" value="insert">

                    <div class="address-form-group">
                        <label class="address-form-label">Tỉnh / Thành phố</label>
                        <input type="text"
                               name="province"
                               class="address-form-input"
                               placeholder="Ví dụ: TP. Hồ Chí Minh"
                               required>
                    </div>

                    <div class="address-form-group">
                        <label class="address-form-label">Phường / Xã</label>
                        <input type="text"
                               name="ward"
                               class="address-form-input"
                               placeholder="Nhập phường hoặc xã"
                               required>
                    </div>

                    <div class="address-form-group">
                        <label class="address-form-label">Địa chỉ chi tiết</label>
                        <input type="text"
                               name="detailAddress"
                               class="address-form-input"
                               placeholder="Số nhà, tên đường, tòa nhà..."
                               required>
                    </div>

                    <label class="address-checkbox-label">
                        <input type="checkbox" name="isDefault" value="true">
                        <span>Đặt làm địa chỉ mặc định</span>
                    </label>

                    <button type="submit" class="btn-address-submit">
                        <i class="fa-solid fa-plus"></i> Thêm địa chỉ mới
                    </button>
                </form>
            </div>

        </div>

    </main>

    <!-- Nhúng Footer dùng chung -->
    <jsp:include page="/views/common/footer.jsp" />


</body>
</html>
