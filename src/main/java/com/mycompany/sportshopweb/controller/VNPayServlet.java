package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.util.PaymentService;
import com.mycompany.sportshopweb.util.VNPayService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/vnpay-create")
public class VNPayServlet extends HttpServlet {
    

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Lấy orderId từ form
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
            if (session == null || session.getAttribute("customer") == null) {
                response.sendRedirect(request.getContextPath() + "/LoginServlet");
                return;
            }

            Customer customer = (Customer) session.getAttribute("customer");

            // 3. Nghiệp vụ: Kiểm tra đơn hàng & Khóa giữ chỗ tồn kho qua PaymentService
            Order order = paymentService.getOrderForPayment(orderId, customer);
            if (order == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Order not found or not eligible for payment.");
                return;
            }

            long amountVnd = order.getTotalAmount();

            // 4. Tạo mã giao dịch tham chiếu VNPay và lưu mapping đối soát
            String txnRef = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 20);
            session.setAttribute("vnpay_order_" + txnRef, orderId);
            String orderInfo = "Thanh toan don hang " + orderId;
            String ipAddress = request.getRemoteAddr();

            // 5. Tạo URL thanh toán VNPay có chữ ký số
            String paymentUrl = VNPayService.createPaymentUrl(
                    amountVnd,
                    txnRef,
                    orderInfo,
                    ipAddress);

            // 6. Chuyển trình duyệt sang cổng thanh toán VNPay Sandbox
            response.sendRedirect(paymentUrl);

        } catch (Exception e) {
            throw new ServletException(
                    "Unable to create VNPay payment URL.", e);
        }
    }
}
