package com.purchase.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name="account")
public class AccountEntity {
    @Id
    private int customerId;
    private String customerName;
    private String bank;
    private double custBalance;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public double getCustBalance() {
        return custBalance;
    }

    public void setCustBalance(double custBalance) {
        this.custBalance = custBalance;
    }
}
