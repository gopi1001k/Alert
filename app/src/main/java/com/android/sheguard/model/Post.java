package com.android.sheguard.model;

public class Post {
    private String imageUrl;
    private String text;

    public Post() {
        // Default constructor required for Firebase
    }

    public String getProductImage() {
        return imageUrl;
    }

    public void setProductImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return text;
    }

    public void setDescription(String text) {
        this.text = text;
    }

    public Post(String imageUrl, String text) {
        this.imageUrl = imageUrl;
        this.text = text;
    }
}
