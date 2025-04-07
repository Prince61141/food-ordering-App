package com.example.foodorderingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.R;

public class EnterOtpActivity extends AppCompatActivity {

    EditText otpInput;
    Button verifyOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        otpInput = findViewById(R.id.otpInput);
        verifyOtpBtn = findViewById(R.id.verifyOtpBtn);

        verifyOtpBtn.setOnClickListener(v -> {
            String enteredOtp = otpInput.getText().toString().trim();

            if (enteredOtp.equals(ForgotPasswordActivity.globalOtp)) {
                Toast.makeText(this, "OTP Verified!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, NewPasswordActivity.class));
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        });

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }
    public void goBackToHomepage(View view) {
        Intent intent = new Intent(EnterOtpActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}
