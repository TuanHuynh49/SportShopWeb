package com.sportshop.model;

/**
 * Role — Vai trò người dùng trong hệ thống RBAC (Role-Based Access Control)
 */
public enum Role {
    CUSTOMER("Khách hàng"),
    ADMIN("Quản trị viên");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
