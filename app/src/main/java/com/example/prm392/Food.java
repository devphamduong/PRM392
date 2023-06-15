package com.example.prmproject;

public class Food {
    public int image;
    public String name;
    public String description;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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

    public Food() {
    }

    public Food(int image, String name, String description) {
        this.image = image;
        this.name = name;
        this.description = description;
    }
}
