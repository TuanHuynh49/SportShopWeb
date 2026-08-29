package com.sportshop.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User — Đối tượng người dùng hệ thống phục vụ Authentication & Authorization.
 * Tuân thủ đầy đủ các thuộc tính cốt lõi: id, fullName, email, passwordHash, role, phone, status.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String fullName;
    private String email;
    private String passwordHash;
    private Role role;
    private String phone;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
        this.role = Role.CUSTOMER;
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(Long id, String fullName, String email, String passwordHash, Role role, String phone, UserStatus status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role != null ? role : Role.CUSTOMER;
        this.phone = phone;
        this.status = status != null ? status : UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Helper methods for RBAC & security
    public boolean isAdmin() {
        return Role.ADMIN.equals(this.role);
    }

    public boolean isCustomer() {
        return Role.CUSTOMER.equals(this.role);
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    public boolean isLocked() {
        return UserStatus.LOCKED.equals(this.status);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                '}';
    }
}
