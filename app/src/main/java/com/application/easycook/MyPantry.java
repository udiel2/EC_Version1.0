package com.application.easycook;

import java.util.ArrayList;
import java.util.List;

public class MyPantry {
    private List<Product> productList;

    public MyPantry() {
        this.productList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        productList.add(product);
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
