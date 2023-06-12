package com.application.easycook.MyPantryPackage;

import com.application.easycook.Product;

import java.util.Date;

public class PantryProduct {
    private String product_id;
    private String id;

    private String category;
    private String amount_type;
    private int amount;
    private Date entry_time;
    private Product product;

    public PantryProduct(String id,String product_id, String category, String amount_type, int amount, Date date) {
        this.id=id;
        this.product_id = product_id;
        this.category = category;
        this.amount_type = amount_type;
        this.amount = amount;
        this.entry_time = date;
    }
    public PantryProduct(String id,String product_id, String category, String amount_type, int amount, Date date,Product product) {
        this.product=product;
        this.id=id;
        this.product_id = product_id;
        this.category = category;
        this.amount_type = amount_type;
        this.amount = amount;
        this.entry_time = date;
    }
    public PantryProduct() {
        // קונסטרקטור ללא פרמטרים
    }

    PantryProduct(Product product){

    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount_type() {
        return amount_type;
    }

    public void setAmount_type(String amount_type) {
        this.amount_type = amount_type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getDate() {
        return this.entry_time;
    }

    public void setDate(Date date) {
        this.entry_time = date;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


}
