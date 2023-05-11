package com.application.easycook;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyPantry {
    private List<Product> productList;
    FirebaseStorage db = FirebaseStorage.getInstance();
    StorageReference storageRef;

    public MyPantry()
    {
        this.productList = new ArrayList<>();
        storageRef= FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("EZ_icons/cola.png");


    }

    public void addProduct(Product product) {
        productList.add(product);
    }
    public void addProduct(ParseProduct product) {

        Product temp=new Product(product.getTitle(),product.getImgUrl(),"test");
    }

    public void removeProduct(Product product) {
        productList.remove(product);
    }

    public Product getProductByName(String name) {
        for (Product product : productList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
}
