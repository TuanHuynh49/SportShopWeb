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
public class Role implements Serializable{
    
    private int roleId;
    private String name;
    private String description;
    private String rolePermission;
    
    public Role (){
        roleId = 0;
        name = "";
        description = "";
        rolePermission = "";
    }
    
    public Role (int roleId, String name, String description, String rolePermission){
        this.roleId = roleId;
        this.name = name;
        this.description = description;
        this.rolePermission = rolePermission;
    }
    
    public int getRoleId() {
        return roleId;
    }
    
    public void setRoleId(int roleId){
        this.roleId = roleId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRolePermission() {
        return rolePermission;
    }

    public void setRolePermission(String rolePermission) {
        this.rolePermission = rolePermission;
    }
}
