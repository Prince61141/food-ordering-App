// MainActivity.java
package com.example.foodorderingapp.signup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.Helper.DatabaseHelper;
import com.example.foodorderingapp.Helper.GMailSender;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.homepage.homepage;
import com.example.foodorderingapp.login.EnterOtpActivity;
import com.example.foodorderingapp.login.Login;

import java.util.Locale;

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
            // Use a new thread to send email
            new Thread(() -> {
                try {
                    // PASTE YOUR EMAIL AND APP PASSWORD BELOW
                    GMailSender sender = new GMailSender("youremail@gmail.com", "your_app_password");

                    String emailBody = getHtmlEmailBody();
                    sender.sendMail(user, "Welcome to Food Delivery App!", emailBody);

                    startActivity(new Intent(this, Login.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show());
                }
            }).start();

            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(signup.this, Login.class));
            finish();
        } else {
            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String getHtmlEmailBody() {
       return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Welcome to [Your App Name] - Signup Confirmation</title><style>body{font-family:Arial,sans-serif;color:#333;background-color:#f4f4f4;margin:0;padding:0}.container{max-width:600px;margin:0 auto;background-color:#fff;padding:20px;border-radius:8px;box-shadow:0 2px 10px rgba(0,0,0,0.1)}.header{background-color:#ff9800;color:#fff;padding:15px;text-align:center;border-radius:8px 8px 0 0}.message{margin-top:20px}.message p{font-size:16px;line-height:1.5}.button{display:inline-block;background-color:#ff9800;color:#fff;padding:12px 20px;font-size:16px;text-decoration:none;border-radius:5px;text-align:center;margin-top:20px}.footer{margin-top:30px;font-size:14px;text-align:center;color:#888}.footer a{color:#ff9800;text-decoration:none}</style></head><body><div class=\"container\"><div class=\"header\"><h1>Welcome to [Your App Name]!</h1></div><div class=\"message\"><h2>Thanks for signing up!</h2><p>We’re excited to have you on board! Before you can start ordering your favorite meals, we need you to confirm your email address. Please click the button below to verify your account and activate your profile.</p><a href=\"[Verification Link]\" class=\"button\">Confirm My Email</a><p>If you did not sign up for [Your App Name], please ignore this email.</p></div><div class=\"footer\"><p>For any issues or help, feel free to <a href=\"mailto:support@yourapp.com\">contact us</a>.</p><p>We are always here to assist you!</p></div></div></body></html>";
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
