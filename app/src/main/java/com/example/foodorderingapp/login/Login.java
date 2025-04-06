package com.example.foodorderingapp.login;

import android.content.Intent;
import android.database.Cursor;
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

public class Login extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private TextView signupButton, forgotPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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
                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(Login.this, HomeActivity.class)); // Change to your home activity
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
                Toast.makeText(Login.this, "Forgot Password feature not implemented yet", Toast.LENGTH_SHORT).show();
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
}