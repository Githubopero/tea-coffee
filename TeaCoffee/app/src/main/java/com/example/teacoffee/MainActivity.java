package com.example.teacoffee;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.teacoffee.myDB.Food;
import com.example.teacoffee.myDB.FoodCategory;
import com.example.teacoffee.myDB.MyDatabase;
import com.example.teacoffee.myDB.TableEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // ====== KÍCH HOẠT TẠO DB LẦN ĐẦU (để Room chạy seed trong MyDatabase) ======
        // Gọi DAO bất kỳ sẽ khiến Room build database -> onCreate() callback chạy và insert dữ liệu mẫu.
        MyDatabase db = MyDatabase.getINSTANCE(this);                             // <-- (1) có thể xoá sau
        db.getOpenHelper().getWritableDatabase();

        List<TableEntity> tables = db.getTableDAO().getAll();                     // <-- (2) có thể xoá sau
        List<FoodCategory> cats   = db.getFoodCategoryDAO().getAll();             // <-- (2) có thể xoá sau
        List<Food> foods         = db.getFoodDAO().getAll();                      // <-- (2) có thể xoá sau
        Log.d("DB_CHECK", "Tables=" + tables.size() + ", Cats=" + cats.size() + ", Foods=" + foods.size()); // <-- (3) có thể xoá sau
        Log.d("DB_CHECK", "foods=" + db.getFoodDAO().countAll());
        // ===========================================================================

        // TODO: phần UI thật sự của bạn ở dưới...
    }
}