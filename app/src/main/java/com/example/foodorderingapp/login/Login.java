package com.example.foodorderingapp.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.homepage.homepage;
import com.example.foodorderingapp.Helper.DatabaseHelper;
import com.example.foodorderingapp.signup.signup;

import java.util.Locale;

public class Login extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private TextView signupButton, forgotPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        // If user is already logged in, redirect them to the home page
        if (isLoggedIn) {
            Intent intent = new Intent(Login.this, homepage.class);
            startActivity(intent);
            finish(); // Prevent the user from returning to login screen
            return;
        }

        dbHelper = new DatabaseHelper(this);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupbotton);
        forgotPassword = findViewById(R.id.Forgottext);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkUserCredentials(user, pass)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", user); // Store username or any other user data if needed
                        editor.apply();

                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, homepage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, signup.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    private boolean checkUserCredentials(String user, String pass) {
        Cursor cursor = dbHelper.getReadableDatabase().query(
                "users",
                new String[]{"password"},
                "username=?",
                new String[]{user},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(0);
            cursor.close();
            return storedPassword.equals(dbHelper.hashPassword(pass));
        }

        cursor.close();
        return false;
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