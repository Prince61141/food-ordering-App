// MainActivity.java
package com.example.foodorderingapp.signup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.Helper.DatabaseHelper;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.login.Login;

public class signup extends AppCompatActivity {
    private EditText username, password, confirmPassword;
    private Button signupButton;
    private TextView loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);
        dbHelper = new DatabaseHelper(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to Login Activity
                Intent intent = new Intent(signup.this, Login.class);
                startActivity(intent);
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    private void registerUser() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUser(user)) {
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.registerUser(user, pass)) {
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(signup.this, Login.class));
            finish();
        } else {
            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
