package com.sinhvien.doan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class RecipePostActivity extends AppCompatActivity {
    private ImageView imgRecipe;
    private TextView txtRecipeName, txtDifficulty, txtTime, txtIngredients, txtSteps;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_product_details);

        databaseHelper = new DatabaseHelper(this);

        imgRecipe = findViewById(R.id.imgRecipe);
        txtRecipeName = findViewById(R.id.txtRecipeTitle);
        txtDifficulty = findViewById(R.id.txtRecipeDifficulty);
        txtTime = findViewById(R.id.txtRecipeTime);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtSteps = findViewById(R.id.txtSteps);
        Button btnDonate = findViewById(R.id.btnDonate);
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

            btnDonate.setOnClickListener(v -> handleDonate(name));
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void handleDonate(String recipeName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user_id FROM recipes WHERE recipe_name = ?", new String[]{recipeName});
        String paymentLink = "https://mbbank.com.vn/0906780284"; // Mặc định là tài khoản admin

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            Cursor paymentCursor = db.rawQuery("SELECT momo_number FROM users WHERE user_id = ?", new String[]{String.valueOf(userId)});
            if (paymentCursor.moveToFirst() && paymentCursor.getString(0) != null && !paymentCursor.getString(0).isEmpty()) {
                String momoNumber = paymentCursor.getString(0);
                paymentLink = "https://momo.vn/" + momoNumber; // Link MoMo của chủ bài viết
            }
            paymentCursor.close();
        }
        cursor.close();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink));
        startActivity(browserIntent);
    }
}