package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytest.auth.Authentication;
import com.example.mytest.repository.StudentRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginStudentActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        studentRepository = new StudentRepository(FirebaseFirestore.getInstance());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    studentRepository.getStudentByEmail(email)
                            .thenAccept(student -> {
                                if (student.getPassword().equals(password)) {
                                    Authentication.setStudent(student);
                                    Intent intent = new Intent(LoginStudentActivity.this, StudentProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else Toast.makeText(LoginStudentActivity.this, "Проверьте данные", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(LoginStudentActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
