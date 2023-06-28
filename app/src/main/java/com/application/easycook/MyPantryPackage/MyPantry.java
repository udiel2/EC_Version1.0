package com.application.easycook.MyPantryPackage;


import android.content.Context;

import com.application.easycook.Product;
import com.application.easycook.RecipesPackage.MentorCookPackage.RecipeTitle;
import com.application.easycook.RecipesPackage.Recipes;
import com.application.easycook.database.DatabaseManager;
import com.application.easycook.database.PantryDatabaseHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class MyPantry {

    public ArrayList<PantryProduct> myPantry;
//    public ArrayList<Product> productList;
    FirebaseStorage db = FirebaseStorage.getInstance();
    PantryDatabaseHelper pantryDatabaseHelper;
    DatabaseManager databaseManager;
    StorageReference storageRef;

//    public ArrayList<Product> getProductList() {
//        return productList;
//    }
    public ArrayList<PantryProduct> getPantryList(){return myPantry;}
    public ArrayList<RecipeTitle> getRecipeTitleList(){return databaseManager.getAllRecipeTitles();}


    public MyPantry(Context context)
    {
//        productList = new ArrayList<>();
        myPantry =new ArrayList<>();
        storageRef= FirebaseStorage.getInstance().getReference();
        databaseManager=new DatabaseManager(context);
        stratMyPantry();
        StorageReference pathReference = storageRef.child("EZ_icons/cola.png");


    }
    public ArrayList<Product> getPantryProducts(){
        ArrayList<Product> products=new ArrayList<>();
        for(PantryProduct pantryProduct:myPantry){
            products.add(pantryProduct.getProduct());
        }
        return products;
    }

    public void addProduct(Product product) {
        String product_id=product.getId();
        String category=product.getCategory();
        Date date=new Date();
        String unit_w=product.getUnit_weight();
        PantryProduct pantryProduct=new PantryProduct(null,product_id,category,unit_w,100,date,product);
        databaseManager.open();
        pantryProduct.setId(databaseManager.addPantryProduct(pantryProduct));
        databaseManager.close();
        myPantry.add(pantryProduct);
//        productList.add(product);
    }
//    public void addProduct(ParseProduct product) {
//        Product temp=new Product(product.getTitle(),product.getTitle(),"test");
//        productList.add(temp);
//    }

    public boolean removePantryProduct(String id) {
        for(int i = 0; i< myPantry.size(); i++){
            if(myPantry.get(i).getId()==id){
                myPantry.remove(i);
            }
        }
        boolean del=databaseManager.deletePantryProduct(id);
        return del;

    }
    public ArrayList<Product> getProductsByName(String name) {
        return databaseManager.getProductByName(name);
    }

    public Product getProductByName(String name) {
        for (PantryProduct pantryProduct : myPantry) {
            if (pantryProduct.getProduct().getName().equals(name)) {
                return pantryProduct.getProduct();
            }
        }
        return null;
    }
    private void stratMyPantry(){
        databaseManager.open();
        myPantry =databaseManager.getPantry();
//        for (PantryProduct product: myPantry){
//            productList.add(databaseManager.getProductByID(product.getProduct_id()));
//        }
        databaseManager.close();
    }
    public void updatePantryProduct(PantryProduct p){
        databaseManager.updatePantryProduct(p);
    }
    public  void syncProductsFirebase(){
        databaseManager.syncProductsFromFirebase();
    }
    public  void syncRecipesFirebase(){
        databaseManager.syncRecipesFromFirebase();
    }

    public Recipes getRecipeByid(String id){
        return databaseManager.getRecipeById(id);
    }

    public Product getProductByid(String id){return databaseManager.getProductByID(id);}

    public ArrayList<RecipeTitle> getRecipeTitlesByName(String name){
        return databaseManager.getRecipesTitleByName(name);
    }
    public RecipeTitle getRecipeTitleById(String id){
        return getRecipeTitleById(id);
    }
    public ArrayList<Product> getAllproducts(){
        return databaseManager.getAllProducts();
    }

}
