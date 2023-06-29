package com.example.prm392;

import java.io.Serializable;

public class Food implements Serializable {
    private String image;
    private String name;
    private String description;
    private int categoryId;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Food() {
    }

    public Food(String image, String name, String description, int categoryId) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
    }
}
