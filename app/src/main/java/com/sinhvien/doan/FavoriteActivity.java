package com.sinhvien.doan;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private AppCompatActivity toolbar;
    private MaterialToolbar appBarLayout;
    //private Toolbar toolbar;
    private RecyclerView favoritesRecyclerView;
    private ProductAdapter productAdapter;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Product> lstProduct;
    private ImageView ivAvatar;
    private TextView tvNameFavourite;
    private Button btnBack;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Nút quay về
        Button button = findViewById(R.id.btnBack);
        button.setOnClickListener(v -> finish());
        TextView tvNameFavourite = findViewById(R.id.tvNameFavourite);
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        // Khởi tạo

        lstProduct = new ArrayList<>();

        ArrayList<Product> productList = new ArrayList<Product>();

        MyDataBase myDataBase = new MyDataBase(this);

        databaseHelper = new DatabaseHelper(this);




        // Cài đặt adapter
        productAdapter = new ProductAdapter(this, lstProduct);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
