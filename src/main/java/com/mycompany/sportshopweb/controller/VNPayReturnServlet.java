package com.mycompany.sportshopweb.controller;

import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.PaymentMethod;
import com.mycompany.sportshopweb.util.PaymentService;
import com.mycompany.sportshopweb.util.VNPayService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String contextPath = request.getContextPath();

        try {
            // 1. Lấy toàn bộ tham số VNPay gửi về
            Map<String, String> params = new HashMap<>();

            request.getParameterMap().forEach((key, values) -> {
                if (values != null && values.length > 0) {
                    params.put(key, values[0]);
                }
            });

            String txnRef = params.get("vnp_TxnRef");
            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus = params.get("vnp_TransactionStatus");
            String transactionNo = params.get("vnp_TransactionNo");
            String amountText = params.get("vnp_Amount");

            // 2. Kiểm tra chữ ký bảo mật
            if (!VNPayService.isValidSignature(params)) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=invalid_signature");
                return;
            }

            // 3. Kiểm tra kết quả phản hồi từ VNPay
            boolean paymentSuccess = "00".equals(responseCode)
                    && "00".equals(transactionStatus);

            if (!paymentSuccess) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=payment_failed");
                return;
            }

            if (txnRef == null || txnRef.isBlank()
                    || transactionNo == null || transactionNo.isBlank()
                    || amountText == null) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=missing_data");
                return;
            }

            // 4. Lấy orderId đã lưu khi tạo yêu cầu thanh toán
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=session_expired");
                return;
            }

            String sessionKey = "vnpay_order_" + txnRef;
            Object savedOrderId = session.getAttribute(sessionKey);

            if (!(savedOrderId instanceof Integer)) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=order_not_found");
                return;
            }

            int orderId = (Integer) savedOrderId;

            // 5. Kiểm tra tài khoản khách hàng đăng nhập
            Object customerObject = session.getAttribute("customer");
            if (!(customerObject instanceof Customer)) {
                response.sendRedirect(contextPath + "/LoginServlet");
                return;
            }

            Customer customer = (Customer) customerObject;

            // 6. Xử lý nghiệp vụ thanh toán, trừ tồn kho và cập nhật COMPLETED qua
            // PaymentService
            String result = paymentService.processPaymentSuccess(
                    orderId,
                    customer,
                    amountText,
                    transactionNo,
                    PaymentMethod.VNPAY);

            if (!"SUCCESS".equals(result)) {
                response.sendRedirect(
                        contextPath + "/views/VNPayFail.jsp?reason=" + result);
                return;
            }

            // 7. Xóa mapping session sau khi xử lý thành công
            session.removeAttribute(sessionKey);

            // 8. Chuyển hướng tới trang thông báo thành công
            response.sendRedirect(
                    contextPath + "/views/VNPaySuccess.jsp");

        } catch (Exception e) {
            log("Error processing VNPay return.", e);
            throw new ServletException(
                    "Unable to process VNPay response.", e);
        }
    }
}
