package com.example.mytest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytest.adapter.TestAdapter;
import com.example.mytest.auth.Authentication;
import com.example.mytest.model.Test;
import com.example.mytest.repository.TestRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TestHistory extends AppCompatActivity {
    TestRepository testRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_history);

        RecyclerView recyclerView = findViewById(R.id.rvTestList);
        testRepository = new TestRepository(FirebaseFirestore.getInstance());
        List<Test> testList = new ArrayList<>();

        testRepository.getAllTestByTeacherId(Authentication.getTeacher().getId())
                .thenAccept(list -> {
                    testList.addAll(list);
                    TestAdapter testAdapter = new TestAdapter(testList, this);
                    recyclerView.setAdapter(testAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                });
    }
}
