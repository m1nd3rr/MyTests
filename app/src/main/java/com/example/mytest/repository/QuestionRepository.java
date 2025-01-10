package com.example.mytest.repository;

import com.example.mytest.model.Question;
import com.example.mytest.model.Test;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QuestionRepository {
    private final CollectionReference questionCollection;
    public QuestionRepository(FirebaseFirestore db){
        questionCollection = db.collection("questions");
    }
    public Question addQuestion(Question question) {
        String questionId = questionCollection.document().getId();
        question.setId(questionId);
        questionCollection.document(questionId).set(question);

        return  question;
    }
    public void deleteQuestion(Question question) {
        questionCollection.document(question.getId()).delete();
    }

    public void updateQuestion(Question question) {
        questionCollection.document(question.getId()).set(question);
    }

    public CompletableFuture<List<Question>> getAllQuestion() {
        CompletableFuture<List<Question>> future = new CompletableFuture<>();
        List<Question> questionList = new ArrayList<>();

        questionCollection.get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Question question = document.toObject(Question.class);
                questionList.add(question);
            }
            future.complete(questionList);
        });

        return future;
    }

    public CompletableFuture<List<Question>> getAllQuestionByTestId(String id) {
        CompletableFuture<List<Question>> future = new CompletableFuture<>();
        List<Question> questionList = new ArrayList<>();
        questionList.clear();

        questionCollection.whereEqualTo("testId", id).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Question question = document.toObject(Question.class);
                questionList.add(question);
            }
            future.complete(questionList);
        });

        return future;
    }
}
