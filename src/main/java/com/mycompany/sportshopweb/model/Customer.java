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
public class Customer implements Serializable{
    private String customerId;
    private User user;
    private String firstName;
    private String lastName;
    private String gender;
    
    public Customer(){
        customerId = "";
        user = new User();
        firstName = "";
        lastName = "";
        gender = "";
    }
    
    public Customer(String customerId, User user, String firstName, String lastName, String gender){
        this.customerId = customerId;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
