package com.example.foodorderingapp.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.Adapter.CartAdapter;
import com.example.foodorderingapp.Adapter.CartTotalListener;
import com.example.foodorderingapp.Helper.CartManager;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.homepage.Product;

import java.util.List;

public class cart extends AppCompatActivity {

    RecyclerView recyclerView;
    CartAdapter adapter;
    TextView cartTotalText,gstText,deliveryFeeText,finalAmountText;
    List<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);  // layout file

        cartItems = CartManager.getCartItems(); // fetch cart items
        cartTotalText = findViewById(R.id.cartTotal);
        gstText = findViewById(R.id.gstText);
        deliveryFeeText = findViewById(R.id.deliveryFeeText);
        finalAmountText = findViewById(R.id.finalAmountText);

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CartAdapter(CartManager.getCartItems(), new CartTotalListener() {
            @Override
            public void onCartTotalUpdated(double subtotal, double gst, double deliveryFee, double total) {
                cartTotalText.setText("Subtotal: ₹" + String.format("%.2f", subtotal));
                gstText.setText("GST (18%): ₹" + String.format("%.2f", gst));
                deliveryFeeText.setText("Delivery Fee: ₹" + String.format("%.2f", deliveryFee));
                finalAmountText.setText("Total: ₹" + String.format("%.2f", total));
            }
        });

        recyclerView.setAdapter(adapter);

        // Show initial total
        double initialTotal = calculateTotal(cartItems);
        cartTotalText.setText("Total: ₹" + initialTotal);

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    private double calculateTotal(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.price * product.quantity;
        }
        return total;
    }

    public void goBackToHomepage(View view) {
        Intent intent = new Intent(cart.this, homepage.class);
        startActivity(intent);
        finish();
    }
}
