<%-- order-history.jsp — Lịch sử đơn hàng (MVC JSTL / EL) với xử lý Empty State khi chưa có đơn --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid p-0">

    <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
        <h1 class="h4 fw-bold mb-0 text-primary">📦 ĐƠN MUA CỦA TÔI</h1>
    </div>

    <c:choose>
        <%-- KHI CÓ ĐƠN HÀNG TRONG DATABASE --%>
        <c:when test="${not empty orderList}">
            <div class="table-responsive bg-surface rounded-3 border p-3">
                <table class="table align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>Mã đơn hàng</th>
                            <th>Ngày đặt</th>
                            <th>Tổng tiền</th>
                            <th>Phương thức</th>
                            <th>Trạng thái</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${orderList}" var="o">
                            <tr>
                                <td class="fw-bold">#${o.orderCode}</td>
                                <td class="text-sm">${o.createdAt}</td>
                                <td class="fw-bold text-sm text-primary"><fmt:formatNumber value="${o.totalAmount}" pattern="#,###"/>₫</td>
                                <td class="text-sm">${o.paymentMethod}</td>
                                <td><span class="badge badge-subtle">${o.orderStatus}</span></td>
                                <td><a href="#" class="btn btn-sm btn-outline-primary py-0">Chi tiết</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>

        <%-- KHI CHƯA CÓ ĐƠN HÀNG NÀO (EMPTY STATE) --%>
        <c:otherwise>
            <div class="empty-state-card text-center py-5 px-4 bg-surface rounded-3 border">
                <div class="display-3 text-muted mb-3">📦</div>
                <h2 class="h5 fw-bold text-primary mb-2">Bạn chưa có đơn hàng nào</h2>
                <p class="text-muted text-sm max-w-md mx-auto mb-4">
                    Toàn bộ lịch sử đặt hàng sẽ được lưu trữ và hiển thị tại đây khi bạn thực hiện mua sắm.
                </p>
                <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary px-4">
                    Bắt đầu mua sắm ngay
                </a>
            </div>
        </c:otherwise>
    </c:choose>

</div>