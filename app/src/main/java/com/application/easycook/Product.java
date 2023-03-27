package com.application.easycook;

import java.util.Date;

public class Product {
    private String name;
    private String imageName;
    private String id;
    private Date exportTime;
    private Date getInTime;
    private String category;

    public Product(String name, String imageName, String id, Date exportTime, Date getInTime, String category) {
        this.name = name;
        this.imageName = imageName;
        this.id = id;
        this.exportTime = exportTime;
        this.getInTime = getInTime;
        this.category = category;
    }

    public Product(String name, String imageName, String id) {
        this.name = name;
        this.imageName = imageName;
        this.id = id;
        this.category=null;
        this.exportTime=null;
        this.getInTime=null;

    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public String getId() {
        return id;
    }

    public Date getExportTime() {
        return exportTime;
    }

    public Date getGetInTime() {
        return getInTime;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExportTime(Date exportTime) {
        this.exportTime = exportTime;
    }

    public void setGetInTime(Date getInTime) {
        this.getInTime = getInTime;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
