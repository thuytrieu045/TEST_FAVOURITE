package com.sinhvien.doan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Category3Activity extends AppCompatActivity {
    private RecyclerView rvListC;
    private ProductAdapter productAdapter;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Product> lstProduct;
    private ArrayList<Recipe> lstRecipe;
    private MyDataBase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category3);

        // Xử lý nút "Back"
        Button button = findViewById(R.id.btnBack);
        button.setOnClickListener(v -> finish()); // Trả về MainActivity

        // Khởi tạo
        rvListC = findViewById(R.id.rvList);
        lstProduct = new ArrayList<>();
        lstRecipe = new ArrayList<>();
        myDataBase = new MyDataBase(this);

        // Load dữ liệu sản phẩm
        LoadData();
        boolean hasRecipes = LoadDataRecipe(); // Kiểm tra tồn tại bài đăng

        // Cài đặt adapter
        productAdapter = new ProductAdapter(this, lstProduct);
        rvListC.setLayoutManager(new LinearLayoutManager(this));

        if(hasRecipes) {
            recipeAdapter = new RecipeAdapter(this, lstRecipe);
            rvListC.setAdapter(new ConcatAdapter(productAdapter, recipeAdapter));
        }
        else {
            rvListC.setAdapter(productAdapter);
        }
    }

    // Load dữ liệu Product
    private void LoadData() {
        lstProduct.add(new Product("9", "Bánh kem trà xanh", "Bánh kem trà xanh là bánh được làm từ bột trà xanh Nhật Bản (matcha), mang hương vị thanh nhẹ, hơi đắng nhưng hài hòa với vị ngọt.", "matcha.jpg", R.drawable.matcha));
        lstProduct.add(new Product("10", "Bánh kem xoài phủ lớp kem cheese", "Bánh kem xoài phủ lớp kem cheese là bánh kem xoài mềm mịn, thường có lớp bánh bông lan xốp nhẹ, xen kẽ với kem tươi béo ngậy và xoài tươi ngọt thanh.", "mango.jpg", R.drawable.mango));
        lstProduct.add(new Product("11", "Bánh kem trái cây", "Bánh kem trái cây là loại bánh bông lan mềm mịn, phủ lớp kem tươi béo ngậy và trang trí với các loại trái cây tươi như dâu, kiwi, xoài, việt quất,… tạo hương vị thanh mát, ngọt dịu.", "traicay.jpg", R.drawable.traicay));
        lstProduct.add(new Product("12", "Bánh kem chanh tươi ", "Bánh kem chanh tươi có lớp bánh mềm mịn, vị chua thanh mát, kết hợp kem béo ngậy và sốt chanh thơm dịu. ", "lemoncake.jpg", R.drawable.lemoncake));

    }

    // Load dữ liệu Recipe từ Category 3
    private boolean LoadDataRecipe() {
        Cursor cursor = myDataBase.getRecipeByCategory(3); // Lấy dữ liệu từ Category 3
        if(cursor != null && cursor.moveToFirst()) {
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
            }while(cursor.moveToNext());
        }
        return !lstRecipe.isEmpty();
    }
}
