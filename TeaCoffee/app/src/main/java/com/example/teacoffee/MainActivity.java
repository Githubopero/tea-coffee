package com.example.teacoffee;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teacoffee.myDB.Account;
import com.example.teacoffee.myDB.Food;
import com.example.teacoffee.myDB.FoodCategory;
import com.example.teacoffee.myDB.MyDatabase;
import com.example.teacoffee.myDB.TableEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtUser, edtPass;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1) Ánh xạ view
        edtUser  = findViewById(R.id.username);
        edtPass  = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);

        // 2) Ép mở DB (để Inspector luôn thấy connected & callback onCreate đã seed)
        MyDatabase db = MyDatabase.getINSTANCE(this);
        db.getOpenHelper().getWritableDatabase();

        // 3) Xử lý đăng nhập
        btnLogin.setOnClickListener(v -> {
            String u = edtUser.getText().toString().trim();
            String p = edtPass.getText().toString().trim();

            // 3.1) Validate đơn giản
            if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
                Toast.makeText(this, "Vui lòng nhập đủ Username và Password", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3.2) Gọi DAO: nếu sai trả về null
            Account acc = db.getAccountDAO().login(u, p);

            if (acc == null) {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3.3) Điều hướng theo quyền
            if ("ADMIN".equalsIgnoreCase(acc.Type)) {
                // Mở màn hình admin
                Intent it = new Intent(this, AdminHomeActivity.class);
                it.putExtra("displayName", acc.Display_Name);
                startActivity(it);
                //thêm sửa xóa nhân viên = tài khoản nhân viên
            } else {
                // Mở màn hình staff
                Intent it = new Intent(this, UserHomeActivity.class);
                it.putExtra("displayName", acc.Display_Name);
                startActivity(it);
            }
        });
    }

}