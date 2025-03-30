package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ViewPostActivity extends AppCompatActivity {
    private ImageView imgRecipe;
    private TextView txtRecipeName, txtDifficulty, txtTime, txtIngredients, txtSteps;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        databaseHelper = new DatabaseHelper(this);

        imgRecipe = findViewById(R.id.imgRecipe);
        txtRecipeName = findViewById(R.id.txtRecipeTitle);
        txtDifficulty = findViewById(R.id.txtRecipeDifficulty);
        txtTime = findViewById(R.id.txtRecipeTime);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtSteps = findViewById(R.id.txtSteps);
        Button btnBack = findViewById(R.id.btnBack);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("title");
            String difficulty = intent.getStringExtra("difficulty");
            int time = intent.getIntExtra("time", -1);
            String ingredients = intent.getStringExtra("ingredients");
            String steps = intent.getStringExtra("steps");
            String imageUrl = intent.getStringExtra("imageUrl");

            txtRecipeName.setText(name);
            txtDifficulty.setText("Độ khó: " + difficulty);
            if (time > 60) {
                txtTime.setText("Thời gian: " + time / 60 + " giờ " + time % 60 + " phút");
            } else {
                txtTime.setText("Thời gian: " + time + " phút");
            }
            txtIngredients.setText(ingredients);
            txtSteps.setText(steps);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl).into(imgRecipe);
            } else {
                imgRecipe.setImageResource(R.drawable.dessert);
            }
        }

        btnBack.setOnClickListener(v -> finish());
    }
}