package com.example.foodorderingapp.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodorderingapp.R;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        LottieAnimationView animationView = findViewById(R.id.successAnimation);
        animationView.playAnimation();

        // Navigate to homepage after 2.5 seconds
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SuccessActivity.this, homepage.class));
            finish();
        }, 2500);
    }
}
