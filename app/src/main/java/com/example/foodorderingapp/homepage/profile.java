package com.example.foodorderingapp.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodorderingapp.Helper.ProfileHelper;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.login.Login;
import android.database.Cursor;

import java.util.Locale;

public class profile extends AppCompatActivity {

    EditText editName, editEmail, editPhone,editUsername ,editAddress;
    Button btnSave;
    ImageView profileImage;
    SharedPreferences sharedPreferences;
    ProfileHelper dbHelper;
    String email;
    public static final String PREF_NAME = "UserSession";
    public static final String PREFS_NAME = "AppPrefs";
    public static final String KEY_LANGUAGE = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button langButton = findViewById(R.id.lanchang);
        langButton.setOnClickListener(v -> showLanguagePopup(v));

        editName = findViewById(R.id.tv_name);
        editEmail = findViewById(R.id.tv_email);
        editPhone = findViewById(R.id.tv_phone);
        editAddress = findViewById(R.id.tv_address);
        editUsername = findViewById(R.id.tv_username);

        profileImage = findViewById(R.id.account_profile);

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);  // 100 is the request code
        });

        btnSave = findViewById(R.id.btn_save);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        dbHelper = new ProfileHelper(this);

        email = sharedPreferences.getString("email", "");
        editEmail.setText(email);  // Make email uneditable
        editEmail.setEnabled(false);

        loadProfile();

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String phone = editPhone.getText().toString();
            String username = editUsername.getText().toString();
            String address = editAddress.getText().toString();


            dbHelper.upsertUser(email, name, phone,username,address);
            Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
        });

        // Change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    @Override
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

    private void showLanguagePopup(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.language, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.lang_en) {
                setLocale("en");
                return true;
            } else if (id == R.id.lang_hi) {
                setLocale("hi");
                return true;
            } else if (id == R.id.lang_gu) {
                setLocale("gu");
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void setLocale(String langCode) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_LANGUAGE, langCode);
        editor.apply();

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Restart current activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
            editor.remove("email");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            ImageView profileImage = findViewById(R.id.account_profile);
            profileImage.setImageURI(imageUri);

            // Optional: Save URI string to SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("UserSession", MODE_PRIVATE).edit();
            editor.putString("image_uri", imageUri.toString());
            editor.apply();
        }
    }



    private void loadProfile() {
        Cursor cursor = dbHelper.getUserByEmail(email);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));

            editName.setText(name);
            editPhone.setText(phone);
            editUsername.setText(username);
            editAddress.setText(address);


        }
        cursor.close();
    }


    // Back button functionality to navigate to Homepage
    public void goBackToHomepage(View view) {
        Intent intent = new Intent(profile.this, homepage.class); // Assuming homepage is your main activity
        startActivity(intent);
        finish();  // Close the current activity
    }
}
