package com.example.mytest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mytest.auth.Select;
import com.example.mytest.model.Question;

public class QuestionSettingActivity extends AppCompatActivity {

    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_setting);

        question = Select.getQuestion();
    }

    public void onBack(View view) {
        Intent intent = new Intent(QuestionSettingActivity.this, CreateTestActivity.class);
        startActivity(intent);
        finish();
    }


    public void ClickOnOneChoise(View view) {
        Intent intent = new Intent(QuestionSettingActivity.this, QuestionActivity.class);
        question.setType("single-choice");
        Select.setQuestion(question);
        startActivity(intent);
        finish();
    }

    public void ClickOnMultiChoise(View view) {
        Intent intent = new Intent(QuestionSettingActivity.this, QuestionActivity.class);
        question.setType("multi-choice");
        Select.setQuestion(question);
        startActivity(intent);
        finish();
    }

    public void ClickOnSort(View view){
        Intent intent = new Intent(QuestionSettingActivity.this,QuestionActivity.class);
        question.setType("sort");
        Select.setQuestion(question);
        startActivity(intent);
        finish();
    }
    public  void ClickOnText(View view){
        Intent intent = new Intent(QuestionSettingActivity.this,QuestionActivity.class);
        question.setType("text");
        Select.setQuestion(question);
        startActivity(intent);
        finish();
    }
    public  void ClickOnTrueFalse(View view){
        Intent intent = new Intent(QuestionSettingActivity.this,QuestionActivity.class);
        question.setType("true-false");
        Select.setQuestion(question);
        startActivity(intent);
        finish();
    }
    public  void ClickOnConnect(View view){
        Intent intent = new Intent(QuestionSettingActivity.this,QuestionActivity.class);
        question.setType("connect");
        Select.setQuestion(question);
        startActivity(intent);
        finish();
    }
}