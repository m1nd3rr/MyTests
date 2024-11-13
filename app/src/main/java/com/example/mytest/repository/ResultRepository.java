package com.example.mytest.repository;

import android.util.Pair;

import com.example.mytest.model.Answer;
import com.example.mytest.model.Result;
import com.example.mytest.model.Test;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ResultRepository {
    private final CollectionReference resultCollection;

    public ResultRepository(FirebaseFirestore db){
        resultCollection = db.collection("result");
    }

    public Result addResult(Result result) {
        String resultId = resultCollection.document().getId();
        result.setId(resultId);
        result.setTime(Timestamp.now());
        resultCollection.document(resultId).set(result);

        return result;
    }

    public CompletableFuture<List<Result>> getAllResultByStudentId(String id) {
        CompletableFuture<List<Result>> future = new CompletableFuture<>();
        List<Result> resultList = new ArrayList<>();

        resultCollection.whereEqualTo("userId", id).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Result result = document.toObject(Result.class);
                resultList.add(result);
            }
            future.complete(resultList);
        });

        return future;
    }

    public CompletableFuture<Pair<Integer, Integer>> getResultByStudentId(String id) {
        CompletableFuture<Pair<Integer, Integer>> future = new CompletableFuture<>();

        resultCollection.whereEqualTo("userId", id).get().addOnCompleteListener(task -> {
            int correct = 0, count = 0;

            for (QueryDocumentSnapshot document : task.getResult()) {
                Result result = document.toObject(Result.class);
                correct += result.getCorrectAnswer();
                count += result.getCountAnswer();
            }
            Pair<Integer, Integer> res = new Pair<>(correct, count);
            future.complete(res);
        });

        return future;
    }
}
