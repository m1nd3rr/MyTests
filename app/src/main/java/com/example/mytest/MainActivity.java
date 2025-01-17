package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStudent = findViewById(R.id.button_student);
        Button buttonTeacher = findViewById(R.id.button_teacher);
        ImageView buttonAdmin = findViewById(R.id.button_admin);

        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterStudentActivity.class);
                startActivity(intent);
            }
        });

        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterTeacherActivity.class);
                startActivity(intent);
            }
        });

        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterAdmin.class);
                startActivity(intent);
            }
        });


    }

}