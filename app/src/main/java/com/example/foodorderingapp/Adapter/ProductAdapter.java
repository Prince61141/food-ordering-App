package com.example.foodorderingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.foodorderingapp.Helper.CartManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.R;
import com.example.foodorderingapp.homepage.Product;
import com.example.foodorderingapp.homepage.ProductDetailsActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final List<List<Product>> productPairs;

    public ProductAdapter(List<List<Product>> productPairs) {
        this.productPairs = productPairs;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView add_to_cart1,product1Name, product1Price,product1Brand, product1rating, add_to_cart2, product2Name, product2Price,product2Brand,product2rating;
        ImageView product1image, product2image;
        public ViewHolder(View view) {
            super(view);
            add_to_cart1 = view.findViewById(R.id.add_to_cart1);
            product1Name = view.findViewById(R.id.product1Name);
            product1Price = view.findViewById(R.id.product1Price);
            product1Brand = view.findViewById(R.id.product1Brand);
            product1rating = view.findViewById(R.id.product1rating);
            product1image = view.findViewById(R.id.product1image);

            add_to_cart2 = view.findViewById(R.id.add_to_cart2);
            product2Name = view.findViewById(R.id.product2Name);
            product2Price = view.findViewById(R.id.product2Price);
            product2Brand = view.findViewById(R.id.product2Brand);
            product2rating = view.findViewById(R.id.product2rating);
            product2image = view.findViewById(R.id.product2image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<Product> pair = productPairs.get(position);

        Product p1 = pair.get(0);
        holder.add_to_cart1.setId(p1.id);
        holder.product1Name.setText(p1.name);
        holder.product1Price.setText("₹" + p1.price);
        holder.product1Brand.setText(p1.brand);
        holder.product1rating.setText(" " + p1.rating);
        holder.product1image.setImageResource(Integer.parseInt(p1.image));

        holder.product1image.setOnClickListener(v -> openProductDetails(v.getContext(), p1));
        holder.product1Name.setOnClickListener(v -> openProductDetails(v.getContext(), p1));

        holder.add_to_cart1.setOnClickListener(v -> {
            CartManager.addToCart(p1);
            Toast.makeText(v.getContext(), p1.name + " added to cart", Toast.LENGTH_SHORT).show();
        });

        if (pair.size() > 1) {
            Product p2 = pair.get(1);
            holder.add_to_cart1.setId(p2.id);
            holder.product2Name.setText(p2.name);
            holder.product2Price.setText("₹" + p2.price);
            holder.product2Brand.setText(p2.brand);
            holder.product2rating.setText(" " + p2.rating);
            holder.product2image.setImageResource(Integer.parseInt(p2.image));

            holder.product2image.setOnClickListener(v -> openProductDetails(v.getContext(), p2));
            holder.product2Name.setOnClickListener(v -> openProductDetails(v.getContext(), p2));

            holder.add_to_cart2.setOnClickListener(v -> {
                CartManager.addToCart(p2);
                Toast.makeText(v.getContext(), p2.name + " added to cart", Toast.LENGTH_SHORT).show();
            });

        } else {
            holder.product2Name.setText("");
            holder.product2Price.setText("");
            holder.product2Brand.setText("");
            holder.product2rating.setText("");
            holder.product2image.setImageDrawable(null);
        }
    }

    private void openProductDetails(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("id",product.id);
        intent.putExtra("name", product.name);
        intent.putExtra("price", product.price);
        intent.putExtra("brand", product.brand);
        intent.putExtra("rating", product.rating);
        intent.putExtra("image", product.image); // send image resource ID
        intent.putExtra("detail",product.detail);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return productPairs.size();
    }
}
