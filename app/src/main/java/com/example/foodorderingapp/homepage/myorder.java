package com.example.foodorderingapp.homepage;

import static com.example.foodorderingapp.homepage.profile.KEY_LANGUAGE;
import static com.example.foodorderingapp.homepage.profile.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.Adapter.OrderAdapter;
import com.example.foodorderingapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class myorder extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderAdapter adapter;
    List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder);

        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));

        recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("OrderPrefs", MODE_PRIVATE);
        String ordersJson = prefs.getString("order_list", "[]");

        try {
            JSONArray orderArray = new JSONArray(ordersJson);
            for (int i = 0; i < orderArray.length(); i++) {
                JSONObject obj = orderArray.getJSONObject(i);
                String id = obj.getString("orderId");
                String time = obj.getString("dateTime");

                orderList.add(new Order(id, time));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);

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

    public void goBackToHomepage(View view) {
        Intent intent = new Intent(myorder.this, homepage.class);
        startActivity(intent);
        finish();
    }
}
