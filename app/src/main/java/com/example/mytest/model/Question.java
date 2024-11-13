package com.example.mytest.model;

import java.io.Serializable;

public class Question implements Serializable {
    private String id;
    private String title;
    private String type;
    private String photo;
    private String testId;


    public Question(String id, String title, String type, String photo, String testId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.photo = photo;
        this.testId = testId;
    }

    public Question() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }
}
