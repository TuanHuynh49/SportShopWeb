<%-- review-moderation.jsp — Kiểm duyệt đánh giá sản phẩm (REQ-A07) --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <div>
            <h1 class="h5 fw-bold text-primary mb-1">⭐ KIỂM DUYỆT ĐÁNH GIÁ (REVIEW)</h1>
            <div class="text-xs text-muted">Duyệt hoặc ẩn các bình luận, số sao đánh giá sản phẩm của khách hàng</div>
        </div>
    </div>

    <!-- BẢNG ĐÁNH GIÁ -->
    <div class="bg-surface rounded-3 border p-3">
        <c:choose>
            <c:when test="${not empty reviews}">
                <div class="table-responsive">
                    <table class="table align-middle mb-0 text-sm">
                        <thead class="table-light">
                            <tr>
                                <th>Sản Phẩm</th>
                                <th>Khách Hàng</th>
                                <th>Số Sao</th>
                                <th>Nội Dung Nhận Xét</th>
                                <th>Ngày Gửi</th>
                                <th class="text-end">Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${reviews}" var="r">
                                <tr>
                                    <td class="fw-bold">${r.productName}</td>
                                    <td>${r.customerName}</td>
                                    <td class="text-warning">
                                        <c:forEach begin="1" end="${r.rating}">★</c:forEach>
                                    </td>
                                    <td>${r.comment}</td>
                                    <td class="text-xs text-muted">${r.createdAt}</td>
                                    <td class="text-end">
                                        <button class="btn btn-sm btn-outline-danger py-0 px-2">Ẩn</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <div class="display-4 mb-2">⭐</div>
                    <div class="fw-bold text-sm text-primary mb-1">Chưa có đánh giá nào từ khách hàng</div>
                    <div class="text-xs">Khi khách hàng đã mua sản phẩm và gửi đánh giá (Rating/Comment), danh sách duyệt sẽ xuất hiện tại đây.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>