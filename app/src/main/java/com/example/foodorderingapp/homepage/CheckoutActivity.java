package com.example.foodorderingapp.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.BuildConfig;
import com.example.foodorderingapp.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        TextView checkoutTotal = findViewById(R.id.checkoutTotalText);

        Intent intent = getIntent();
        String totalAmountString = intent.getStringExtra("total_amount");

        double totalAmount = Double.parseDouble(totalAmountString);
        checkoutTotal.setText("Payable Amount: ₹" + totalAmount);

        Checkout.preload(getApplicationContext());

        Button btnPayNow = findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(v -> startPayment());
    }

    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID(BuildConfig.RAZORPAY_KEY);  // Use secure key

        Intent intent = getIntent();
        String totalAmountString = intent.getStringExtra("total_amount");
        double totalAmount = Double.parseDouble(totalAmountString);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Food Ordering App");
            options.put("description", "Payment for order");
            options.put("currency", "INR");
            options.put("amount", totalAmount * 100); // amount in paise
            options.put("prefill.email", "");
            options.put("prefill.contact", "");

            checkout.open(CheckoutActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Payment failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful! ID: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_LONG).show();
    }
}
