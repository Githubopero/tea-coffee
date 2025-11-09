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
    private Button btnReserve, btnMerge, btnPay;
    private Integer selectedTableId = null;
    private List<TableEntity> tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        gridTables  = findViewById(R.id.gridTables);
        btnReserve  = findViewById(R.id.btnReserve);
        btnMerge    = findViewById(R.id.btnMerge);
        btnPay      = findViewById(R.id.btnPay);

        loadTables();

        // Đặt bàn (chỉ cho bàn trống)
        btnReserve.setOnClickListener(v -> {
            if (selectedTableId == null) {
                Toast.makeText(this, "Hãy chọn 1 bàn trước", Toast.LENGTH_SHORT).show();
                return;
            }
            TableEntity t = null;
            for (TableEntity x : tables) if (x.Table_Id == selectedTableId) { t = x; break; }
            if (t == null) return;

            if (t.Status != 0) {
                Toast.makeText(this, "Bàn đang bận, vui lòng chọn bàn trống", Toast.LENGTH_SHORT).show();
                return;
            }
            // Gửi CẢ 2 khóa -> OrderActivity đọc cái nào cũng đúng
            Intent it = new Intent(this, OrderActivity.class);
            it.putExtra("tableId",  selectedTableId);
            startActivity(it);
        });

        btnMerge.setOnClickListener(v ->
                Toast.makeText(this,"Tính năng gộp bàn sẽ làm sau",Toast.LENGTH_SHORT).show());

        // Thanh toán
        btnPay.setOnClickListener(v -> {
            if (selectedTableId == null) {
                Toast.makeText(this, "Hãy chọn 1 bàn trước", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isBusy(selectedTableId)) {
                Toast.makeText(this, "Bàn đang trống, không thể thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            startPayment(selectedTableId);  // mở trang thanh toán
        });


        btnReserve.setEnabled(false);
    }

    private void loadTables() {
        tables = MyDatabase.getINSTANCE(this).getTableDAO().getAll();

        gridTables.removeAllViews();
        int colCount = gridTables.getColumnCount();
        if (colCount <= 0) colCount = 4; // fallback
        int i = 0;

        for (TableEntity t : tables) {
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(t.Table_Id));
            tv.setGravity(android.view.Gravity.CENTER);
            tv.setTextSize(16);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = dp(60);
            lp.columnSpec = GridLayout.spec(i % colCount, 1f);
            lp.setMargins(dp(6), dp(6), dp(6), dp(6));
            tv.setLayoutParams(lp);

            tv.setTag(t.Table_Id);
            bindTableCell(tv, t);           // click + long-press
            gridTables.addView(tv);
            i++;
        }
        refreshSelection();
    }

    private void refreshSelection() {
        for (int idx = 0; idx < gridTables.getChildCount(); idx++) {
            View v = gridTables.getChildAt(idx);
            Integer id = (Integer) v.getTag();
            TableEntity t = findTable(id);
            if (t == null) continue;

            if (t.Status == 1) {
                v.setBackgroundResource(R.drawable.bg_table_busy);     // đỏ
            } else if (selectedTableId != null && id.equals(selectedTableId)) {
                v.setBackgroundResource(R.drawable.bg_table_selected);  // xanh nhạt + viền
            } else {
                v.setBackgroundResource(R.drawable.bg_table_free);      // xanh
            }
        }

        // Chỉ cho đặt bàn khi chọn bàn trống
        btnReserve.setEnabled(selectedTableId != null && !isBusy(selectedTableId));
        // Chỉ cho thanh toán khi chọn bàn bận (đỏ)
        btnPay.setEnabled(selectedTableId != null && isBusy(selectedTableId));
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadTables();
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

    private void bindTableCell(TextView tv, TableEntity t) {
        tv.setOnClickListener(v -> {
            selectedTableId = t.Table_Id;     // luôn chọn bàn, kể cả đỏ
            refreshSelection();
            if (t.Status == 1) {
                Toast.makeText(this, "Bàn đang bận – bấm nút Thanh toán hoặc nhấn giữ để mở menu", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đã chọn " + t.Table_Name, Toast.LENGTH_SHORT).show();
            }
        });

        // NHẤN GIỮ: menu sửa/thanh toán – chỉ cho bàn bận
        tv.setOnLongClickListener(v -> {
            if (t.Status != 1) return true;
            androidx.appcompat.widget.PopupMenu pop = new androidx.appcompat.widget.PopupMenu(this, v);
            pop.getMenu().add(0, 1, 0, "Sửa thông tin bill");
            pop.getMenu().add(0, 2, 1, "Thanh toán");
            pop.setOnMenuItemClickListener(mi -> {
                if (mi.getItemId() == 1) {
                    Intent i = new Intent(this, OrderActivity.class);
                    i.putExtra("tableId", t.Table_Id);
                    startActivity(i);
                    return true;
                } else if (mi.getItemId() == 2) {
                    startPayment(t.Table_Id);
                    return true;
                }
                return false;
            });
            pop.show();
            return true;
        });
    }


    private void startPayment(int tableId) {
        Intent i = new Intent(this, PaymentActivity.class);
        i.putExtra("tableId",  tableId); // gửi kèm cho tương thích cũ
        startActivity(i);
    }
    private TableEntity findTable(int id) {
        if (tables == null) return null;
        for (TableEntity x : tables) if (x.Table_Id == id) return x;
        return null;
    }
    private boolean isBusy(Integer id) {
        TableEntity t = (id == null) ? null : findTable(id);
        return t != null && t.Status == 1;
    }
}
