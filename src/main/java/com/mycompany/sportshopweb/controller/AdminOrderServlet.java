package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrderServlet", urlPatterns = { "/admin/orders", "/admin/order-detail" })
public class AdminOrderServlet extends HttpServlet {

    // Xử lý GET cho cả trang danh sách và trang chi tiết đơn hàng.
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        if ("/admin/order-detail".equals(servletPath)) {
            showOrderDetail(request, response);
        } else {
            showOrders(request, response);
        }
    }

    // Hiển thị danh sách đơn hàng.
    private void showOrders(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        if (keyword != null) {
            keyword = keyword.trim();
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try {
            String jpql = "SELECT DISTINCT o FROM Order o " +
                    "JOIN FETCH o.customer c " +
                    "JOIN FETCH c.user " +
                    "LEFT JOIN FETCH o.orderItems " +
                    "ORDER BY o.orderId DESC";

            TypedQuery<Order> query = em.createQuery(jpql, Order.class);

            List<Order> orders = query.getResultList();

            // Tìm theo mã đơn hàng hoặc tên khách hàng.
            // Hiện thực hiện lọc trên danh sách đã truy vấn.
            if (keyword != null && !keyword.isEmpty()) {
                final String search = keyword.toLowerCase();

                orders = orders.stream()
                        .filter(order -> {
                            String orderId = String.valueOf(order.getOrderId());

                            String firstName = order.getCustomer().getFirstName();

                            String lastName = order.getCustomer().getLastName();

                            String fullName = ((firstName == null ? "" : firstName)
                                    + " "
                                    + (lastName == null ? "" : lastName))
                                    .toLowerCase();

                            String email = order.getCustomer().getUser().getEmail();

                            email = email == null
                                    ? ""
                                    : email.toLowerCase();

                            return orderId.contains(search)
                                    || fullName.contains(search)
                                    || email.contains(search);
                        })
                        .toList();
            }

            // Tính số đơn chờ xử lý (PENDING hoặc CONFIRMED)
            long pendingOrders = orders.stream()
                    .filter(o -> o.getOrderStatus() == OrderStatus.PENDING
                            || o.getOrderStatus() == OrderStatus.CONFIRMED)
                    .count();

            // Tính tổng doanh thu từ các đơn hàng thành công (COMPLETED)
            long totalRevenue = orders.stream()
                    .filter(o -> o.getOrderStatus() == OrderStatus.COMPLETED)
                    .mapToLong(Order::getTotalAmount)
                    .sum();

            request.setAttribute("orders", orders);
            request.setAttribute("totalOrders", orders.size());
            request.setAttribute("pendingOrders", pendingOrders);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("keyword", keyword);

        } finally {
            em.close();
        }

        request.getRequestDispatcher(
                "/views/admin/orders.jsp").forward(request, response);
    }

    // Hiển thị chi tiết một đơn hàng.
    private void showOrderDetail(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Missing order ID.");
            return;
        }

        int orderId;

        try {
            orderId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid order ID.");
            return;
        }

        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        Order order;

        try {
            List<Order> results = em.createQuery(
                    "SELECT DISTINCT o FROM Order o " +
                            "JOIN FETCH o.customer c " +
                            "JOIN FETCH c.user " +
                            "LEFT JOIN FETCH o.orderItems oi " +
                            "LEFT JOIN FETCH oi.productVariant pv " +
                            "LEFT JOIN FETCH pv.product " +
                            "WHERE o.orderId = :orderId",
                    Order.class)
                    .setParameter("orderId", orderId)
                    .getResultList();

            order = results.isEmpty() ? null : results.get(0);

        } finally {
            em.close();
        }

        if (order == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Order not found.");
            return;
        }

        request.setAttribute("order", order);

        request.getRequestDispatcher(
                "/views/admin/order-detail.jsp").forward(request, response);
    }
}
