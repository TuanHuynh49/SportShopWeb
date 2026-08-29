package com.sportshop.model;

/**
 * UserStatus — Trạng thái hoạt động của tài khoản người dùng
 */
public enum UserStatus {
    ACTIVE("Đang hoạt động"),
    LOCKED("Đã bị khóa");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
