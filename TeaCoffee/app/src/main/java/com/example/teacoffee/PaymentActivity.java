package com.example.teacoffee;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        int tableId = getIntent().getIntExtra("tableId", -1);
        tv.setText("Thanh toán cho bàn: " + tableId + " (demo)");
        tv.setTextSize(18);
        setContentView(tv);
    }
}
