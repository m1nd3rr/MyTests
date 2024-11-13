package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytest.auth.Authentication;
import com.example.mytest.model.Teacher;
import com.example.mytest.repository.TeacherRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterTeacherActivity extends AppCompatActivity {

    private EditText editTextTeacherFirstName, editTextTeacherLastName, editTextEmail, editTextPassword;

    private TeacherRepository teacherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        editTextTeacherFirstName = findViewById(R.id.editTextTeacherFirstName);
        editTextTeacherLastName = findViewById(R.id.editTextTeacherLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonSubmitTeacher = findViewById(R.id.buttonSubmitTeacher);

        teacherRepository = new TeacherRepository(FirebaseFirestore.getInstance());
        buttonSubmitTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextTeacherFirstName.getText().toString().trim();
                String lastName = editTextTeacherLastName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();


                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    teacherRepository.getTeacherByEmail(email)
                            .thenAccept(find_teacher -> {
                                if (find_teacher == null) {
                                    Teacher teacher = new Teacher(firstName,lastName,password,email);
                                    teacherRepository.addTeacher(teacher);
                                    Authentication.setTeacher(teacher);
                                    Intent intent = new Intent(RegisterTeacherActivity.this, TeacherProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else Toast.makeText(RegisterTeacherActivity.this, "Такой учитель уже есть", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(RegisterTeacherActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ClickOnLoginPageTeacher(View view) {
        Intent intent = new Intent(this, LoginTeacherActivity.class);
        startActivity(intent);
        finish();
    }
}
