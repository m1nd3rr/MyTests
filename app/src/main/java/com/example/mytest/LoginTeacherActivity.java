package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytest.auth.Authentication;
import com.example.mytest.repository.TeacherRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginTeacherActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private TeacherRepository teacherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        teacherRepository = new TeacherRepository(FirebaseFirestore.getInstance());

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    teacherRepository.getTeacherByEmail(email)
                            .thenAccept(teacher -> {
                                if (teacher.getPassword().equals(password)) {
                                    Authentication.setTeacher(teacher);
                                    Intent intent = new Intent(LoginTeacherActivity.this, TeacherProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else Toast.makeText(LoginTeacherActivity.this, "Проверьте данные", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(LoginTeacherActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}