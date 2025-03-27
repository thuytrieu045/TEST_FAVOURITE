package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class LinkAccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_accounts);

        // Khai báo các ImageView
        ImageView imgMoMo = findViewById(R.id.imgMoMo);
        ImageView imgZaloPay = findViewById(R.id.imgZaloPay);
        ImageView imgVietcombank = findViewById(R.id.imgVietcombank);
        ImageView imgMBBank = findViewById(R.id.imgMBBank);
        ImageView imgVietinBank = findViewById(R.id.imgVietinBank);
        Button btnBack = findViewById(R.id.btnBack);

        // Xử lý sự kiện khi nhấn vào các ImageView
        imgMoMo.setOnClickListener(v -> openPaymentLinkActivity("MoMo"));
        imgZaloPay.setOnClickListener(v -> openPaymentLinkActivity("ZaloPay"));
        imgVietcombank.setOnClickListener(v -> openPaymentLinkActivity("Vietcombank"));
        imgMBBank.setOnClickListener(v -> openPaymentLinkActivity("MB Bank"));
        imgVietinBank.setOnClickListener(v -> openPaymentLinkActivity("VietinBank"));

        // Quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void openPaymentLinkActivity(String serviceName) {
        Intent intent = new Intent(this, PaymentLinkActivity.class);
        intent.putExtra("service_name", serviceName);
        startActivity(intent);
    }
}