<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard | SportShop Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>

<body class="admin-body">
    <div class="admin-layout">
        <aside class="admin-sidebar">
            <div class="admin-brand">
                <span class="admin-brand-mark">S</span>
                <div>
                    <div class="admin-brand-name">SPORTSHOP</div>
                    <span class="admin-brand-caption">Administration</span>
                </div>
            </div>

            <div class="admin-nav-label">Workspace</div>

            <nav class="admin-nav">
                <a class="active" href="${pageContext.request.contextPath}/admin/dashboard">
                    <span class="admin-nav-icon">▦</span>Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/admin/products">
                    <span class="admin-nav-icon">▣</span>Products
                </a>
                <a href="${pageContext.request.contextPath}/admin/albums">
                    <span class="admin-nav-icon">☷</span>Albums
                </a>
                <a href="${pageContext.request.contextPath}/admin/orders">
                    <span class="admin-nav-icon">⇄</span>Orders
                </a>
                <a href="${pageContext.request.contextPath}/admin/customers">
                    <span class="admin-nav-icon">♙</span>Customers
                </a>
            </nav>

            <div class="admin-sidebar-bottom">
                <nav class="admin-nav">
                    <a href="${pageContext.request.contextPath}/HomeServlet">
                        <span class="admin-nav-icon">↗</span>Back to Store
                    </a>
                </nav>
            </div>
        </aside>

        <main class="admin-main">
            <header class="admin-topbar">
                <div>
                    <h1 class="admin-topbar-title">Dashboard</h1>
                    <p class="admin-topbar-subtitle">Workspace / Overview</p>
                </div>
                <div class="admin-topbar-actions">
                    <span class="admin-date-label">ADMIN</span>
                </div>
            </header>

            <section class="admin-content">
                <div class="page-heading-row">
                    <div>
                        <div class="admin-eyebrow">Store overview</div>
                        <h2 class="admin-page-title">Dashboard</h2>
                        <p class="admin-page-description">A summary of store activity.</p>
                    </div>
                </div>

                <!-- Dashboard statistics -->
                <div class="admin-stats-grid">
                    <article class="admin-stat-card">
                        <div class="admin-stat-top">
                            <span class="admin-stat-label">TODAY'S REVENUE</span>
                        </div>
                        <div class="admin-stat-value">
                            <c:choose>
                                <c:when test="${not empty todayRevenue}">
                                    <fmt:formatNumber value="${todayRevenue}" type="number" /> VND
                                </c:when>
                                <c:otherwise>0 VND</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="admin-stat-foot">Completed orders today</div>
                    </article>

                    <article class="admin-stat-card">
                        <div class="admin-stat-top">
                            <span class="admin-stat-label">MONTHLY REVENUE</span>
                        </div>
                        <div class="admin-stat-value">
                            <c:choose>
                                <c:when test="${not empty monthRevenue}">
                                    <fmt:formatNumber value="${monthRevenue}" type="number" /> VND
                                </c:when>
                                <c:otherwise>0 VND</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="admin-stat-foot">Completed orders this month</div>
                    </article>

                    <article class="admin-stat-card">
                        <div class="admin-stat-top">
                            <span class="admin-stat-label">NEW ORDERS</span>
                        </div>
                        <div class="admin-stat-value">
                            <c:out value="${newOrderCount}" default="0" />
                        </div>
                        <div class="admin-stat-foot">Orders placed today</div>
                    </article>

                    <article class="admin-stat-card">
                        <div class="admin-stat-top">
                            <span class="admin-stat-label">NEW CUSTOMERS</span>
                        </div>
                        <div class="admin-stat-value">
                            <c:out value="${newCustomerCount}" default="0" />
                        </div>
                        <div class="admin-stat-foot">New signups today</div>
                    </article>
                </div>

                <!-- Recent Orders -->
                <section class="admin-panel">
                    <div class="admin-panel-header">
                        <div>
                            <h2 class="admin-panel-title">Recent Orders</h2>
                            <p class="admin-panel-description">
                                Latest orders placed in the store.
                            </p>
                        </div>
                        <a class="admin-panel-link" href="${pageContext.request.contextPath}/admin/orders">
                            View all →
                        </a>
                    </div>

                    <div class="admin-table-wrap">
                        <table class="admin-table">
                            <thead>
                                <tr>
                                    <th>ORDER ID</th>
                                    <th>CUSTOMER</th>
                                    <th>ITEM LINES</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty recentOrders}">
                                        <c:forEach items="${recentOrders}" var="order">
                                            <tr>
                                                <td>
                                                    #<c:out value="${order.orderId}" />
                                                </td>
                                                <td>
                                                    <c:out value="${order.customer.firstName}" default="" />
                                                    <c:out value="${order.customer.lastName}" default="" />
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty order.orderItems}">
                                                            <c:out value="${order.orderItems.size()}" />
                                                        </c:when>
                                                        <c:otherwise>0</c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="3">
                                                <div class="empty-message">No recent orders found.</div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- Recent Products: Update and Delete actions -->
                <section class="admin-panel">
                    <div class="admin-panel-header">
                        <div>
                            <h2 class="admin-panel-title">Recent Products</h2>
                            <p class="admin-panel-description">
                                View and manage products directly from the dashboard.
                            </p>
                        </div>
                        <a class="admin-panel-link"
                            href="${pageContext.request.contextPath}/admin/products">
                            Manage all products →
                        </a>
                    </div>

                    <div class="admin-table-wrap">
                        <table class="admin-table">
                            <thead>
                                <tr>
                                    <th>PRODUCT ID</th>
                                    <th>PRODUCT</th>
                                    <th>BRAND</th>
                                    <th>CATEGORY</th>
                                    <th>ACTIONS</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty products}">
                                        <c:forEach items="${products}" var="product">
                                            <tr>
                                                <td>
                                                    <c:out value="${product.productId}" />
                                                </td>
                                                <td>
                                                    <c:out value="${product.productName}" />
                                                </td>
                                                <td>
                                                    <c:out value="${product.brand.name}" default="—" />
                                                </td>
                                                <td>
                                                    <c:out value="${product.category.name}" default="—" />
                                                </td>
                                                <td>
                                                    <div class="admin-product-actions">
                                                        <!-- Update product -->
                                                        <a class="admin-button"
                                                            href="${pageContext.request.contextPath}/ProductActionServlet?action=edit&amp;id=${product.productId}">
                                                            Update
                                                        </a>

                                                        <!-- Delete product using existing servlet -->
                                                        <form
                                                            action="${pageContext.request.contextPath}/admin/products"
                                                            method="post"
                                                            onsubmit="return confirm('Set all variants of this product quantity to zero?');">

                                                            <input type="hidden" name="action"
                                                                value="delete">

                                                            <input type="hidden" name="productId"
                                                                value="${product.productId}">

                                                            <button class="admin-button" type="submit">
                                                                Delete
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>

                                    <c:otherwise>
                                        <tr>
                                            <td colspan="5">
                                                <div class="empty-message">No products found.</div>
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
