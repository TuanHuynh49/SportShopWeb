package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.AdminOrderDAO;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrderServlet", urlPatterns = { "/admin/orders", "/admin/order-detail" })
public class AdminOrderServlet extends HttpServlet {

    private AdminOrderDAO adminOrderDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        adminOrderDAO = new AdminOrderDAO();
    }

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

        List<Order> orders = adminOrderDAO.getAllOrdersWithDetails();

        // Tìm theo mã đơn hàng hoặc tên khách hàng / email.
        if (keyword != null && !keyword.isEmpty()) {
            final String search = keyword.toLowerCase();

            orders = orders.stream()
                    .filter(order -> {
                        String orderId = String.valueOf(order.getOrderId());

                        String firstName = order.getCustomer() != null ? order.getCustomer().getFirstName() : "";
                        String lastName = order.getCustomer() != null ? order.getCustomer().getLastName() : "";

                        String fullName = ((firstName == null ? "" : firstName)
                                + " "
                                + (lastName == null ? "" : lastName))
                                .toLowerCase();

                        String email = (order.getCustomer() != null && order.getCustomer().getUser() != null)
                                ? order.getCustomer().getUser().getEmail()
                                : "";

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

        Order order = adminOrderDAO.getOrderDetailById(orderId);

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
