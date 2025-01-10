package com.example.mytest.repository;

import com.example.mytest.model.Room;
import com.example.mytest.model.Student;
import com.example.mytest.model.Test;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoomRepository {
    private final CollectionReference testCollection;
    private final TestRepository testRepository;

    public RoomRepository(FirebaseFirestore db){
        testCollection = db.collection("room");
        testRepository = new TestRepository(db);
    }

    public Room addRoom(Room room) {
        String testId = testCollection.document().getId();
        room.setId(testId);
        room.setRoomNumber(testId.substring(0, 5));
        testCollection.document(testId).set(room);
        return room;
    }

    public void deleteRoom(Room room) {
        testCollection.document(room.getId()).delete();
    }

    public void updateRoom(Room room) {
        testCollection.document(room.getId()).set(room);
    }
    public CompletableFuture<List<Room>> getAllRoom() {
        CompletableFuture<List<Room>> future = new CompletableFuture<>();
        List<Room> testList = new ArrayList<>();

        testCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Room room = document.toObject(Room.class);
                testList.add(room);
            }
            future.complete(testList);
        });

        return future;
    }
    public CompletableFuture<Test> getTestByRoomNumber(String roomNumber) {
        CompletableFuture<Test>  future = new CompletableFuture<>();
        testCollection.whereEqualTo("roomNumber", roomNumber).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Room room = document.toObject(Room.class);
                testRepository.getById(room.getTestId())
                        .thenAccept(future::complete);
            }
        });
        return future;
    }
    public CompletableFuture<List<Room>> getAllRoomsByTestId(String testId) {
        CompletableFuture<List<Room>> future = new CompletableFuture<>();
        List<Room> roomList = new ArrayList<>();

        testCollection.whereEqualTo("testId", testId).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Room room = document.toObject(Room.class);
                roomList.add(room);
            }
            future.complete(roomList);
        });

        return future;
    }
}
