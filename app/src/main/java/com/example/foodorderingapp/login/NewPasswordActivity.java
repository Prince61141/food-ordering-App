package com.example.foodorderingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.foodorderingapp.Helper.DatabaseHelper;
import com.example.foodorderingapp.R;

public class NewPasswordActivity extends AppCompatActivity {

    EditText newPasswordInput, confirmPasswordInput;
    Button resetPasswordBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        dbHelper = new DatabaseHelper(this);

        resetPasswordBtn.setOnClickListener(v -> {
            String newPass = newPasswordInput.getText().toString().trim();
            String confirmPass = confirmPasswordInput.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = dbHelper.updatePassword(ForgotPasswordActivity.globalEmail, newPass);

            if (updated) {
                Toast.makeText(this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    public void goBackToHomepage(View view) {
        Intent intent = new Intent(NewPasswordActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}
