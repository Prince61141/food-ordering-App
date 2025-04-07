package com.example.foodorderingapp.Helper;

import com.example.foodorderingapp.homepage.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final List<Product> cartList = new ArrayList<>();

    public static void addToCart(Product product) {
        // Check if already in cart
        for (Product p : cartList) {
            if (p.id == product.id) {
                p.quantity++; // Increase quantity
                return;
            }
        }
        product.quantity = 1;
        cartList.add(product);
    }

    public static int getTotalItemCount() {
        int count = 0;
        for (Product p : cartList) {
            count += p.quantity;
        }
        return count;
    }

    public static List<Product> getCartItems() {
        return cartList;
    }

    public static void clearCart() {
        cartList.clear();
    }
}
