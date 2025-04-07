package com.example.foodorderingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.Helper.GMailSender;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.homepage.cart;
import com.example.foodorderingapp.homepage.homepage;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailInput;
    Button sendOtpBtn;
    String generatedOtp;
    public static String globalEmail, globalOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.emailInput);
        sendOtpBtn = findViewById(R.id.sendOtpBtn);

        sendOtpBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulate OTP generation
            generatedOtp = String.format("%06d", new Random().nextInt(999999));
            globalEmail = email;
            globalOtp = generatedOtp;

            // Use a new thread to send email
            new Thread(() -> {
                try {
                    // PASTE YOUR EMAIL AND APP PASSWORD BELOW
                    GMailSender sender = new GMailSender("youremail@gmail.com", "your_app_password");

                    sender.sendMail(email, "OTP Verification", "Your OTP is: " + generatedOtp);
                    runOnUiThread(() -> Toast.makeText(this, "OTP sent to your email", Toast.LENGTH_SHORT).show());

                    startActivity(new Intent(this, EnterOtpActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Failed to send OTP", Toast.LENGTH_SHORT).show());
                }
            }).start();

            startActivity(new Intent(this, EnterOtpActivity.class));
        });

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    public void goBackToHomepage(View view) {
        Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}
