package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytest.auth.Authentication;
import com.example.mytest.model.Student;
import com.example.mytest.repository.StudentRepository;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterStudentActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextGroupNumber, editTextEmail, editTextPassword;
    private StudentRepository studentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        studentRepository = new StudentRepository(FirebaseFirestore.getInstance());

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextGroupNumber = findViewById(R.id.editTextGroupNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String groupNumber = editTextGroupNumber.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !groupNumber.isEmpty() &&
                        !email.isEmpty() && !password.isEmpty()) {

                    studentRepository.getStudentByEmail(email)
                            .thenAccept(find_student -> {
                                if(find_student == null){
                                    Student student = new Student(null, firstName, lastName, groupNumber, password, email, null);
                                    studentRepository.addStudent(student);
                                    Authentication.setStudent(student);
                                    Intent intent = new Intent(RegisterStudentActivity.this, StudentProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else Toast.makeText(RegisterStudentActivity.this, "Такой студент уже есть", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(RegisterStudentActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void ClickOnLoginPageStudent(View view) {
        Intent intent = new Intent(RegisterStudentActivity.this,LoginStudentActivity.class);
        startActivity(intent);
        finish();
    }
}