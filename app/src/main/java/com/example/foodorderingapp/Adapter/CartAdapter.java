package com.example.foodorderingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.R;
import com.example.foodorderingapp.homepage.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final List<Product> cartItems;
    private final CartTotalListener totalListener;

    public CartAdapter(List<Product> cartItems, CartTotalListener totalListener) {
        this.cartItems = cartItems;
        this.totalListener = totalListener;
        notifyCartTotal(); // Initial total
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, priceText, txtQuantity, btnIncrease, btnDecrease, cartItemPriceTotal;
        ImageView ItemImage;

        public ViewHolder(View view) {
            super(view);
            ItemImage  = view.findViewById(R.id.cartItemImage);
            nameText = view.findViewById(R.id.cartItemName);
            priceText = view.findViewById(R.id.cartItemPrice);
            txtQuantity = view.findViewById(R.id.txtQuantity);
            btnIncrease = view.findViewById(R.id.btnIncrease);
            btnDecrease = view.findViewById(R.id.btnDecrease);
            cartItemPriceTotal = view.findViewById(R.id.cartItemPriceTotal);
        }
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.ItemImage.setImageResource(Integer.parseInt(product.image));
        holder.nameText.setText(product.name);
        holder.priceText.setText("₹" + product.price);
        holder.txtQuantity.setText(String.valueOf(product.quantity));
        holder.cartItemPriceTotal.setText("₹" + (product.price * product.quantity));

        holder.btnIncrease.setOnClickListener(v -> {
            product.quantity++;
            holder.txtQuantity.setText(String.valueOf(product.quantity));
            holder.cartItemPriceTotal.setText("₹" + (product.price * product.quantity));
            Toast.makeText(v.getContext(), product.name + " increased", Toast.LENGTH_SHORT).show();
            notifyCartTotal();
            notifyItemChanged(position);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (product.quantity > 1) {
                product.quantity--;
                holder.txtQuantity.setText(String.valueOf(product.quantity));
                holder.cartItemPriceTotal.setText("₹" + (product.price * product.quantity));
                Toast.makeText(v.getContext(), product.name + " decreased", Toast.LENGTH_SHORT).show();
                notifyCartTotal();
                notifyItemChanged(position);
            } else {
                cartItems.remove(position);
                Toast.makeText(v.getContext(), product.name + " removed from cart", Toast.LENGTH_SHORT).show();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
                notifyCartTotal();
            }
        });
    }

    private void notifyCartTotal() {
        double subtotal = 0;
        for (Product p : cartItems) {
            subtotal += p.price * p.quantity;
        }

        double gst = subtotal * 0.09; // 9% GST
        double deliveryFee = subtotal > 0 ? 50 : 0; // ₹50 delivery only if cart has items
        double total = subtotal + gst + deliveryFee;

        totalListener.onCartTotalUpdated(subtotal, gst, deliveryFee, total);
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
