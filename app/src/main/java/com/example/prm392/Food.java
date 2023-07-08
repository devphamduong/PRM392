package com.example.prm392;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String image;
    private String name;
    private float calories;
    private float carbs;
    private float fat;
    private float protein;
    private String description;
    private int categoryId;
    private boolean enabled = true;

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

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Food() {
    }

    public Food(String id, String image, String name, float calories, float carbs, float fat, float protein, String description, int categoryId) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.description = description;
        this.categoryId = categoryId;
        this.enabled = true;
    }
}
