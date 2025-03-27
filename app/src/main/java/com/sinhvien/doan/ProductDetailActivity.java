package com.sinhvien.doan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String productId = getIntent().getStringExtra("product_id");

        if ("1".equals(productId)) {
            setContentView(R.layout.banh_choco);
        } else if ("2".equals(productId)) {
            setContentView(R.layout.flan);
        } else if ("3".equals(productId)) {
            setContentView(R.layout.rocher);
        } else if ("4".equals(productId)) {
            setContentView(R.layout.tiramisu);
        } else if ("5".equals(productId)) {
            setContentView(R.layout.trungbo);
        } else if ("6".equals(productId)) {
            setContentView(R.layout.cahoi);
        } else if ("7".equals(productId)) {
            setContentView(R.layout.xongkhoi);
        } else if ("8".equals(productId)) {
            setContentView(R.layout.tom);
        } else if ("9".equals(productId)) {
            setContentView(R.layout.banhkem_traxanh);
        } else if ("10".equals(productId)) {
            setContentView(R.layout.banhkem_xoai);
        } else if ("11".equals(productId)) {
            setContentView(R.layout.banhkem_traicay);
        } else if ("12".equals(productId)) {
            setContentView(R.layout.banhkem_chanh);
        } else {
            finish();
        }

        Button btnDonate = findViewById(R.id.btnDonate);
        Button btnBack = findViewById(R.id.btnBack);

        btnDonate.setOnClickListener(v -> {
            String paymentLink = "https://mbbank.com.vn/0906780284"; // Tài khoản admin
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink));
            startActivity(browserIntent);
        });

        btnBack.setOnClickListener(v -> finish());
    }
}