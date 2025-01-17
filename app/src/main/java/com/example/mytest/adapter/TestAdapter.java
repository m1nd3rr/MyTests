package com.example.mytest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytest.CreateTestActivity;
import com.example.mytest.PassingTestActivity;
import com.example.mytest.R;
import com.example.mytest.StudentProfileActivity;
import com.example.mytest.TestHistory;
import com.example.mytest.auth.Authentication;
import com.example.mytest.auth.Select;
import com.example.mytest.model.Test;
import com.example.mytest.repository.TestRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private final List<Test> testList;
    private final Context context;

    public TestAdapter(List<Test> testList, Context context) {
        this.testList = testList;
        this.context = context;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_item_list, parent, false);
        return new TestViewHolder(view, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test test = testList.get(position);
        holder.bind(test);

    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        private TextView text, number;
        private LinearLayout layout;
        private Context context;
        private ImageView imageView;
        private TestAdapter adapter;

        public TestViewHolder(@NonNull View itemView, Context context, TestAdapter adapter) {
            super(itemView);
            text = itemView.findViewById(R.id.test_title);
            number = itemView.findViewById(R.id.test_number);
            layout = itemView.findViewById(R.id.test_item_list);
            imageView = itemView.findViewById(R.id.notificationIcon);
            this.context = context;
            this.adapter = adapter;
        }

        public void bind(Test test) {
            number.setText(String.valueOf(getAdapterPosition() + 1));
            text.setText(test.getTitle());
            layout.setOnClickListener(view -> {
                Intent intent;
                if (Authentication.getStudent() != null && !(context instanceof TestHistory)) {
                    intent = new Intent(context, PassingTestActivity.class);
                } else {
                    intent = new Intent(context, CreateTestActivity.class);
                }
                Select.setTest(test);
                context.startActivity(intent);
            });

            imageView.setOnClickListener(view -> {
                TestRepository testRepository = new TestRepository(FirebaseFirestore.getInstance());
                testRepository.deleteTest(test);
                int position = getAdapterPosition();
                adapter.removeTest(position);
            });
        }
    }


    public void removeTest(int position) {
        testList.remove(position);
        notifyItemRemoved(position);
    }

}
