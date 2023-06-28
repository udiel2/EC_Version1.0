package com.application.easycook.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.application.easycook.MyPantryPackage.PantryProduct;
import com.application.easycook.Product;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.application.easycook.RecipesPackage.Recipes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private Context context;
    private SQLiteDatabase database;
    private PantryDatabaseHelper pantryDatabaseHelper;
    private ProductDatabaseHelper productDatabaseHelper;
    private RecipesDatabaseHelper recipesDatabaseHelper;
    private RecipeDetailsDatabaseHelper recipeDetailsDatabaseHelper;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public DatabaseManager(Context context) {

        this.context = context;

    }

    public void open() {
        productDatabaseHelper = new ProductDatabaseHelper(context);
        pantryDatabaseHelper =new PantryDatabaseHelper(context);
        recipesDatabaseHelper=new RecipesDatabaseHelper(context);
        recipeDetailsDatabaseHelper=new RecipeDetailsDatabaseHelper(context);
//        recipeDetailsDatabaseHelper.syncWithFirestore();
        database = productDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        recipesDatabaseHelper.close();
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
    public ArrayList<RecipeTitle> getAllRecipeTitles(){
        return recipesDatabaseHelper.getAllRecipeTitles();
    }
    public ArrayList<Product> getProductByName(String text){
        return productDatabaseHelper.getProductByName(text);
    }
    public ArrayList<PantryProduct> getPantry(){
        ArrayList<PantryProduct> pantry=pantryDatabaseHelper.getPantry();
        for (PantryProduct product: pantry){
            product.setProduct(productDatabaseHelper.getProductById(product.getProduct_id()));
        }
        return pantry;
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
    public void syncProductsFromFirebase(){
        productDatabaseHelper.syncWithFirestore();
    }
    public void updatePantryProduct(PantryProduct pantryProduct){
        pantryDatabaseHelper.updatePantryProduct(pantryProduct);
    }
    public void syncRecipesFromFirebase(){
        recipesDatabaseHelper.syncWithFirestore();
    }


    public Recipes getRecipeById(String id){
        return recipeDetailsDatabaseHelper.getRecipeById(id);
    }
    public Recipes getRecipe(String id){
        DocumentReference ref=firestore.collection("Recipes_details").document(id);
        ArrayList<Recipes> recipes=new ArrayList<>();

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        document.getData();
                        recipes.add(document.toObject(Recipes.class));
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        if (recipes.isEmpty())
            return null;
        else
            return recipes.get(0);
    }


    public RecipeTitle getRecipesTitleById(String id){
        return recipesDatabaseHelper.getRecipeTitleById(id);
    }

    public ArrayList<RecipeTitle> getRecipesTitleByName(String name){
        ArrayList<Recipes> recipes=recipeDetailsDatabaseHelper.getRecipesByName(name);
        ArrayList<RecipeTitle> recipeTitles=new ArrayList<>();
        for(Recipes recipe:recipes){
            RecipeTitle recipeTitle =recipesDatabaseHelper.getRecipeTitleById(recipe.getId());
            recipeTitles.add(recipeTitle);
        }
        return recipeTitles;

    }


}