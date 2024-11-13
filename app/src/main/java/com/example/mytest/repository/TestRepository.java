package com.example.mytest.repository;

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

public class TestRepository {
    private final CollectionReference testCollection;

    public TestRepository(FirebaseFirestore db){
        testCollection = db.collection("tests");
    }

    public Test addTest(Test test) {
        String testId = testCollection.document().getId();
        test.setId(testId);
        test.setTimestamp(Timestamp.now());
        testCollection.document(testId).set(test);
        return test;
    }

    public void deleteTest(Test test) {
        testCollection.document(test.getId()).delete();
    }

    public void updateTest(Test test) {
        testCollection.document(test.getId()).set(test);
    }
    public CompletableFuture<List<Test>> getAllTest() {
        CompletableFuture<List<Test>> future = new CompletableFuture<>();
        List<Test> testList = new ArrayList<>();

        testCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Test test = document.toObject(Test.class);
                testList.add(test);
            }
            future.complete(testList);
        });

        return future;
    }

    public CompletableFuture<List<Test>> getAllTestByTeacherId(String id) {
        CompletableFuture<List<Test>> future = new CompletableFuture<>();
        List<Test> testList = new ArrayList<>();

        testCollection.whereEqualTo("teacherId", id).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Test test = document.toObject(Test.class);
                testList.add(test);
            }
            testList.sort(Comparator.comparing(Test::getTimestamp));
            future.complete(testList);
        });

        return future;
    }
    public CompletableFuture<List<Test>> getAllTestByStudentId(String id) {
        CompletableFuture<List<Test>> future = new CompletableFuture<>();
        List<Test> testList = new ArrayList<>();

        testCollection.whereEqualTo("studentId", id).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Test test = document.toObject(Test.class);
                testList.add(test);
            }
            testList.sort(Comparator.comparing(Test::getTimestamp));
            future.complete(testList);
        });

        return future;
    }

    public CompletableFuture<List<Test>> getAllStudentTest() {
        CompletableFuture<List<Test>> future = new CompletableFuture<>();
        List<Test> testList = new ArrayList<>();

        testCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Test test = document.toObject(Test.class);
                if (test.getTeacherId() == null || test.getTeacherId().equals("")) {
                    testList.add(test);
                }
            }
            testList.sort(Comparator.comparing(Test::getTimestamp));
            future.complete(testList);
        });

        return future;
    }

    public CompletableFuture<Test> getById(String testId) {
        CompletableFuture<Test> future = new CompletableFuture<>();

        testCollection.whereEqualTo("id", testId).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Test test = document.toObject(Test.class);
                future.complete(test);
            }
        });

        return future;
    }
}
