package com.app.narlocks.model;

import java.io.Serializable;

public class Order implements Serializable {

    public static final int STATUS_OPENED = 1;
    public static final int STATUS_CLOSED = 2;

    public static final int PAYMENT_METHOD_CASH = 1;
    public static final int PAYMENT_METHOD_DEBIT = 2;
    public static final int PAYMENT_METHOD_CREDIT = 3;

    private int id;
    private int status;
    private Integer paymentMethod;
    private Double balance;
    private User user;

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
