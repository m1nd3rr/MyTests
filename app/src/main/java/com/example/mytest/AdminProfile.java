package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytest.repository.AdminRepository;
import com.example.mytest.repository.StudentRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfile extends AppCompatActivity {
    private AdminRepository adminRepository;
    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        adminRepository = new AdminRepository(FirebaseFirestore.getInstance());
        studentRepository = new StudentRepository(FirebaseFirestore.getInstance());

        AllStudent();
    }

    public void AllStudent() {
        TextView textView = findViewById(R.id.allStudent);
        studentRepository.getAllStudent().thenAccept(studentList -> {
            runOnUiThread(() -> {
                int studentCount = studentList.size();
                textView.setText("Всего студентов: " + studentCount);
            });
        }).exceptionally(throwable -> {
            runOnUiThread(() -> {
                Toast.makeText(this, "Ошибка загрузки студентов", Toast.LENGTH_SHORT).show();
            });
            return null;
        });
    }

    public void openAllStudents(View view) {
        Intent intent = new Intent(this, AllStudentsActivity.class);
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

