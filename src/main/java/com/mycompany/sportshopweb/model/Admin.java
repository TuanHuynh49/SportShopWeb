/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.model;
import java.io.Serializable;

/**
 *
 * @author Anh Tuan
 */
public class Admin implements Serializable{
    
    private String adminId;
    private User user;
    private Role role;

    public Admin() {
        this.adminId = "";
        this.user = new User();
        this.role = new Role();
    }

    public Admin(String adminId, User user, Role role) {
        this.adminId = adminId;
        this.user = user;
        this.role = role;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
