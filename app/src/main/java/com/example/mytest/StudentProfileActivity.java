package com.example.mytest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mytest.auth.Authentication;
import com.example.mytest.auth.Select;
import com.example.mytest.model.Test;
import com.example.mytest.repository.ResultRepository;
import com.example.mytest.repository.RoomRepository;
import com.example.mytest.repository.StudentRepository;
import com.example.mytest.repository.TestRepository;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import android.Manifest;

import java.util.ArrayList;

public class StudentProfileActivity extends AppCompatActivity {
    private StudentRepository studentRepository;
    private RoomRepository roomRepository;
    private ResultRepository resultRepository;
    TestRepository testRepository;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        roomRepository = new RoomRepository(FirebaseFirestore.getInstance());
        resultRepository = new ResultRepository(FirebaseFirestore.getInstance());
        testRepository = new TestRepository(FirebaseFirestore.getInstance());
        studentRepository = new StudentRepository(FirebaseFirestore.getInstance());
        setCompleteTest();
        setCreateTestUser();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        ImageView photoUser = findViewById(R.id.profile_image);
        if (Authentication.getStudent().getPhoto() != null) {
            Glide.with(this).load(Authentication.getStudent().getPhoto()).apply(new RequestOptions()
                    .centerCrop()
                    .circleCrop()).into(photoUser);
        }


        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home){
                return true;
            }
            else if (item.getItemId() == R.id.navigation_search){
                Intent intent = new Intent(this, TestsActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        resultRepository.getResultByStudentId(Authentication.student.getId())
                .thenAccept(pair -> {
                    createPieDiagram(pair);
                });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            ImageView photo = findViewById(R.id.profile_image);
                            Glide.with(this).load(imageUri).apply(new RequestOptions()
                                    .centerCrop()
                                    .circleCrop()).into(photo);

                            String userId = Authentication.getStudent().getId();
                            CloudinaryUploader uploader = new CloudinaryUploader(this);
                            uploader.uploadImage(imageUri, userId, new CloudinaryUploader.UploadCallback() {
                                @Override
                                public void onUploadComplete(String imageUrl) {
                                    if (imageUrl != null) {
                                        Authentication.getStudent().setPhoto(imageUrl);
                                        studentRepository.updateStudent(Authentication.getStudent());
                                    }
                                }
                            });
                        }
                    }
                });
    }

    public void createPieDiagram(Pair<Integer, Integer> pair) {
        PieChart pieChart = findViewById(R.id.pieChart);

        // Данные для диаграммы
        int correctAnswers = pair.first;  // Количество правильных ответов
        int totalQuestions = pair.second; // Общее количество вопросов

        float percentageCorrect = (correctAnswers / (float) totalQuestions) * 100;
        float percentageIncorrect = 100 - percentageCorrect;

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(percentageCorrect, "")); // Процент правильных
        entries.add(new PieEntry(percentageIncorrect, "")); // Остаток для заполнения

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.WHITE, Color.LTGRAY); // Цвет правильных и серый для пустого места
        dataSet.setValueTextSize(0f);  // Убираем подписи значений на секторах

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);  // Процентное отображение
        pieChart.getDescription().setEnabled(false);  // Убираем описание
        pieChart.getLegend().setEnabled(false);  // Убираем легенду
        pieChart.setRotationEnabled(false);  // Запрещаем вращение

        pieChart.setHoleRadius(90f);  // Радиус центральной "дырки"
        pieChart.setHoleColor(Color.TRANSPARENT);

        // Установить процент правильных ответов в центре
        pieChart.setCenterText(Math.round(percentageCorrect) + "%");
        pieChart.setCenterTextSize(24f);
        pieChart.setCenterTextColor(Color.BLACK);

        pieChart.animateY(1000);  // Анимация
        pieChart.invalidate(); // Обновление диаграммы
    }


    public void ClickMama(View view) {
        showEnterCodeDialog();
    }

    private void showEnterCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите код комнаты");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String roomCode = input.getText().toString();
                if (!roomCode.isEmpty()) {
                    fetchTestByRoomCode(roomCode);
                } else {
                    Toast.makeText(StudentProfileActivity.this, "Введен неправильный код", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void fetchTestByRoomCode(String roomCode) {
        roomRepository.getTestByRoomNumber(roomCode)
                .thenAccept(test -> {
                    if (test != null) {
                        Select.setTest(test);
                        Intent intent = new Intent(this, PassingTestActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid room code", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setCompleteTest(){
        TextView textView = findViewById(R.id.completeTest);
        resultRepository.getAllResultByStudentId(Authentication.student.getId()).thenAccept(list->{
            textView.setText(String.valueOf(list.size()));
        });
    }

    public void CreateTest(View view) {
        Test test = new Test();
        test.setStudentId(Authentication.getStudent().getId());
        Intent intent = new Intent(this, CreateTestActivity.class);
        Select.setTest(testRepository.addTest(test));
        startActivity(intent);
        finish();
    }
    public void setCreateTestUser(){
        TextView textView = findViewById(R.id.setCreateTestUser);
        testRepository.getAllTestByStudentId(Authentication.student.getId()).thenAccept(list ->{
            textView.setText(String.valueOf(list.size()));
        });
    }

    public void onClickOpenGallery(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 1);
            } else {
                openGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }
}
