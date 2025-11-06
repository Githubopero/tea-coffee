package com.example.teacoffee;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacoffee.myDB.Bill;
import com.example.teacoffee.myDB.MyDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private static String fmt(Long ms) {
        if (ms == null) return "-";
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(ms));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int tableId = getIntent().getIntExtra("tableId", -1);

        // 1) Lấy bill đang mở của bàn
        MyDatabase db = MyDatabase.getINSTANCE(this);
        // dùng bản DAO đã sửa (mục 2 bên dưới)
        Bill bill = db.getBillDAO().getOpenByTable(tableId, "0");

        StringBuilder sb = new StringBuilder();

        if (bill != null) {
            // 2) Đóng bill
            bill.Date_Checkout = System.currentTimeMillis();
            bill.Status = "1"; // Closed
            db.getBillDAO().update(bill);

            // 3) Trả trạng thái bàn về rảnh
            db.getTableDAO().updateStatus(0, tableId);

            // 4) Hiển thị tóm tắt
            sb.append("Bàn: ").append(tableId).append("\n")
                    .append("Bill_ID: ").append(bill.Bill_Id).append("\n")
                    .append("Checkin: ").append(fmt(bill.Date_Checkin)).append("\n")
                    .append("Checkout: ").append(fmt(bill.Date_Checkout)).append("\n")
                    .append("Giảm giá: ").append(bill.Discount == null ? 0 : bill.Discount).append("%\n")
                    .append("Thành tiền: ").append(bill.Total_Price == null ? 0 : bill.Total_Price).append(" đ\n")
                    .append("Trạng thái: ĐÃ THANH TOÁN");
        } else {
            sb.append("Không tìm thấy bill đang mở cho bàn ").append(tableId);
        }

        TextView tv = new TextView(this);
        tv.setText(sb.toString());
        tv.setTextSize(16f);
        tv.setPadding(24, 24, 24, 24);
        setContentView(tv);
    }
}
