// com.example.teacoffee.AdminActivity
package com.example.teacoffee;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        String name = getIntent().getStringExtra("displayName");
        tv.setText("Xin chào ADMIN: " + name);
        tv.setTextSize(20);
        setContentView(tv);
    }
}
