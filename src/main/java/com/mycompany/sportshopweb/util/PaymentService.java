
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;
    private final OrderDAO orderDAO;
    private final ProductVariantDAO productVariantDAO;

    // =========================================================
    // INIT
    // =========================================================
    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
        this.orderDAO = new OrderDAO();
        this.productVariantDAO = new ProductVariantDAO();
    }

    // =========================================================
    // PAYMENT CRUD
    // =========================================================
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
            throw new IllegalArgumentException(
                    "Payment must not be null.");
        }

        paymentDAO.insertPayment(payment);
    }

    public void updatePayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException(
                    "Payment must not be null.");
        }

        paymentDAO.updatePayment(payment);
    }

    // =========================================================
    // GET ORDER BEFORE REDIRECTING TO VNPAY
    // =========================================================
    public Order getOrderForPayment(
            int orderId,
            Customer customer) {

        if (orderId <= 0 || customer == null) {
            return null;
        }

        List<Order> orders = orderDAO.findByCustomerId(
                customer.getCustomerId());

        if (orders == null) {
            return null;
        }

        for (Order order : orders) {
            if (order == null) {
                continue;
            }

            boolean sameOrder =
                    order.getOrderId() == orderId;

            boolean sameCustomer =
                    order.getCustomer() != null
                    && order.getCustomer().getCustomerId()
                    == customer.getCustomerId();

            boolean waitingForPayment =
                    order.getOrderStatus()
                    == OrderStatus.CONFIRMED;

            boolean validAmount =
                    order.getTotalAmount() > 0;

            if (sameOrder
                    && sameCustomer
                    && waitingForPayment
                    && validAmount) {

                return order;
            }
        }

        return null;
    }

    // =========================================================
    // PROCESS VNPAY PAYMENT SUCCESS
    // =========================================================
    public String processPaymentSuccess(
            int orderId,
            Customer customer,
            String amountText,
            String transactionNo,
            PaymentMethod method) {

        // 1. Validate input.
        if (orderId <= 0
                || customer == null
                || amountText == null
                || amountText.isBlank()
                || transactionNo == null
                || transactionNo.isBlank()
                || method != PaymentMethod.VNPAY) {

            return "missing_data";
        }

        // 2. Parse the amount returned by VNPay.
        // VNPay vnp_Amount = order amount * 100.
        final long returnedAmount;

        try {
            returnedAmount = Long.parseLong(amountText);
        } catch (NumberFormatException e) {
            return "invalid_amount";
        }

        if (returnedAmount <= 0) {
            return "invalid_amount";
        }

        EntityManager em =
                DBUtil.getEmFactory().createEntityManager();

        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            // 3. Lock the order to prevent concurrent processing.
            Order order = orderDAO.findByIdForUpdate(
                    em, orderId);

            if (order == null) {
                rollback(trans);
                return "order_not_found";
            }

            // 4. Verify order ownership.
            if (order.getCustomer() == null
                    || order.getCustomer().getCustomerId()
                    != customer.getCustomerId()) {

                rollback(trans);
                return "order_not_found";
            }

            // 5. Verify the amount.
            final long expectedAmount;

            try {
                expectedAmount = Math.multiplyExact(
                        order.getTotalAmount(), 100L);
            } catch (ArithmeticException e) {
                rollback(trans);
                return "invalid_amount";
            }

            if (returnedAmount != expectedAmount) {
                rollback(trans);
                return "amount_mismatch";
            }

            // 6. Check whether this transaction has already
            // been recorded for this order.
            List<Payment> existingPayments =
                    paymentDAO.findByOrderId(em, orderId);

            if (existingPayments != null) {
                for (Payment existing : existingPayments) {
                    if (existing == null) {
                        continue;
                    }

                    if (transactionNo.equals(
                            existing.getTransactionCode())) {

                        boolean sameSuccessfulPayment =
                                existing.getPaymentMethod()
                                        == PaymentMethod.VNPAY
                                && existing.getAmount()
                                        == order.getTotalAmount()
                                && order.getOrderStatus()
                                        == OrderStatus.COMPLETED;

                        if (sameSuccessfulPayment) {
                            trans.commit();
                            return "SUCCESS";
                        }

                        rollback(trans);
                        return "payment_state_conflict";
                    }
                }
            }

            // 7. Only a pending order may be paid.
            if (order.getOrderStatus()
                    != OrderStatus.CONFIRMED) {

                rollback(trans);
                return "order_not_payable";
            }

            // 8. Load and validate order items.
            List<OrderItem> items = order.getOrderItems();

            if (items == null || items.isEmpty()) {
                rollback(trans);
                return "order_items_empty";
            }

            // 9. Deduct stock inside the same transaction.
            // If any item cannot be deducted, roll back everything.
            for (OrderItem item : items) {
                if (item == null
                        || item.getProductVariant() == null
                        || item.getQuantity() <= 0) {

                    throw new IllegalStateException(
                            "Invalid order item.");
                }

                int variantId = item.getProductVariant()
                        .getVariantId();

                boolean deducted =
                        productVariantDAO.deductStock(
                                em,
                                variantId,
                                item.getQuantity());

                if (!deducted) {
                    rollback(trans);
                    return "insufficient_stock";
                }
            }

            // 10. Create the payment record.
            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setPaymentMethod(PaymentMethod.VNPAY);
            payment.setAmount(order.getTotalAmount());
            payment.setTransactionCode(transactionNo);
            payment.setPaymentTime(LocalDateTime.now());

            paymentDAO.insertPayment(em, payment);

            // 11. Update order status in the same transaction.
            boolean updated = orderDAO.updateOrderStatus(
                    em,
                    orderId,
                    OrderStatus.COMPLETED);

            if (!updated) {
                rollback(trans);
                return "update_status_failed";
            }

            // 12. Commit payment, stock and order status together.
            trans.commit();

            return "SUCCESS";

        } catch (RuntimeException e) {
            rollback(trans);

            throw e;

        } finally {
            em.close();
        }
    }

    // =========================================================
    // HELPER: SAFE ROLLBACK
    // =========================================================
    private void rollback(EntityTransaction trans) {
        if (trans != null && trans.isActive()) {
            trans.rollback();
        }
    }
}
