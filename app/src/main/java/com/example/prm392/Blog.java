package com.example.prm392;

import java.io.Serializable;

public class Blog implements Serializable {
    private String id;
    private String title;
    private String desc;
    private String author;
    private String date;
    private String img;
    private String shareCount;
    private String timestamp;

    public Blog() {
    }

    public Blog(String id, String title, String desc, String author, String date, String img, String shareCount, String timestamp) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.date = date;
        this.img = img;
        this.shareCount = shareCount;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }
}
