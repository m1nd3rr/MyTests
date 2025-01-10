package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytest.R;
import com.example.mytest.adapter.StudentAdapter;
import com.example.mytest.repository.StudentRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class AllStudentsActivity extends AppCompatActivity {
    private StudentRepository studentRepository;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_students);

        studentRepository = new StudentRepository(FirebaseFirestore.getInstance());
        recyclerView = findViewById(R.id.recycler_view_students);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadStudents();
    }

    private void loadStudents() {
        studentRepository.getAllStudent().thenAccept(studentList -> {
            adapter = new StudentAdapter(studentList, student -> {
                studentRepository.deleteStudent(student);
                adapter.removeStudent(student);
                Toast.makeText(this, "Студент удален", Toast.LENGTH_SHORT).show();
            });
            recyclerView.setAdapter(adapter);
        }).exceptionally(throwable -> {
            Toast.makeText(this, "Ошибка загрузки студентов", Toast.LENGTH_SHORT).show();
            return null;
        });
    }

    public void OnBack(View view) {
        Intent intent = new Intent(AllStudentsActivity.this, AdminProfile.class);
        startActivity(intent);
        finish();
    }
}
