package com.example.prm392;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String image;
    private String name;
    private String description;
    private int categoryId;
    private boolean isEnabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Food() {
    }

    public Food(String id, String image, String name, String description, int categoryId) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.isEnabled = true;
    }
}
