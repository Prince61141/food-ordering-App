package com.example.foodorderingapp.homepage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.foodorderingapp.Helper.ProfileHelper;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.login.Login;
import android.database.Cursor;

public class profile extends AppCompatActivity {

    EditText editName, editEmail, editPhone,editUsername ,editAddress;
    Button btnSave;
    ImageView profileImage;
    SharedPreferences sharedPreferences;
    ProfileHelper dbHelper;
    String email;
    public static final String PREF_NAME = "UserSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String imageUriStr = sharedPreferences.getString("image_uri", null);
        if (imageUriStr != null) {
            Uri imageUri = Uri.parse(imageUriStr);
            profileImage.setImageURI(imageUri);
        }
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
