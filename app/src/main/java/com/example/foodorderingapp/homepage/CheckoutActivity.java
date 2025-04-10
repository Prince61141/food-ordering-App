package com.example.foodorderingapp.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
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

import java.util.Locale;

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

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
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

    public static final String PREFS_NAME = "AppPrefs";
    public static final String KEY_LANGUAGE = "language";

    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String language = prefs.getString(KEY_LANGUAGE, "en");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        Context context = newBase;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context = newBase.createConfigurationContext(config);
        } else {
            newBase.getResources().updateConfiguration(config, newBase.getResources().getDisplayMetrics());
        }

        super.attachBaseContext(context);
    }
}
