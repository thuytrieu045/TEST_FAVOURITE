package com.sinhvien.doan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Category1Activity extends AppCompatActivity {
    private RecyclerView rvListC;
    private ProductAdapter productAdapter;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Product> lstProduct;
    private ArrayList<Recipe> lstRecipe;
    private MyDataBase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category1);

        // Nút quay về
        Button button = findViewById(R.id.btnBack);
        button.setOnClickListener(v -> finish());

        // Khởi tạo
        rvListC = findViewById(R.id.rvList);
        lstProduct = new ArrayList<>();
        lstRecipe = new ArrayList<>();
        myDatabase = new MyDataBase(this); // Khởi tạo database

        // Load dữ liệu sản phẩm
        loadProductData();
        boolean hasRecipes = loadRecipeData(); // Kiểm tra nếu có bài đã được đăng

        // Cài đặt adapter
        productAdapter = new ProductAdapter(this, lstProduct);
        rvListC.setLayoutManager(new LinearLayoutManager(this));

        if (hasRecipes) { // Chỉ đặt recipeAdapter nếu có bài đã được đăng
            recipeAdapter = new RecipeAdapter(this, lstRecipe);
            rvListC.setAdapter(new ConcatAdapter(productAdapter, recipeAdapter));
        } else {
            rvListC.setAdapter(productAdapter); // Chỉ hiển thị product
        }
    }

    // Load dữ liệu Product
    private void loadProductData() {
        lstProduct.add(new Product("1", "Choco", "Bánh Choco là bánh quy phủ sô cô la", "banhchoco.jpg", R.drawable.choco));
        lstProduct.add(new Product("2", "Flan", "Bánh caramel mềm mịn", "bread.jpg", R.drawable.flan));
        lstProduct.add(new Product("3", "Rocher", "Ferrero Rocher Chocolate", "rocher.jpg", R.drawable.rocher));
        lstProduct.add(new Product("4", "Tiramisu", "Món tráng miệng Ý", "tiramisu.jpg", R.drawable.tiraminsu));
    }

    // Load dữ liệu Recipe từ Category2
    private boolean loadRecipeData() {
        Cursor cursor = myDatabase.getRecipeByCategory(1); // Load category 1 (Sweet Dessert)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_TEN_RECIPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_INGREDIENTS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_STEPS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_IMG_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COT_DOKHO))
                );
                lstRecipe.add(recipe);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return !lstRecipe.isEmpty(); // Trả về true nếu có công thức trong DB
    }
}
