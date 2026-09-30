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
public class MembershipTier implements Serializable{
    
    private int tierId;
    private String tierName;
    private long minSpending;
    private float discountRate;
    private float pointMultiplier;

    public MembershipTier() {
        this.tierId = 0;
        this.tierName = "";
        this.minSpending = 0;
        this.discountRate = 0.0f;
        this.pointMultiplier = 0.0f;
    }

    public MembershipTier(int tierId, String tierName, long minSpending, float discountRate, float pointMultiplier) {
        this.tierId = tierId;
        this.tierName = tierName;
        this.minSpending = minSpending;
        this.discountRate = discountRate;
        this.pointMultiplier = pointMultiplier;
    }

    public int getTierId() {
        return tierId;
    }

    public void setTierId(int tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public long getMinSpending() {
        return minSpending;
    }

    public void setMinSpending(long minSpending) {
        this.minSpending = minSpending;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public float getPointMultiplier() {
        return pointMultiplier;
    }

    public void setPointMultiplier(float pointMultiplier) {
        this.pointMultiplier = pointMultiplier;
    }
}
