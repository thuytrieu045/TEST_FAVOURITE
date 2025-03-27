package com.sinhvien.doan;

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


public class Category2Activity extends AppCompatActivity {
    private RecyclerView rvListC;
    private ProductAdapter productAdapter;

    private RecipeAdapter recipeAdapter;
    private ArrayList<Product> lstProduct;
    private ArrayList<Recipe> lstRecipe;
    private MyDataBase myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category2);

        // Xử lý nút "Back"
        Button button = findViewById(R.id.btnBack);
        button.setOnClickListener(v -> finish()); // Trả về MainActivity

        // Khởi tạo
        rvListC = findViewById(R.id.rvList);
        lstProduct = new ArrayList<>();
        lstRecipe = new ArrayList<>();
        myDataBase = new MyDataBase(this); // Khởi tạo DB

        // Load dữ liệu sản phẩm
        LoadData();
        boolean hasRecipes = LoadDataRecipe(); // Kiểm tra bài đăng

        // Cài đặt adapter
        productAdapter = new ProductAdapter(this, lstProduct);
        rvListC.setLayoutManager(new LinearLayoutManager(this));

        if(hasRecipes) { // Kiểm tra nếu có tồn tại bài đăng
            recipeAdapter = new RecipeAdapter(this, lstRecipe);
            rvListC.setAdapter(new ConcatAdapter(productAdapter, recipeAdapter));
        }
        else{
            rvListC.setAdapter(productAdapter);
        }
    }

    // Load dữ liệu Product
    private void LoadData() {
        lstProduct.add(new Product("5", "Bánh mì nướng phủ bơ nghiền và trứng", "Bánh mì nướng phủ bơ nghiền, trứng luộc lòng đào thái lát, rắc tiêu đen và ớt bột, tạo hương vị béo ngậy, thơm ngon. ", "trungbo.jpg", R.drawable.trungbo));
        lstProduct.add(new Product("6", "Bánh mì cá hồi", "Bánh bagel hạt giòn thơm, kẹp kem phô mai, bơ lát và cá hồi xông khói, tạo hương vị béo ngậy, mặn mà. ", "bothit.jpg", R.drawable.bothit));
        lstProduct.add(new Product("7", "bánh mì xông khói", "Bagel hạt giòn, kẹp kem phô mai, cá hồi xông khói, hành tím và nụ bạch hoa, tạo hương vị béo, mặn và thanh nhẹ", "xongkhoi.jpg", R.drawable.xongkhoi));
        lstProduct.add(new Product("8", "bánh mì tôm hùm", "Bánh mì nướng giòn phủ sốt đỏ đậm đà, tôm hùm tươi ngọt, thịt cua thơm béo và rau xanh tươi mát, tạo hương vị hài hòa và hấp dẫn", "tom.jpg", R.drawable.tom));
    }

    // Load dữ liệu Recipe từ Category2
    private boolean LoadDataRecipe() {
        Cursor cursor = myDataBase.getRecipeByCategory(2); // Load category 2 (Breakfast)
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
            } while (cursor.moveToNext());
            cursor.close();
        }
        return !lstRecipe.isEmpty(); // Trả về true nếu có công thức trong DB
    }
}
