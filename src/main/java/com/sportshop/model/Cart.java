package com.sportshop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart — Giỏ hàng người dùng
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public int getCartItemCount() {
        if (isEmpty()) return 0;
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getTotalAmount() {
        if (isEmpty()) return BigDecimal.ZERO;
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
