package com.application.easycook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.application.easycook.database.ProductDatabaseHelper;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private ProductDatabaseHelper productDatabaseHelper;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public void open() {
        productDatabaseHelper = new ProductDatabaseHelper(context);
//        dbHelper = new DatabaseHelper(context);
        database = productDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        productDatabaseHelper.close();
    }

    public void insertProduct(String name, double price) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        long result = database.insert("products", null, values);
        if (result == -1) {
            Log.e(TAG, "Failed to insert product into database");
        } else {
            Log.d(TAG, "Product inserted successfully");
        }
    }

    public Cursor getAllProducts() {
        return database.query("products", null, null, null, null, null, null);
    }
}
