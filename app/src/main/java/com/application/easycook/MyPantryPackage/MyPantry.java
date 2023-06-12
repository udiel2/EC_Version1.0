package com.application.easycook.MyPantryPackage;


import android.content.Context;

import com.application.easycook.Product;
import com.application.easycook.database.DatabaseManager;
import com.application.easycook.database.PantryDatabaseHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class MyPantry {

    public ArrayList<PantryProduct> products;
    public ArrayList<Product> productList;
    FirebaseStorage db = FirebaseStorage.getInstance();
    PantryDatabaseHelper pantryDatabaseHelper;
    DatabaseManager databaseManager;
    StorageReference storageRef;

    public ArrayList<Product> getProductList() {
        return productList;
    }
    public ArrayList<PantryProduct> getPantryList(){return  products;}

    public MyPantry(Context context)
    {
        productList = new ArrayList<>();
        products=new ArrayList<>();
        storageRef= FirebaseStorage.getInstance().getReference();
        databaseManager=new DatabaseManager(context);
        stratMyPantry();
        StorageReference pathReference = storageRef.child("EZ_icons/cola.png");


    }

    public void addProduct(Product product) {
        String product_id=product.getId();
        String category=product.getCategory();
        Date date=new Date();
        String unit_w=product.getUnit_weight();
        PantryProduct pantryProduct=new PantryProduct(null,product_id,category,unit_w,100,date);
        databaseManager.open();
        pantryProduct.setId(databaseManager.addPantryProduct(pantryProduct));
        databaseManager.close();
        products.add(pantryProduct);
        productList.add(product);
    }
//    public void addProduct(ParseProduct product) {
//        Product temp=new Product(product.getTitle(),product.getTitle(),"test");
//        productList.add(temp);
//    }

    public boolean removePantryProduct(String id) {
        for(int i=0;i<products.size();i++){
            if(products.get(i).getId()==id){
                products.remove(i);
                productList.remove(i);
            }
        }
        boolean del=databaseManager.deletePantryProduct(id);
        return del;

    }

    public Product getProductByName(String name) {
        for (Product product : productList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
    private void stratMyPantry(){
        databaseManager.open();
        products=databaseManager.getPantry();
        for (PantryProduct product:products){
            productList.add(databaseManager.getProductByID(product.getProduct_id()));
        }
        databaseManager.close();
    }
    public void updatePantryProduct(PantryProduct p){
        databaseManager.updatePantryProduct(p);
    }
}
