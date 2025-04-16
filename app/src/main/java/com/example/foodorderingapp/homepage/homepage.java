package com.example.foodorderingapp.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorderingapp.Adapter.BannerAdapter;
import com.example.foodorderingapp.Adapter.ProductAdapter;
import com.example.foodorderingapp.Helper.CartManager;
import com.example.foodorderingapp.Helper.ProductDatabaseHelper;
import android.Manifest;
import com.example.foodorderingapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class homepage extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView location_text;
    private static final int MAX_ADDRESS_LENGTH = 30;

    private LinearLayout searchItem, accountLayout, cartlayout,myOrder;
    private int currentItem = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private EditText searchBar;
    private ImageView cart;

    RecyclerView recyclerView;
    TextView countCart;
    ProductDatabaseHelper dbHelper;
    ProductAdapter adapter;
    List<List<Product>> groupedProducts;

    public static final String PREFS_NAME = "AppPrefs";
    public static final String KEY_LANGUAGE = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        location_text = findViewById(R.id.location_text);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permission
        if (ActivityCompat.checkSelfPermission(homepage.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(homepage.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, get the location
            getLocation();
        }

        searchItem = findViewById(R.id.search_item);
        searchBar = findViewById(R.id.search_bar);

        countCart = findViewById(R.id.countcart);
        int totalItems = CartManager.getTotalItemCount();

        countCart.setText(String.valueOf(totalItems));

        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        ViewPager2 viewPager2 = findViewById(R.id.banner_view_pager);
        List<Integer> bannerImages = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        );

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        viewPager2.setAdapter(bannerAdapter);

        Runnable autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentItem == bannerImages.size() - 1) {
                    currentItem = 0;
                } else {
                    currentItem++;
                }
                viewPager2.setCurrentItem(currentItem, true);
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(autoScrollRunnable, 3000);

        accountLayout = findViewById(R.id.account_profile);
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, profile.class);
                startActivity(intent);
            }
        });

        cartlayout = findViewById(R.id.cart);
        cartlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, cart.class);
                startActivity(intent);
            }
        });

        myOrder = findViewById(R.id.my_order);
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, myorder.class);
                startActivity(intent);
            }
        });

        cart = findViewById(R.id.top_cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homepage.this, cart.class);
                startActivity(intent);
            }
        });

        dbHelper = new ProductDatabaseHelper(this);

        // Insert sample products only once
        dbHelper.insertProduct("Cheese And Corn Pizza", 244, "Puffizza", 4, R.drawable.pizza, "A delicious and cheesy pizza topped with a generous amount of sweet corn kernels, creating the perfect balance of flavors. Baked to perfection with a crispy crust, this pizza is a must-try for cheese lovers.");
        dbHelper.insertProduct("Mexican Green Wave", 279, "Domino's", 4, R.drawable.pizza, "A spicy and flavorful Mexican-inspired pizza loaded with jalapenos, green peppers, onions, and a special blend of spices. Perfect for those who crave a zesty and bold taste, with a burst of freshness from the vegetables.");
        dbHelper.insertProduct("Farmhouse", 299, "Pizza Hut", 4, R.drawable.burger, "A classic farmhouse pizza made with a variety of fresh vegetables like mushrooms, onions, and green peppers, all piled on top of a delicious, golden crust. Topped with melty cheese, this pizza is a savory delight that will satisfy your hunger and your taste buds.");
        dbHelper.insertProduct("Farmhouse", 295, "Pizza Hut", 5, R.drawable.burger, "A deliciously hearty farmhouse pizza with a mix of fresh vegetables, including mushrooms, onions, green peppers, and olives. The thin, crispy crust is perfectly complemented by the rich, melted cheese and tangy tomato sauce, making it an irresistible treat for pizza lovers.");

        List<Product> allProducts = (List<Product>) dbHelper.getAllProducts();
        groupedProducts = groupProductsByTwo(allProducts);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(groupedProducts);
        recyclerView.setAdapter(adapter);

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }

    private List<List<Product>> groupProductsByTwo(List<Product> products) {
        List<List<Product>> pairs = new ArrayList<>();
        for (int i = 0; i < products.size(); i += 2) {
            List<Product> pair = new ArrayList<>();
            pair.add(products.get(i));
            if (i + 1 < products.size()) {
                pair.add(products.get(i + 1));
            }
            pairs.add(pair);
        }
        return pairs;
    }

    private void getLocation() {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Reverse geocoding: Convert latitude and longitude to a human-readable address
                                Geocoder geocoder = new Geocoder(homepage.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            1 // Get only one address result
                                    );

                                    if (addresses != null && !addresses.isEmpty()) {
                                        // Get the first address and display it
                                        Address address = addresses.get(0);
                                        String areaName = address.getSubLocality();
                                        String addressLine = address.getAddressLine(0);
                                        String zipCode = address.getPostalCode();

                                        if (addressLine != null && addressLine.length() > MAX_ADDRESS_LENGTH) {
                                            addressLine = addressLine.substring(0, MAX_ADDRESS_LENGTH);
                                        }

                                        if (areaName != null && zipCode != null) {
                                            location_text.setText(addressLine +  "...,\n" + zipCode);
                                        } else {
                                            addressLine = addressLine.substring(0, MAX_ADDRESS_LENGTH);
                                            location_text.setText(addressLine + "...,\n" + "380001");
                                        }
                                    } else {
                                        location_text.setText("Address not found.");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    location_text.setText("Geocoder service is unavailable.");
                                }
                            } else {
                                location_text.setText("Location not available.");
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                getLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Cannot access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteDatabase("ProductDB");
    }

    @Override
    protected void onStop() {
        super.onStop();
        deleteDatabase("ProductDB");
    }
}