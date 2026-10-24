/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.util;

import com.mycompany.sportshopweb.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author dung2
 */
public class CategoryDB {
    public static List<Category> getAllCategory() {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    try {
            TypedQuery<Category> query = em.createQuery("SELECT p FROM Category  p", Category.class);
            return query.getResultList();
    } finally {
            em.close();
        }
    }
    
}
