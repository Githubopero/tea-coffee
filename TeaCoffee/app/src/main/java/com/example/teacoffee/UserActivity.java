// com.example.teacoffee.UserActivity
package com.example.teacoffee;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        String name = getIntent().getStringExtra("displayName");
        tv.setText("Xin chào STAFF: " + name);
        tv.setTextSize(20);
        setContentView(tv);
    }
}
