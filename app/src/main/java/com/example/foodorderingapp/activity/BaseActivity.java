package com.example.foodorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.R;
import com.example.foodorderingapp.login.Login;  // Correct import statement for Login activity

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Handler to delay the redirect to Login activity by 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After 5 seconds, start Login activity
                Intent intent = new Intent(BaseActivity.this, Login.class);
                startActivity(intent);
                finish(); // Finish the BaseActivity so it doesn't show up in the back stack
            }
        }, 2000); // Delay of 2000ms (2 seconds)

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }
}
