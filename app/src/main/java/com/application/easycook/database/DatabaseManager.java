package com.application.easycook.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.application.easycook.DatabaseHelper;
import com.application.easycook.MyPantryPackage.PantryProduct;
import com.application.easycook.Product;

import java.util.ArrayList;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private PantryDatabaseHelper pantryDatabaseHelper;
    private ProductDatabaseHelper productDatabaseHelper;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public void open() {
        productDatabaseHelper = new ProductDatabaseHelper(context);
        pantryDatabaseHelper =new PantryDatabaseHelper(context);
//        dbHelper = new DatabaseHelper(context);
        database = productDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        pantryDatabaseHelper.close();
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

//    public Cursor getAllProducts() {
//        return database.query("products", null, null, null, null, null, null);
//    }
    public ArrayList<Product> getAllProducts(){
        return productDatabaseHelper.getAllProducts();
    }
    public ArrayList<Product> getProductByName(String text){
        return productDatabaseHelper.getProductByName(text);
    }
    public ArrayList<PantryProduct> getPantry(){
        return pantryDatabaseHelper.getPantry();
    }
    public String addPantryProduct(PantryProduct product){
        return  pantryDatabaseHelper.addProduct(product);
    }
    public Product getProductByID(String id){
        return productDatabaseHelper.getProductById(id);
    }
    public void DeletPantryProduct(String id){

    }
    public boolean deletePantryProduct(String id){
        boolean del=pantryDatabaseHelper.deleteProduct(id);
        return del;
    }
    public void updatePantryProduct(PantryProduct pantryProduct){
        pantryDatabaseHelper.updatePantryProduct(pantryProduct);
    }

}