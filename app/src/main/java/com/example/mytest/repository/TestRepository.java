package com.example.mytest.repository;

import androidx.annotation.NonNull;

import com.example.mytest.model.Answer;
import com.example.mytest.model.Question;
import com.example.mytest.model.Result;
import com.example.mytest.model.Room;
import com.example.mytest.model.Student;
import com.example.mytest.model.Test;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRepository {
    private final CollectionReference testCollection;
    private final FirebaseFirestore db;

    public TestRepository(FirebaseFirestore db){
        testCollection = db.collection("tests");
        this.db = db;
    }

    public Test addTest(Test test) {
        String testId = testCollection.document().getId();
        test.setId(testId);
        test.setTimestamp(Timestamp.now());
        testCollection.document(testId).set(test);
        return test;
    }

    public void deleteTest(Test test) {
        testCollection.document(test.getId()).delete().addOnCompleteListener(task -> {
            db.collection("room").whereEqualTo("testId", test.getId()).get().addOnCompleteListener(roomTask -> {
                for (QueryDocumentSnapshot document : roomTask.getResult()) {
                    Room room = document.toObject(Room.class);
                    db.collection("room").document(room.getId()).delete();
                }
            });

            db.collection("questions").whereEqualTo("testId", test.getId()).get().addOnCompleteListener(questionTask -> {
                for (QueryDocumentSnapshot document : questionTask.getResult()) {
                    Question question = document.toObject(Question.class);

                    db.collection("answer").whereEqualTo("questionId", question.getId()).get().addOnCompleteListener(answerTask -> {
                        for (QueryDocumentSnapshot answerDocument : answerTask.getResult()) {
                            Answer answer = answerDocument.toObject(Answer.class);
                            db.collection("answer").document(answer.getId()).delete();
                        }
                    });
                    db.collection("questions").document(question.getId()).delete();
                }
            });
        });
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

        if (id == null || id.isEmpty()) {
            future.complete(testList);
            return future;
        }

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

        testCollection.document(testId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Test test = task.getResult().toObject(Test.class);
                future.complete(test);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    public CompletableFuture<List<Test>> getAllTestByResult(List<Result> results) {
        CompletableFuture<List<Test>> future = new CompletableFuture<>();
        List<Test> testList = new ArrayList<>();
        AtomicInteger pendingTasks = new AtomicInteger(results.size());

        for (Result result : results) {
            testCollection.whereEqualTo("id", result.getTestId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Test test = document.toObject(Test.class);
                        testList.add(test);
                    }
                }
                if (pendingTasks.decrementAndGet() == 0) {
                    future.complete(testList);
                }
            });
        }
        if (results.isEmpty()) {
            future.complete(testList);
        }
        return future;
    }

}
