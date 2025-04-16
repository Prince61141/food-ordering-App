package com.example.foodorderingapp.homepage;

public class Order {
    private String orderId;
    private String dateTime;

    public Order(String orderId, String dateTime) {
        this.orderId = orderId;
        this.dateTime = dateTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDateTime() {
        return dateTime;
    }
}
