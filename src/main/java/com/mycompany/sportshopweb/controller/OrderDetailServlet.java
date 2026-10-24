
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.dao.PaymentDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/order-detail")
public class OrderDetailServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập
        if (!isLoggedIn(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");

        // 2. Lấy orderId từ URL
        int orderId = readOrderId(request, response);

        if (orderId <= 0) {
            return;
        }

        try {
            // 3. Tìm đơn hàng thuộc khách hàng đang đăng nhập
            Order order = findCustomerOrder(
                    customer.getCustomerId(), orderId);

            if (order == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Không tìm thấy đơn hàng.");
                return;
            }

            // 4. Lấy thông tin thanh toán
            List<Payment> payments =
                    paymentDAO.findByOrderId(orderId);

            Payment payment = null;

            if (payments != null && !payments.isEmpty()) {
                payment = payments.get(0);
            }

            // 5. Gửi dữ liệu sang JSP
            request.setAttribute("order", order);
            request.setAttribute("payment", payment);

            request.getRequestDispatcher("/views/order-detail.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                    "Lỗi khi tải chi tiết đơn hàng.", e);
        }
    }

    private boolean isLoggedIn(HttpSession session) {
        return session != null
                && session.getAttribute("customer") != null;
    }

    private int readOrderId(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String orderIdParam = request.getParameter("orderId");

        if (orderIdParam == null || orderIdParam.isBlank()) {
            response.sendRedirect(
                    request.getContextPath() + "/orders");
            return -1;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);

            if (orderId <= 0) {
                response.sendRedirect(
                        request.getContextPath() + "/orders");
                return -1;
            }

            return orderId;

        } catch (NumberFormatException e) {
            response.sendRedirect(
                    request.getContextPath() + "/orders");
            return -1;
        }
    }

    private Order findCustomerOrder(int customerId, int orderId) {
        List<Order> orders = orderDAO.findByCustomerId(customerId);

        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }
}
