package com.example.foodorderingapp.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.BuildConfig;
import com.example.foodorderingapp.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        TextView checkoutTotal = findViewById(R.id.checkoutTotalText);
        RadioGroup paymentOptions = findViewById(R.id.paymentOptions);
        Button btnConfirm = findViewById(R.id.btnPayNow);

        Intent intent = getIntent();
        String totalAmountString = intent.getStringExtra("total_amount");
        double totalAmount = Double.parseDouble(totalAmountString);
        checkoutTotal.setText("Payable Amount: ₹" + totalAmount);

        Checkout.preload(getApplicationContext());

        btnConfirm.setOnClickListener(v -> {
            int selectedId = paymentOptions.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            } else if (selectedId == R.id.radioRazorpay) {
                startPayment();
            } else if (selectedId == R.id.radioCOD) {
                handleCashOnDelivery();
            }
        });

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
        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        SharedPreferences prefs = getSharedPreferences("OrderPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get existing orders
        String existingOrdersJson = prefs.getString("order_list", "[]");
        try {
            JSONArray orderArray = new JSONArray(existingOrdersJson);

            // Create new order object
            JSONObject orderObject = new JSONObject();
            orderObject.put("orderId", razorpayPaymentID);
            orderObject.put("dateTime", currentDateTime);

            // Add to array
            orderArray.put(orderObject);

            // Save updated array back
            editor.putString("order_list", orderArray.toString());
            editor.apply();

            Intent successIntent = new Intent(this, SuccessActivity.class);
            startActivity(successIntent);
            finish();

        } catch (Exception e) {
            Toast.makeText(this, "Error saving order", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_LONG).show();
    }

    private void handleCashOnDelivery() {
        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        SharedPreferences prefs = getSharedPreferences("OrderPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String existingOrdersJson = prefs.getString("order_list", "[]");

        try {
            JSONArray orderArray = new JSONArray(existingOrdersJson);

            JSONObject orderObject = new JSONObject();
            orderObject.put("orderId", "COD_" + System.currentTimeMillis()); // unique ID
            orderObject.put("dateTime", currentDateTime);
            orderObject.put("paymentMethod", "Cash on Delivery");

            orderArray.put(orderObject);

            editor.putString("order_list", orderArray.toString());
            editor.apply();

            Toast.makeText(this, "Order placed with Cash on Delivery!", Toast.LENGTH_LONG).show();

            // Optionally go to a confirmation screen or back to homepage
            Intent successIntent = new Intent(this, SuccessActivity.class);
            startActivity(successIntent);
            finish();


        } catch (Exception e) {
            Toast.makeText(this, "Error saving order", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
