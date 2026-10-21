<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Orders | SportShop Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>

<body class="admin-body">
    <div class="admin-layout">
        <aside class="admin-sidebar">
            <div class="admin-brand"><span class="admin-brand-mark">S</span>
                <div>
                    <div class="admin-brand-name">SPORTSHOP</div><span
                        class="admin-brand-caption">Administration</span>
                </div>
            </div>
            <div class="admin-nav-label">Workspace</div>
            <nav class="admin-nav">
                <a href="${pageContext.request.contextPath}/admin/dashboard"><span
                        class="admin-nav-icon">▦</span>Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/products"><span
                        class="admin-nav-icon">▣</span>Products</a>
                <a href="${pageContext.request.contextPath}/admin/albums"><span
                        class="admin-nav-icon">☷</span>Albums</a>
                <a class="active" href="${pageContext.request.contextPath}/admin/orders"><span
                        class="admin-nav-icon">⇄</span>Orders</a>
                <a href="${pageContext.request.contextPath}/admin/customers"><span
                        class="admin-nav-icon">♙</span>Customers</a>
            </nav>
            <div class="admin-sidebar-bottom">
                <nav class="admin-nav"><a href="${pageContext.request.contextPath}/HomeServlet"><span
                            class="admin-nav-icon">↗</span>Back to Store</a></nav>
            </div>
        </aside>

        <main class="admin-main">
            <header class="admin-topbar">
                <div>
                    <h1 class="admin-topbar-title">Orders</h1>
                    <p class="admin-topbar-subtitle">Workspace / Order management</p>
                </div>
                <div class="admin-topbar-actions"><span class="admin-date-label">ADMIN</span></div>
            </header>
            <section class="admin-content">
                <div class="page-heading-row">
                    <div>
                        <h2 class="admin-page-title">Order Management</h2>
                        <p class="admin-page-description">Review orders and associated customers.</p>
                    </div>
                </div>

                <div class="admin-stats-grid">
                    <article class="admin-stat-card">
                        <div class="admin-stat-label">TOTAL ORDERS</div>
                        <div class="admin-stat-value">
                            <c:out value="${totalOrders}" default="0" />
                        </div>
                        <div class="admin-stat-foot">Provided by Admin Controller</div>
                    </article>
                    <article class="admin-stat-card">
                        <div class="admin-stat-label">PENDING ORDERS</div>
                        <div class="admin-stat-value">
                            <c:out value="${pendingOrders}" default="0" />
                        </div>
                        <div class="admin-stat-foot">Awaiting payment or processing</div>
                    </article>
                    <article class="admin-stat-card">
                        <div class="admin-stat-label">TOTAL REVENUE</div>
                        <div class="admin-stat-value">
                            <c:choose>
                                <c:when test="${not empty totalRevenue}">
                                    <fmt:formatNumber value="${totalRevenue}" type="number" /> VND
                                </c:when>
                                <c:otherwise>0 VND</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="admin-stat-foot">Completed orders revenue</div>
                    </article>
                </div>

                <section class="admin-panel">
                    <div class="admin-panel-header">
                        <div>
                            <h2 class="admin-panel-title">Order List</h2>
                            <p class="admin-panel-description">Order and customer details.</p>
                        </div>
                    </div>
                    <form class="toolbar" action="${pageContext.request.contextPath}/admin/orders" method="get">
                        <input type="search" name="keyword" placeholder="Search order ID or customer..."
                            value="<c:out value='${param.keyword}'/>">
                        <button class="admin-button primary" type="submit">Search</button>
                        <a class="admin-button" href="${pageContext.request.contextPath}/admin/orders">Reset</a>
                    </form>
                    <div class="admin-table-wrap">
                        <table class="admin-table">
                            <thead>
                                <tr>
                                    <th>ORDER ID</th>
                                    <th>CUSTOMER</th>
                                    <th>EMAIL</th>
                                    <th>STATUS</th>
                                    <th>TOTAL AMOUNT</th>
                                    <th>ITEM LINES</th>
                                    <th>ACTION</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty orders}">
                                        <c:forEach items="${orders}" var="order">
                                            <tr>
                                                <td><span class="admin-order-id">#
                                                        <c:out value="${order.orderId}" />
                                                    </span></td>
                                                <td>
                                                    <c:out value="${order.customer.firstName}" default="" />
                                                    <c:out value="${order.customer.lastName}" default="" />
                                                </td>
                                                <td>
                                                    <c:out value="${order.customer.user.email}" default="—" />
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty order.orderStatus}">
                                                            <span class="admin-status-badge status-${order.orderStatus.toString().toLowerCase()}">
                                                                <c:out value="${order.orderStatus}" />
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>—</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${order.totalAmount}" type="number" /> VND
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty order.orderItems}">
                                                            <c:out value="${order.orderItems.size()}" />
                                                        </c:when>
                                                        <c:otherwise>0</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td><a class="admin-text-link"
                                                        href="${pageContext.request.contextPath}/admin/order-detail?id=${order.orderId}">View
                                                        details →</a></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7">
                                                <div class="empty-message">No orders found.</div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </section>
            </section>
        </main>
    </div>
</body>

</html>
