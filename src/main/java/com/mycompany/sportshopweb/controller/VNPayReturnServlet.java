
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

    private static final String FAIL_PAGE = "/views/VNPayFail.jsp";
    private static final String SUCCESS_PAGE = "/views/VNPaySuccess.jsp";
    private static final String SESSION_CUSTOMER = "customer";

    private PaymentService paymentService;

    // =========================================================
    // INIT
    // =========================================================
    @Override
    public void init() throws ServletException {
        paymentService = new PaymentService();
    }

    // =========================================================
    // GET: NHẬN KẾT QUẢ TRẢ VỀ TỪ VNPAY
    // =========================================================
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getContextPath();

        try {
            // 1. Lấy tham số VNPay.
            Map<String, String> params = getRequestParameters(request);

            String txnRef = params.get("vnp_TxnRef");
            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus = params.get("vnp_TransactionStatus");
            String transactionNo = params.get("vnp_TransactionNo");
            String amountText = params.get("vnp_Amount");

            // 2. Xác minh chữ ký trước khi tin tưởng dữ liệu.
            if (!VNPayService.isValidSignature(params)) {
                redirectToFail(response, contextPath, "invalid_signature");
                return;
            }

            // 3. Kiểm tra dữ liệu bắt buộc.
            if (isBlank(txnRef)
                    || isBlank(transactionNo)
                    || isBlank(amountText)
                    || isBlank(responseCode)
                    || isBlank(transactionStatus)) {

                redirectToFail(response, contextPath, "missing_data");
                return;
            }

            // 4. Chỉ xử lý khi VNPay báo thanh toán thành công.
            if (!"00".equals(responseCode)
                    || !"00".equals(transactionStatus)) {

                redirectToFail(response, contextPath, "payment_failed");
                return;
            }

            // 5. Lấy mapping orderId từ session.
            HttpSession session = request.getSession(false);

            if (session == null) {
                redirectToFail(response, contextPath, "session_expired");
                return;
            }

            String sessionKey = "vnpay_order_" + txnRef;
            Object savedOrderId = session.getAttribute(sessionKey);

            if (!(savedOrderId instanceof Integer)) {
                redirectToFail(response, contextPath, "order_not_found");
                return;
            }

            int orderId = (Integer) savedOrderId;

            // 6. Kiểm tra khách hàng đăng nhập.
            Object customerObject = session.getAttribute(SESSION_CUSTOMER);

            if (!(customerObject instanceof Customer)) {
                response.sendRedirect(contextPath + "/LoginServlet");
                return;
            }

            Customer customer = (Customer) customerObject;

            // 7. Xác minh và xử lý thanh toán trong service.
            String result = paymentService.processPaymentSuccess(
                    orderId,
                    customer,
                    amountText,
                    transactionNo,
                    PaymentMethod.VNPAY);

            if (!"SUCCESS".equals(result)) {
                redirectToFail(response, contextPath, result);
                return;
            }

            // 8. Chỉ xóa mapping sau khi service xử lý thành công.
            session.removeAttribute(sessionKey);

            // 9. Chuyển tới trang thành công.
            response.sendRedirect(contextPath + SUCCESS_PAGE);

        } catch (Exception e) {
            log("Error processing VNPay return.", e);

            // Không để thông báo exception nội bộ lộ ra giao diện.
            redirectToFail(response, contextPath, "processing_error");
        }
    }

    // =========================================================
    // HELPER: LẤY THAM SỐ REQUEST
    // =========================================================
    private Map<String, String> getRequestParameters(
            HttpServletRequest request) {

        Map<String, String> params = new HashMap<>();

        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        });

        return params;
    }

    // =========================================================
    // HELPER: KIỂM TRA CHUỖI RỖNG
    // =========================================================
    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    // =========================================================
    // HELPER: CHUYỂN TỚI TRANG THẤT BẠI
    // =========================================================
    private void redirectToFail(
            HttpServletResponse response,
            String contextPath,
            String reason) throws IOException {

        response.sendRedirect(
                contextPath + FAIL_PAGE + "?reason=" + reason);
    }
}
