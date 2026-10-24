package com.mycompany.sportshopweb.util;

import com.mycompany.sportshopweb.dao.OrderDAO;
import com.mycompany.sportshopweb.dao.PaymentDAO;
import com.mycompany.sportshopweb.dao.ProductVariantDAO;
import com.mycompany.sportshopweb.model.Customer;
import com.mycompany.sportshopweb.model.Order;
import com.mycompany.sportshopweb.model.OrderItem;
import com.mycompany.sportshopweb.model.OrderStatus;
import com.mycompany.sportshopweb.model.Payment;
import com.mycompany.sportshopweb.model.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service xử lý nghiệp vụ liên quan đến Payment và điều phối tồn kho khi thanh toán.
 */
public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;
    private final ProductVariantDAO productVariantDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.orderDAO = new OrderDAO();
        this.productVariantDAO = new ProductVariantDAO();
    }

    public Payment findById(int paymentId) {
        return paymentDAO.findById(paymentId);
    }

    public List<Payment> findByOrderId(int orderId) {
        return paymentDAO.findByOrderId(orderId);
    }

    public List<Payment> findAll() {
        return paymentDAO.findAll();
    }

    public void insertPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment must not be null.");
        }
        paymentDAO.insertPayment(payment);
    }

    public void updatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment must not be null.");
        }
        paymentDAO.updatePayment(payment);
    }

    /**
     * Kiểm tra và lấy đơn hàng hợp lệ để thanh toán:
     * - Đơn hàng phải tồn tại và thuộc sở hữu của khách hàng.
     * - Trạng thái đơn hàng phải là CONFIRMED và số tiền > 0.
     * - Thực hiện khóa giữ chỗ (Soft Lock) số lượng tồn kho cho các sản phẩm trong đơn.
     *
     * @param orderId ID đơn hàng
     * @param customer Khách hàng đang đăng nhập
     * @return Order nếu hợp lệ và giữ chỗ thành công, ngược lại trả về null
     */
    public Order getOrderForPayment(int orderId, Customer customer) {
        if (orderId <= 0 || customer == null) {
            return null;
        }

        List<Order> orders = orderDAO.findByCustomerId(customer.getCustomerId());
        if (orders == null || orders.isEmpty()) {
            return null;
        }

        Order selectedOrder = null;
        for (Order o : orders) {
            if (o.getOrderId() == orderId) {
                selectedOrder = o;
                break;
            }
        }

        if (selectedOrder == null) {
            return null;
        }

        // Chỉ cho phép thanh toán đơn hàng có trạng thái CONFIRMED và số tiền hợp lệ
        if (selectedOrder.getOrderStatus() != OrderStatus.CONFIRMED
                || selectedOrder.getTotalAmount() <= 0) {
            return null;
        }

        // Khóa giữ chỗ (Soft Lock) cho từng mặt hàng trong đơn
        if (selectedOrder.getOrderItems() != null) {
            for (OrderItem item : selectedOrder.getOrderItems()) {
                if (item.getProductVariant() != null) {
                    int variantId = item.getProductVariant().getVariantId();
                    int quantity = item.getQuantity();
                    productVariantDAO.reserveStock(variantId, quantity);
                }
            }
        }

        return selectedOrder;
    }

    /**
     * Xử lý xác nhận thanh toán thành công (khi nhận callback từ cổng thanh toán):
     * 1. Đối chiếu số tiền thanh toán (Anti-tampering Check).
     * 2. Ghi nhận bản ghi Payment (chống trùng lặp Idempotency).
     * 3. Trừ số lượng tồn kho thực tế và xóa giữ chỗ (Phương án A).
     * 4. Cập nhật trạng thái đơn hàng sang COMPLETED.
     *
     * @param orderId ID đơn hàng
     * @param customer Khách hàng đăng nhập
     * @param amountText Số tiền do cổng thanh toán gửi về (đơn vị xu = VND * 100)
     * @param transactionNo Mã giao dịch do cổng thanh toán cấp
     * @param method Phương thức thanh toán (VNPAY, MOMO,...)
     * @return "SUCCESS" nếu thành công, hoặc mã lỗi (amount_mismatch, order_not_found, missing_data,...)
     */
    public String processPaymentSuccess(
            int orderId,
            Customer customer,
            String amountText,
            String transactionNo,
            PaymentMethod method) {

        if (orderId <= 0 || customer == null || amountText == null || transactionNo == null) {
            return "missing_data";
        }

        List<Order> orders = orderDAO.findByCustomerId(customer.getCustomerId());
        Order selectedOrder = null;
        if (orders != null) {
            for (Order o : orders) {
                if (o.getOrderId() == orderId) {
                    selectedOrder = o;
                    break;
                }
            }
        }

        if (selectedOrder == null) {
            return "order_not_found";
        }

        // 1. Đối chiếu số tiền (VND * 100)
        long expectedAmount = Math.multiplyExact(selectedOrder.getTotalAmount(), 100L);
        long returnedAmount;
        try {
            returnedAmount = Long.parseLong(amountText);
        } catch (NumberFormatException e) {
            return "invalid_amount";
        }

        if (returnedAmount != expectedAmount) {
            return "amount_mismatch";
        }

        // 2. Tạo bản ghi Payment nếu chưa có (Idempotency)
        List<Payment> existingPayments = paymentDAO.findByOrderId(orderId);
        if (existingPayments == null || existingPayments.isEmpty()) {
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setPaymentMethod(method != null ? method : PaymentMethod.VNPAY);
            payment.setAmount(selectedOrder.getTotalAmount());
            payment.setTransactionCode(transactionNo);
            payment.setPaymentTime(LocalDateTime.now());
            paymentDAO.insertPayment(payment);
        }

        // 3. Trừ kho thực tế và xóa giữ chỗ cho từng sản phẩm trong đơn hàng
        if (selectedOrder.getOrderItems() != null) {
            for (OrderItem item : selectedOrder.getOrderItems()) {
                if (item.getProductVariant() != null) {
                    int variantId = item.getProductVariant().getVariantId();
                    int quantity = item.getQuantity();
                    productVariantDAO.deductReservedStock(variantId, quantity);
                }
            }
        }

        // 4. Cập nhật trạng thái đơn hàng sang COMPLETED
        boolean updated = orderDAO.updateOrderStatus(orderId, OrderStatus.COMPLETED);
        if (!updated) {
            return "update_status_failed";
        }

        return "SUCCESS";
    }
}
