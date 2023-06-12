package com.application.easycook;

import android.graphics.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Product {
    private String name;
    private String id=null;
    private String unit_type;

    private String unit_weight="";
    private String unit_brand="";

    private ArrayList<String>imagePath;
    private String bar_cod;
    private Point life_time;
    private String category;


    private String price;
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.name);
        jsonObject.put("imagePath", this.imagePath);
        jsonObject.put("category", this.category);
        return jsonObject;
    }

    public Product(String name, String imagePath, String id) {
        this.name = name;
        this.imagePath.add(imagePath);
        this.unit_type = id;
    }

    public Product(String name, ArrayList<String> imagePath,  String bar_cod, Point life_time, String category,String w_methud1,String price,String unit_weight,String unit_brand) {
        this.name = name;
        this.imagePath=imagePath;
        this.unit_type=w_methud1;
        this.bar_cod = bar_cod;
        this.life_time = life_time;
        this.category = category;
        this.price = price;
        this.unit_weight=unit_weight;
        this.unit_brand=unit_brand;

    }
    public Product(String id,String name, ArrayList<String> imagePath,  String bar_cod, Point life_time, String category,String w_methud1,String price,String unit_weight,String unit_brand) {
        this.id=id;
        this.name = name;
        this.imagePath=imagePath;
        this.unit_type=w_methud1;
        this.bar_cod = bar_cod;
        this.life_time = life_time;
        this.category = category;
        this.price = price;
        this.unit_weight=unit_weight;
        this.unit_brand=unit_brand;

    }
    public Product(){

    }
    public Product(Product p){
        this.name = p.getName();
        this.imagePath=p.getImagePath();
        this.unit_type=p.getUnit_type();
        this.bar_cod = p.getBar_cod();
        this.life_time = p.getLife_time();
        this.category =p.getCategory();
        this.price = p.getPrice();
        this.unit_weight=p.getUnit_weight();

    }
    public Product(String name, String imagePath, String id, Date exportTime, Date getInTime, String category) {
        this.name = name;
        this.imagePath.add(imagePath);
        this.unit_type = id;

        this.category = category;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getUnit_type() {
        return unit_type;
    }
    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }

    public void setUnit_type(String unit_type) {
        this.unit_type = unit_type;
    }

    public String getUnit_weight() {
        return unit_weight;
    }

    public void setUnit_weight(String unit_weight) {
        this.unit_weight = unit_weight;
    }

    public String getName() {
        return name;
    }



    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void showProduct(){
        System.out.println("\n\nProduct: "+this.name+"\nID: "+this.id +"\nimag: "+this.imagePath+"\nBarCod: "
                +this.bar_cod+"\nCategory: "+this.category+"\nMethud: "+this.unit_type+"\nPrice: "
                +this.price+"\nUnit W: "+this.unit_weight+"\nBrand: "+this.unit_brand);
    }
    public Point getLife_time() {
        return life_time;
    }
    public String getBar_cod() {
        return bar_cod;
    }
    public void setBar_cod(String bar_cod) {
        this.bar_cod = bar_cod;
    }

    public void setLife_time(Point life_time) {
        this.life_time = life_time;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit_brand() {
        return unit_brand;
    }

    public void setUnit_brand(String unit_brand) {
        this.unit_brand = unit_brand;
    }
}
