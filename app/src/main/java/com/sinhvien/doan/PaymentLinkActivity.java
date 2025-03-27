package com.sinhvien.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PaymentLinkActivity extends AppCompatActivity {
    private TextView tvPaymentTitle;       // Tiêu đề hiển thị tên dịch vụ (ví dụ: "Liên kết tài khoản MoMo")
    private EditText edtPaymentInfo;       // Ô nhập thông tin (số điện thoại, số tài khoản, v.v.)
    private Button btnSave;                // Nút "Lưu" để lưu thông tin
    private Button btnBack;                // Nút "Quay lại" để thoát trang
    private DatabaseHelper databaseHelper; // Đối tượng để tương tác với cơ sở dữ liệu SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_link); // Gắn layout activity_payment_link.xml

        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Ánh xạ các thành phần giao diện từ layout
        tvPaymentTitle = findViewById(R.id.tvPaymentTitle);
        edtPaymentInfo = findViewById(R.id.edtPaymentInfo);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Lấy tên dịch vụ từ Intent
        String serviceName = getIntent().getStringExtra("service_name");
        if (serviceName != null) {
            tvPaymentTitle.setText("Liên kết tài khoản " + serviceName); // Cập nhật tiêu đề với tên dịch vụ
        }

        // Xử lý nút Lưu
        btnSave.setOnClickListener(v -> {
            String paymentInfo = edtPaymentInfo.getText().toString().trim(); // Lấy thông tin người dùng nhập
            if (paymentInfo.isEmpty()) { // Kiểm tra xem ô nhập có trống không
                Toast.makeText(this, "Vui lòng nhập thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu thông tin vào database
            String columnName = getColumnName(serviceName); // Lấy tên cột tương ứng trong database
            if (columnName != null) {
                savePaymentInfo(columnName, paymentInfo); // Gọi hàm lưu thông tin
                Toast.makeText(this, "Đã lưu thông tin cho " + serviceName, Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity sau khi lưu thành công
            }
        });

        // Xử lý nút Quay lại
        btnBack.setOnClickListener(v -> finish()); // Đóng Activity khi nhấn "Quay lại"
    }

    // Hàm ánh xạ tên dịch vụ với tên cột trong database
    private String getColumnName(String serviceName) {
        switch (serviceName) {
            case "MoMo":
                return "momo_number";
            case "ZaloPay":
                return "zalopay_number";
            case "Vietcombank":
                return "vietcombank_account";
            case "MB Bank":
                return "mbbank_account";
            case "VietinBank":
                return "vietinbank_account";
            default:
                return null; // Trả về null nếu dịch vụ không hợp lệ
        }
    }

    // Hàm lưu thông tin vào database
    private void savePaymentInfo(String columnName, String paymentInfo) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Lấy thông tin người dùng từ Firebase
        if (user != null) {
            int userId = databaseHelper.getUserId(user.getUid()); // Lấy ID người dùng từ database
            databaseHelper.updatePaymentInfo(userId,
                    columnName.equals("momo_number") ? paymentInfo : "",       // Chỉ cập nhật cột tương ứng
                    columnName.equals("zalopay_number") ? paymentInfo : "",
                    columnName.equals("vietcombank_account") ? paymentInfo : "",
                    columnName.equals("mbbank_account") ? paymentInfo : "",
                    columnName.equals("vietinbank_account") ? paymentInfo : "");
        }
    }
}