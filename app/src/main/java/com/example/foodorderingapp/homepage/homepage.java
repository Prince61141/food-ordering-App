package com.example.foodorderingapp.homepage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorderingapp.Adapter.BannerAdapter;
import com.example.foodorderingapp.Adapter.ProductAdapter;
import com.example.foodorderingapp.Helper.ProductDatabaseHelper;
import com.example.foodorderingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class homepage extends AppCompatActivity {

    private LinearLayout searchItem, accountLayout, cartlayout;
    private int currentItem = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private EditText searchBar;

    RecyclerView recyclerView;
    ProductDatabaseHelper dbHelper;
    ProductAdapter adapter;
    List<List<Product>> groupedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        searchItem = findViewById(R.id.search_item);
        searchBar = findViewById(R.id.search_bar);

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

        dbHelper = new ProductDatabaseHelper(this);

        // Insert sample products only once
        dbHelper.insertProduct("Cheese And Corn Pizza", 244, "Puffizza", 4);
        dbHelper.insertProduct("Mexican Green Wave", 279, "Domino's", 4);
        dbHelper.insertProduct("Farmhouse", 299, "Pizza Hut", 4);
        dbHelper.insertProduct("Farmhouse", 295, "Pizza Hut", 5);

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