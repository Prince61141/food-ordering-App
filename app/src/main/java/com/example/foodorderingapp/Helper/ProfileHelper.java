package com.example.foodorderingapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "UserData.db";
    public static final String TABLE_NAME = "UserProfile";
    public static final int DB_VERSION = 1;

    public ProfileHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                "email TEXT PRIMARY KEY," +
                "name TEXT," +
                "phone TEXT," +
                "username TEXT," +
                "address TEXT)";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert or update user data
    public void upsertUser(String email, String name, String phone, String username, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("name", name);
        values.put("phone", phone);
        values.put("username", username);
        values.put("address", address);

        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // Get user profile by email
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email = ?", new String[]{email});
    }
}
