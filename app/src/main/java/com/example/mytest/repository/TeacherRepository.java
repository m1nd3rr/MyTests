package com.example.mytest.repository;

import androidx.annotation.NonNull;

import com.example.mytest.model.Student;
import com.example.mytest.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TeacherRepository {
    private final CollectionReference teacherCollection;

    public TeacherRepository(FirebaseFirestore db) {
        teacherCollection = db.collection("teacher");
    }

    public void addTeacher(Teacher teacher) {
        String teacherId = teacherCollection.document().getId();
        teacher.setId(teacherId);
        teacherCollection.document(teacherId).set(teacher);
    }

    public void deleteTeacher(Teacher teacher) {
        teacherCollection.document(teacher.getId()).delete();
    }

    public void updateTeacher(Teacher teacher) {
        teacherCollection.document(teacher.getId()).set(teacher);
    }

    public CompletableFuture<List<Teacher>> getAllTeacher() {
        CompletableFuture<List<Teacher>> future = new CompletableFuture<>();
        List<Teacher> teacherList = new ArrayList<>();

        teacherCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Teacher teacher = document.toObject(Teacher.class);
                teacherList.add(teacher);
            }
            future.complete(teacherList);
        });

        return future;
    }

    public CompletableFuture<Teacher> getTeacherById(String id) {
        CompletableFuture<Teacher> future = new CompletableFuture<>();

        teacherCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Teacher teacher = document.toObject(Teacher.class);
                if (teacher.getId().equals(id)) future.complete(teacher);
            }
        });

        return future;
    }
    public CompletableFuture<Teacher> getTeacherByEmail(String email) {
        CompletableFuture<Teacher> future = new CompletableFuture<>();

        teacherCollection.whereEqualTo("email",email).get().addOnCompleteListener(task -> {
            if(task.getResult().isEmpty()) future.complete(null);

            for (QueryDocumentSnapshot document : task.getResult()) {
                Teacher teacher = document.toObject(Teacher.class);
                if (teacher.getEmail().equals(email)) future.complete(teacher);
            }
        });

        return future;
    }

}
