package com.mycompany.sportshopweb.model;

public class Voucher {

    private int voucherId;
    private String code;
    private double discount;
    private boolean active;

    public Voucher() {
    }

    public Voucher(int voucherId, String code, double discount, boolean active) {
        this.voucherId = voucherId;
        this.code = code;
        this.discount = discount;
        this.active = active;
    }

    // Getter & Setter cho voucherId
    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    // Getter & Setter cho code
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Getter & Setter cho discount
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // Getter & Setter cho active
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}