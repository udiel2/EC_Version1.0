package com.application.easycook;

import android.widget.ImageView;

public class ParseProduct {
    private String imgUrl;
    private String title;

    public void setIs_chked(boolean is_chked) {
        this.is_chked = is_chked;
    }

    public boolean is_chked=false;


    ImageView imageView=null;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public ParseProduct() {
    }

    public ParseProduct(String imgUrl, String title) {
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
