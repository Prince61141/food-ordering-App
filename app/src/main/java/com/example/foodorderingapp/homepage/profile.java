package com.example.foodorderingapp.homepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodorderingapp.R;
import com.example.foodorderingapp.login.Login;

public class profile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);  // Reference to the layout file for ProfileActivity

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // Set the Toolbar as the action bar

        // Change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu to show the logout item in the toolbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;  // Return true to display the menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Handle logout action
        if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.remove("id");
            editor.apply();

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            // Navigate to the login screen
            Intent intent = new Intent(profile.this, Login.class);
            startActivity(intent);
            finish();  // Close the ProfileActivity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Back button functionality to navigate to Homepage
    public void goBackToHomepage(View view) {
        Intent intent = new Intent(profile.this, homepage.class); // Assuming homepage is your main activity
        startActivity(intent);
        finish();  // Close the current activity
    }
}
