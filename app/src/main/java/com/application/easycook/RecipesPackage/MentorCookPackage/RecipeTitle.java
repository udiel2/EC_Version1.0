package com.application.easycook.RecipesPackage.MentorCookPackage;

import java.util.ArrayList;

public class RecipeTitle {
    private String title;
    private String id;
    private String description;
    private String link;
    private ArrayList<String> imagPath;
    private String auther;
    private ArrayList<String> imgAuth;

    @Override
    public String toString() {
        return "\nRecipeTitle{" +
                "\nID='" + id + '\'' +
                "\ntitle='" + title + '\'' +
                "\n description='" + description + '\'' +
                "\n link='" + link + '\'' +
                "\n imagPath=" + imagPath +
                "\n auther='" + auther + '\'' +
                "\n imgAuth=" + imgAuth +
                '}';
    }
    public RecipeTitle(){

    }

    public RecipeTitle(String title, String id, String description, String link, ArrayList<String> imagPath, String auther, ArrayList<String> imgAuth) {
        this.title = title;
        this.id = id;
        this.description = description;
        this.link = link;
        this.imagPath = imagPath;
        this.auther = auther;
        this.imgAuth = imgAuth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<String> getImagPath() {
        return imagPath;
    }

    public void setImagPath(ArrayList<String> imagPath) {
        this.imagPath = imagPath;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public ArrayList<String> getImgAuth() {
        return imgAuth;
    }

    public void setImgAuth(ArrayList<String> imgAuth) {
        this.imgAuth = imgAuth;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
