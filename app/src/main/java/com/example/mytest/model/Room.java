package com.example.mytest.model;

public class Room {
    private String id;
    private String testId;
    private String roomNumber;

    public Room(String id, String testId, String roomNumber) {
        this.id = id;
        this.testId = testId;
        this.roomNumber = roomNumber;
    }

    public Room() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}


