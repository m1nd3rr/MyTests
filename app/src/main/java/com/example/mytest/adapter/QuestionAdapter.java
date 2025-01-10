package com.example.mytest.adapter;

import android.app.Activity;
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

import com.example.mytest.R;
import com.example.mytest.QuestionActivity;
import com.example.mytest.auth.Select;
import com.example.mytest.model.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private final List<Question> questionList;
    private final Context context;
    public QuestionAdapter(List<Question> questionList, Context context) {
        this.questionList = questionList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_item_list, parent, false);
        return new QuestionViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView text, number;
        private Context context;
        private ImageView icon;
        public QuestionViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            text = itemView.findViewById(R.id.question_title);
            number = itemView.findViewById(R.id.question_number);
            icon = itemView.findViewById(R.id.notificationIcon);
            this.context = context;
        }

        public void bind(Question question) {
            number.setText(String.valueOf(getAdapterPosition() + 1));
            text.setText(question.getTitle());
            icon.setOnClickListener(view -> {
                Intent intent = new Intent(context, QuestionActivity.class);
                Select.setQuestion(question);
                context.startActivity(intent);
                ((Activity) context).finish();
            });
        }
    }
}
