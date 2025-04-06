package com.example.foodorderingapp.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.R;

public class profile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);  // Reference to the layout file for ProfileActivity

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    public void goBackToHomepage(View view) {
        Intent intent = new Intent(profile.this, homepage.class); // MainActivity is your homepage
        startActivity(intent);
        finish();
    }
}
