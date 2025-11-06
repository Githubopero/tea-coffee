package com.example.teacoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacoffee.myDB.MyDatabase;
import com.example.teacoffee.myDB.TableEntity;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity {

    private GridLayout gridTables;
    private Button btnReserve, btnMerge, btnDelete;
    private Integer selectedTableId = null;
    private List<TableEntity> tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        gridTables  = findViewById(R.id.gridTables);
        btnReserve  = findViewById(R.id.btnReserve);
        btnMerge    = findViewById(R.id.btnMerge);
        btnDelete   = findViewById(R.id.btnDelete);

        loadTables();

        btnReserve.setOnClickListener(v -> {
            if (selectedTableId == null) {
                Toast.makeText(this, "Hãy chọn 1 bàn trước", Toast.LENGTH_SHORT).show();
                return;
            }
            // Tìm bàn đã chọn
            TableEntity t = null;
            for (TableEntity x : tables) if (x.Table_Id == selectedTableId) { t = x; break; }
            if (t == null) return;

            if (t.Status != 0) {
                Toast.makeText(this, "Bàn đang bận, vui lòng chọn bàn trống", Toast.LENGTH_SHORT).show();
                return;
            }
            // sang OrderActivity
            Intent it = new Intent(this, OrderActivity.class);
            it.putExtra("tableId", selectedTableId);
            startActivity(it);
        });

        // demo: 2 nút còn lại hiện toast
        btnMerge.setOnClickListener(v ->
                Toast.makeText(this,"Tính năng gộp bàn sẽ làm sau",Toast.LENGTH_SHORT).show());
        btnDelete.setOnClickListener(v ->
                Toast.makeText(this,"Tính năng xóa sẽ làm sau",Toast.LENGTH_SHORT).show());
        btnReserve.setEnabled(false);

    }

    private void loadTables() {
        tables = MyDatabase.getINSTANCE(this).getTableDAO().getAll();

        gridTables.removeAllViews();
        int colCount = gridTables.getColumnCount(); // 4
        int i = 0;

        for (TableEntity t : tables) {
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(t.Table_Id));
            tv.setGravity(android.view.Gravity.CENTER);
            tv.setTextSize(16);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = dp(60);                         // dùng dp trực tiếp cho gọn
            lp.columnSpec = GridLayout.spec(i % colCount, 1f);
            lp.setMargins(dp(6), dp(6), dp(6), dp(6));
            tv.setLayoutParams(lp);

            tv.setTag(t.Table_Id);
            tv.setOnClickListener(v -> {
                int id = (Integer) v.getTag();
                TableEntity cur = null;
                for (TableEntity x : tables) if (x.Table_Id == id) { cur = x; break; }
                if (cur == null) return;

                if (cur.Status == 0) {
                    // bàn trống → chọn để đặt
                    selectedTableId = id;
                    refreshSelection();
                } else {
                    // bàn bận → nhảy sang Payment
                    Intent it = new Intent(this, PaymentActivity.class);
                    it.putExtra("tableId", id);
                    startActivity(it);
                }
            });

            gridTables.addView(tv);
            i++;
        }
        refreshSelection(); // vẽ nền theo trạng thái
    }

    private void refreshSelection() {
        for (int idx = 0; idx < gridTables.getChildCount(); idx++) {
            View v = gridTables.getChildAt(idx);
            Integer id = (Integer) v.getTag();

            TableEntity t = null;
            for (TableEntity x : tables) if (x.Table_Id == id) { t = x; break; }
            if (t == null) continue;

            if (t.Status == 1) {
                v.setBackgroundResource(R.drawable.bg_table_busy);      // đỏ
            } else if (selectedTableId != null && id.equals(selectedTableId)) {
                v.setBackgroundResource(R.drawable.bg_table_selected);   // xanh nhạt + viền
            } else {
                v.setBackgroundResource(R.drawable.bg_table_free);       // xanh
            }
        }
        btnReserve.setEnabled(selectedTableId != null);

    }

    @Override protected void onResume() {
        super.onResume();
        loadTables();     // quay lại từ Order/Payment sẽ tự refresh
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
    private void activateTabsForReserve(){
        View tabReserve = findViewById(R.id.tabReserve);
        View tabOrder   = findViewById(R.id.tabOrder);
        View tabPayment = findViewById(R.id.tabPayment);

        tabReserve.setBackgroundResource(R.drawable.bg_chip_active);
        tabOrder.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabPayment.setBackgroundResource(R.drawable.bg_chip_inactive);

        ((android.widget.ImageView)findViewById(R.id.ivReserve)).setColorFilter(android.graphics.Color.WHITE);
        ((android.widget.TextView)findViewById(R.id.tvReserve)).setTextColor(android.graphics.Color.WHITE);

        ((android.widget.ImageView)findViewById(R.id.ivOrder)).setColorFilter(android.graphics.Color.parseColor("#4E4E4E"));
        ((android.widget.TextView)findViewById(R.id.tvOrder)).setTextColor(android.graphics.Color.parseColor("#4E4E4E"));
        ((android.widget.ImageView)findViewById(R.id.ivPayment)).setColorFilter(android.graphics.Color.parseColor("#4E4E4E"));
        ((android.widget.TextView)findViewById(R.id.tvPayment)).setTextColor(android.graphics.Color.parseColor("#4E4E4E"));
    }


}
