package com.example.foodorderingapp.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.R;

import java.util.Locale;

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
