package com.example.mytest.repository;

import com.example.mytest.model.Student;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StudentRepository {
    private final CollectionReference studentCollection;

    public StudentRepository(FirebaseFirestore db) {
        studentCollection = db.collection("students");
    }

    public void addStudent(Student student) {
        String studentId = studentCollection.document().getId();
        student.setId(studentId);
        studentCollection.document(studentId).set(student);
    }

    public void deleteStudent(Student student) {
        studentCollection.document(student.getId()).delete();
    }

    public void updateStudent(Student student) {
        studentCollection.document(student.getId()).set(student);
    }

    public CompletableFuture<List<Student>> getAllStudent() {
        CompletableFuture<List<Student>> future = new CompletableFuture<>();
        List<Student> studentList = new ArrayList<>();

        studentCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Student student = document.toObject(Student.class);
                studentList.add(student);
            }
            future.complete(studentList);
        });

        return future;
    }

    public CompletableFuture<Student> getStudentById(String id) {
        CompletableFuture<Student> future = new CompletableFuture<>();

        studentCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Student student = document.toObject(Student.class);
                if (student.getId().equals(id)) future.complete(student);
            }
        });

        return future;
    }
    public CompletableFuture<Student> getStudentByEmail(String email) {
        CompletableFuture<Student> future = new CompletableFuture<>();

        studentCollection.whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()) future.complete(null);

            for (QueryDocumentSnapshot document : task.getResult()) {
                Student student = document.toObject(Student.class);
                if (student.getEmail().equals(email)) future.complete(student);
            }
        });

        return future;
    }
}
