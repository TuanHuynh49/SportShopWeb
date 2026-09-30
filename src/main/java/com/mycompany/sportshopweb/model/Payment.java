package com.mycompany.sportshopweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

//@author Vu
public class Payment implements Serializable {
    private int paymentId;
    private int orderId;
    private PaymentMethod paymentMethod;
    private long amount;
    private String transactionCode;
    private LocalDateTime paymentTime;

    // constructor 0 tham so
    public Payment() {
    }

    // constructor co tham so
    public Payment(
            int paymentId,
            int orderId,
            PaymentMethod maymentMethod,
            long amount,
            String transactionCode,
            LocalDateTime paymentTime) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        // this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.transactionCode = transactionCode;
        this.paymentTime = paymentTime;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }
}
