<%-- variant-inventory.jsp — Quản lý tồn kho & biến thể sản phẩm (REQ-A03) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">📦 TỒN KHO & BIẾN THỂ SẢN PHẨM</h1>
            <div class="text-xs text-muted">Quản lý kích thước (Size), màu sắc (Color), tồn kho thực tế và số lượng đã đặt trước (Reserved)</div>
        </div>
        <button type="button" class="btn btn-sm btn-primary fw-bold">
            + Nhập Thêm Tồn Kho
        </button>
    </div>

    <!-- BẢNG TỒN KHO -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <c:when test="${not empty inventoryList}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Mã SKU</th>
                                <th>Sản Phẩm</th>
                                <th>Size / Màu</th>
                                <th>Tồn Thực Tế (Quantity)</th>
                                <th>Đã Giữ Chỗ (Reserved)</th>
                                <th>Khả Dụng</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${inventoryList}" var="i">
                                <tr>
                                    <td class="fw-bold"><code>${i.sku}</code></td>
                                    <td>${i.productName}</td>
                                    <td>${i.size} - ${i.color}</td>
                                    <td class="fw-bold">${i.quantity}</td>
                                    <td class="text-warning">${i.reservedQuantity}</td>
                                    <td class="fw-bold text-success">${i.quantity - i.reservedQuantity}</td>
                                    <td class="text-end">
                                        <button class="btn btn-sm btn-outline-primary py-0 px-2">Cập nhật kho</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">📦</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có dữ liệu tồn kho biến thể trong cơ sở dữ liệu</div>
                    <div class="text-xs">Theo chuẩn OR-Mapping: Bảng <code>inventory</code> liên kết 1-1 với <code>product_variant</code>. Khi tạo biến thể, số lượng tồn kho sẽ hiển thị tại đây.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>