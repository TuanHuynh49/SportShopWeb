
package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.util.VNPayConfig;
import com.mycompany.sportshopweb.util.VNPayService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/vnpay-create")
public class VNPayServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Lấy orderId từ form order-success.jsp
            String orderIdText = request.getParameter("orderId");

            if (orderIdText == null || orderIdText.isBlank()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Missing order ID.");
                return;
            }

            int orderId;

            try {
                orderId = Integer.parseInt(orderIdText);
            } catch (NumberFormatException e) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid order ID.");
                return;
            }

            // 2. Kiểm tra đăng nhập
            HttpSession session = request.getSession(false);

            if (session == null
                    || session.getAttribute("customer") == null) {
                response.sendRedirect(
                        request.getContextPath() + "/LoginServlet");
                return;
            }

            Customer customer = (Customer) session.getAttribute("customer");

            int customerId = customer.getCustomerId();

            // 3. Lấy danh sách đơn hàng của khách hàng
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders = orderDAO.findByCustomerId(customerId);

            // 4. Tìm đơn hàng cần thanh toán
            Order order = null;

            if (orders != null) {
                for (Order o : orders) {
                    if (o.getOrderId() == orderId) {
                        order = o;
                        break;
                    }
                }
            }

            if (order == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Order not found.");
                return;
            }

            // 5. Lấy số tiền từ database, không lấy từ form
            long amountVnd = order.getTotalAmount();

            if (amountVnd <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Amount must be positive.");
                return;
            }

            // 6. Tạo mã giao dịch VNPay
            String txnRef = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 20);
            session.setAttribute("vnpay_order_" + txnRef, orderId);
            String orderInfo = "Thanh toan don hang " + orderId;

            String ipAddress = request.getRemoteAddr();

            // 7. Log cấu hình để kiểm tra Return URL
            // String returnUrl = VNPayConfig.getReturnUrl();

            // System.out.println(
            // "VNPAY_TMN_CODE exists: "
            // + (System.getenv("VNPAY_TMN_CODE") != null
            // && !System.getenv("VNPAY_TMN_CODE").isBlank()));

            // System.out.println(
            // "VNPAY_RETURN_URL env = "
            // + System.getenv("VNPAY_RETURN_URL"));

            // System.out.println(
            // "VNPay returnUrl config = " + returnUrl);

            // System.out.println("VNPay amountVnd = " + amountVnd);
            // System.out.println("VNPay orderId = " + orderId);
            // System.out.println("VNPay txnRef = " + txnRef);
            // System.out.println("VNPay ipAddress = " + ipAddress);

            // 8. Tạo URL thanh toán
            String paymentUrl = VNPayService.createPaymentUrl(
                    amountVnd,
                    txnRef,
                    orderInfo,
                    ipAddress);

            // 9. Chuyển trình duyệt sang VNPay Sandbox
            response.sendRedirect(paymentUrl);

        } catch (Exception e) {
            throw new ServletException(
                    "Unable to create VNPay payment URL.", e);
        }
    }
}
