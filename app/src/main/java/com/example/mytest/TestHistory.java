package com.example.mytest;

import static com.example.mytest.auth.Select.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytest.adapter.TestAdapter;
import com.example.mytest.auth.Authentication;
import com.example.mytest.model.Test;
import com.example.mytest.repository.QuestionRepository;
import com.example.mytest.repository.ResultRepository;
import com.example.mytest.repository.RoomRepository;
import com.example.mytest.repository.TestRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TestHistory extends AppCompatActivity {
    TestRepository testRepository;
    ResultRepository resultRepository;
    Test test;
    QuestionRepository questionRepository;
    RoomRepository roomRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_history);

        RecyclerView recyclerView = findViewById(R.id.rvTestList);
        resultRepository = new ResultRepository(FirebaseFirestore.getInstance());
        testRepository = new TestRepository(FirebaseFirestore.getInstance());
        List<Test> testList = new ArrayList<>();
        questionRepository = new QuestionRepository(FirebaseFirestore.getInstance());
        roomRepository = new RoomRepository(FirebaseFirestore.getInstance());

        if (Authentication.getStudent() != null) {
            if(getIntent().getStringExtra("button").equals("complete")){
                resultRepository.getAllResultByStudentId(Authentication.student.getId()).thenAccept(list ->{
                    testRepository.getAllTestByResult(list).thenAccept(listTest ->{
                        testList.addAll(listTest);
                        TestAdapter testAdapter = new TestAdapter(testList, this);
                        recyclerView.setAdapter(testAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    });
                });
            }
            else {
                testRepository.getAllTestByStudentId(Authentication.student.getId()).thenAccept(list ->{
                    testList.addAll(list);
                    TestAdapter testAdapter = new TestAdapter(testList, this);
                    recyclerView.setAdapter(testAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                });
            }
        }else {
            testRepository.getAllTestByTeacherId(Authentication.getTeacher().getId())
                    .thenAccept(list -> {
                        testList.addAll(list);
                        TestAdapter testAdapter = new TestAdapter(testList, this);
                        recyclerView.setAdapter(testAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    });
        }
    }

    public void onBackButtonClickk(View view) {
        Intent intent;
        if(Authentication.getTeacher() != null){
             intent = new Intent(this, TeacherProfileActivity.class);
        } else {
            intent = new Intent(this, StudentProfileActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
