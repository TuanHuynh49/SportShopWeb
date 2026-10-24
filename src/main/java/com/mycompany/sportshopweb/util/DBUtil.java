/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
public class DBUtil {
    private static final EntityManagerFactory emf=Persistence.createEntityManagerFactory("SportShopWebPU");
    public static EntityManagerFactory getEmFactory(){
        return emf; // khởi tạo EntityManagerFactory dùng chung
    }
}
