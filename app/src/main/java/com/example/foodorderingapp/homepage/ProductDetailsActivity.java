package com.example.foodorderingapp.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderingapp.Helper.CartManager;
import com.example.foodorderingapp.R;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, productBrand, productRating,productDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productBrand = findViewById(R.id.productBrand);
        productRating = findViewById(R.id.productRating);
        productDetail = findViewById(R.id.productDetail);

        // Get data from intent
        Bundle bundle = getIntent().getExtras();

        int productId = 0;
        String name = bundle.getString("name");
        double price = bundle.getDouble("price");
        String brand = bundle.getString("brand");
        float rating = bundle.getFloat("rating");
        String detail = bundle.getString("detail");
        String image = bundle.getString("image");
        if (bundle != null) {
            productId = bundle.getInt("id");
            productImage.setImageResource(Integer.parseInt(bundle.getString("image")));
            productName.setText(bundle.getString("name"));
            productPrice.setText("₹" + bundle.getDouble("price"));
            productBrand.setText(bundle.getString("brand"));
            productRating.setText("Rating: " + bundle.getFloat("rating"));
            productDetail.setText(bundle.getString("detail"));

        }
        Product product = new Product(productId, name, price, brand, rating, detail, image);

        Button buyNowBtn = findViewById(R.id.buy_now_button);
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add product to cart
                CartManager.addToCart(product);

                // Open cart activity
                Intent intent = new Intent(ProductDetailsActivity.this, cart.class);
                startActivity(intent);
            }
        });


        getWindow().setStatusBarColor(getResources().getColor(android.R.color.black));
    }
}
