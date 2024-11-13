package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytest.adapter.TestAdapter;
import com.example.mytest.auth.Authentication;
import com.example.mytest.auth.Select;
import com.example.mytest.model.Test;
import com.example.mytest.repository.TestRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TeacherProfileActivity extends AppCompatActivity {

    TestRepository testRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        testRepository = new TestRepository(FirebaseFirestore.getInstance());
        setCreateTest();
    }

    public void ClickOnCreateTest(View view) {
        Test test = new Test();
        test.setTeacherId(Authentication.getTeacher().getId());
        Intent intent = new Intent(this, CreateTestActivity.class);
        Select.setTest(testRepository.addTest(test));
        startActivity(intent);
        finish();
    }

    public void ClickOnTestHistory(View view) {
        Intent intent = new Intent(this,TestHistory.class);
        startActivity(intent);
        finish();
    }
    public void setCreateTest(){
        TextView textView = findViewById(R.id.createTest);
        testRepository.getAllTestByTeacherId(Authentication.teacher.getId()).thenAccept(list ->{
            textView.setText(String.valueOf(list.size()));
        });
    }
}