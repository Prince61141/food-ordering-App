package com.example.foodorderingapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodorderingapp.homepage.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    public ProductDatabaseHelper(Context context) {
        super(context, "ProductDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, brand TEXT, rating REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        onCreate(db);
    }

    public boolean insertProduct(String name, double price, String brand, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("brand", brand);
        values.put("rating", rating);
        long result = db.insert("products", null, values);
        return result != -1;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        while (cursor.moveToNext()) {
            list.add(new Product(
                    cursor.getInt(0),        // id
                    cursor.getString(1),     // name
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getFloat(4)
            ));
        }

        cursor.close();
        return list;
    }
    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM products");
    }
}
