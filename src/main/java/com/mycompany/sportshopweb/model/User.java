/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 *
 * @author Anh Tuan
 */
public class User implements Serializable{
    
    private String userId;
    private String phone;
    private String email;
    private String passwordHash;
    private AccountStatus status;
    private LocalDateTime createdAt;
    
    // Constructor 0 tham số
    public User() {
        userId = "";
        phone = "";
        email = "";
        passwordHash = "";
        status = AccountStatus.ACTIVE;
        createdAt = LocalDateTime.now();
    }

    // Constructor có tham số
    public User(String phone, String email, String passwordHash) {
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

