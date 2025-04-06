package com.example.foodorderingapp.Adapter;

public interface CartTotalListener {
    void onCartTotalUpdated(double subtotal, double gst, double deliveryFee, double total);
}
