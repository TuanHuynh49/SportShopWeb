<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Customers | SportShop Admin</title>
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
                        <a href="${pageContext.request.contextPath}/admin/orders"><span
                                class="admin-nav-icon">⇄</span>Orders</a>
                        <a class="active" href="${pageContext.request.contextPath}/admin/customers"><span
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
                            <h1 class="admin-topbar-title">Customers</h1>
                            <p class="admin-topbar-subtitle">Workspace / Customer management</p>
                        </div>
                        <div class="admin-topbar-actions"><span class="admin-date-label">ADMIN</span></div>
                    </header>
                    <section class="admin-content">
                        <div class="page-heading-row">
                            <div>
                                <h2 class="admin-page-title">Customer Management</h2>
                                <p class="admin-page-description">Review customer accounts and contact information.</p>
                            </div>
                        </div>

                        <div class="admin-stats-grid">
                            <article class="admin-stat-card">
                                <div class="admin-stat-label">TOTAL CUSTOMERS</div>
                                <div class="admin-stat-value">
                                    <c:out value="${totalCustomers}" default="—" />
                                </div>
                                <div class="admin-stat-foot">Provided by Admin Controller</div>
                            </article>
                            <article class="admin-stat-card">
                                <div class="admin-stat-label">NEW CUSTOMERS</div>
                                <div class="admin-stat-value">
                                    <c:out value="${newCustomers}" default="—" />
                                </div>
                                <div class="admin-stat-foot">Provided by Admin Controller</div>
                            </article>
                            <article class="admin-stat-card">
                                <div class="admin-stat-label">WITH ORDERS</div>
                                <div class="admin-stat-value">
                                    <c:out value="${customersWithOrders}" default="—" />
                                </div>
                                <div class="admin-stat-foot">Provided by Admin Controller</div>
                            </article>
                            <article class="admin-stat-card">
                                <div class="admin-stat-label">WITHOUT ORDERS</div>
                                <div class="admin-stat-value">
                                    <c:out value="${customersWithoutOrders}" default="—" />
                                </div>
                                <div class="admin-stat-foot">Provided by Admin Controller</div>
                            </article>
                        </div>

                        <section class="admin-panel">
                            <div class="admin-panel-header">
                                <div>
                                    <h2 class="admin-panel-title">Customer List</h2>
                                    <p class="admin-panel-description">Account details from the current model.</p>
                                </div>
                            </div>
                            <form class="toolbar" action="${pageContext.request.contextPath}/admin/customers"
                                method="get">
                                <input type="search" name="keyword" placeholder="Search name, email or ID..."
                                    value="<c:out value='${param.keyword}'/>">
                                <button class="admin-button primary" type="submit">Search</button>
                                <a class="admin-button"
                                    href="${pageContext.request.contextPath}/admin/customers">Reset</a>
                            </form>

                            <div class="admin-table-wrap">
                                <table class="admin-table">
                                    <thead>
                                        <tr>
                                            <th>CUSTOMER</th>
                                            <th>ID</th>
                                            <th>EMAIL</th>
                                            <th>PHONE</th>
                                            <th>REGISTERED</th>
                                            <th>ACTION</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty customers}">
                                                <c:forEach items="${customers}" var="customer">
                                                    <tr>
                                                        <td>
                                                            <div class="customer-cell"><span
                                                                    class="customer-avatar">C</span>
                                                                <div>
                                                                    <div class="customer-name">
                                                                        <c:out value="${customer.firstName}"
                                                                            default="" />
                                                                        <c:out value="${customer.lastName}"
                                                                            default="" />
                                                                    </div>
                                                                    <div class="customer-subtitle">
                                                                        <c:out
                                                                            value="${customer.membershipTier.tierName}"
                                                                            default="Customer" />
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <c:out value="${customer.customerId}" />
                                                        </td>
                                                        <td>
                                                            <c:out value="${customer.user.email}" default="—" />
                                                        </td>
                                                        <td>
                                                            <c:out value="${customer.user.phone}" default="—" />
                                                        </td>
                                                        <td>
                                                            <c:out value="${customer.user.createdAt}" default="—" />
                                                        </td>
                                                        <td><a class="admin-text-link"
                                                                href="${pageContext.request.contextPath}/admin/customers?id=${customer.customerId}#customer-detail">View
                                                                →</a></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="6">
                                                        <div class="empty-message">No customers supplied. The Admin
                                                            Controller must provide <code>customers</code>.</div>
                                                    </td>
                                                </tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </section>

                        <c:if test="${not empty selectedCustomer}">
                            <section id="customer-detail" style="margin-top:24px">
                                <div class="page-heading-row">
                                    <div>
                                        <h2 class="admin-page-title">Customer Details</h2>
                                        <p class="admin-page-description">Account and order information.</p>
                                    </div><a class="admin-button"
                                        href="${pageContext.request.contextPath}/admin/customers">Close</a>
                                </div>
                                <div class="detail-grid">
                                    <section class="admin-panel">
                                        <div class="admin-panel-header">
                                            <h2 class="admin-panel-title">Account Information</h2>
                                        </div>
                                        <div class="detail-row"><span class="detail-label">Customer ID</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.customerId}" />
                                            </span></div>
                                        <div class="detail-row"><span class="detail-label">First name</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.firstName}" default="—" />
                                            </span></div>
                                        <div class="detail-row"><span class="detail-label">Last name</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.lastName}" default="—" />
                                            </span></div>
                                        <div class="detail-row"><span class="detail-label">Email</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.user.email}" default="—" />
                                            </span></div>
                                        <div class="detail-row"><span class="detail-label">Phone</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.user.phone}" default="—" />
                                            </span></div>
                                        <div class="detail-row"><span class="detail-label">Registered</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.user.createdAt}" default="—" />
                                            </span></div>
                                        <div class="detail-row"><span class="detail-label">Account status</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.user.status}" default="—" />
                                            </span></div>
                                    </section>
                                    <section class="admin-panel">
                                        <div class="admin-panel-header">
                                            <h2 class="admin-panel-title">Membership</h2>
                                        </div>
                                        <div class="detail-row"><span class="detail-label">Tier</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.membershipTier.tierName}"
                                                    default="—" />
                                            </span></div>
                                        <div class="detail-row"><span class="detail-label">Gender</span><span
                                                class="detail-value">
                                                <c:out value="${selectedCustomer.gender}" default="—" />
                                            </span></div>
                                        <p class="admin-panel-description">Order totals can be added when the Admin
                                            Controller supplies those values.</p>
                                    </section>
                                </div>

                                <section class="admin-panel" style="margin-top:20px">
                                    <div class="admin-panel-header">
                                        <h2 class="admin-panel-title">Order History</h2>
                                    </div>
                                    <div class="admin-table-wrap">
                                        <table class="admin-table">
                                            <thead>
                                                <tr>
                                                    <th>ORDER ID</th>
                                                    <th>ITEM LINES</th>
                                                    <th>ACTION</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${not empty customerOrders}">
                                                        <c:forEach items="${customerOrders}" var="order">
                                                            <tr>
                                                                <td>#
                                                                    <c:out value="${order.orderId}" />
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
                                                                        order →</a></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td colspan="3">
                                                                <div class="empty-message">No order history supplied for
                                                                    this customer.</div>
                                                            </td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>
                                    </div>
                                </section>
                            </section>
                        </c:if>
                    </section>
                </main>
            </div>
        </body>

        </html>