package com.example.mytest.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Test implements Serializable {
    private String teacherId;
    private String title;
    private transient Timestamp timestamp;
    private String studentId;

    private String id;

    public Test() {
    }

    public Test(String teacherId, String title, Timestamp timestamp, String studentId, String id) {
        this.teacherId = teacherId;
        this.title = title;
        this.timestamp = timestamp;
        this.studentId = studentId;
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}
