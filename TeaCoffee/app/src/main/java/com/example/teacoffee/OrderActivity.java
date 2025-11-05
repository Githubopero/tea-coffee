package com.example.teacoffee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacoffee.myDB.Bill;
import com.example.teacoffee.myDB.BillInfor;
import com.example.teacoffee.myDB.Food;
import com.example.teacoffee.myDB.FoodCategory;
import com.example.teacoffee.myDB.MyDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private Spinner spnTable;
    private LinearLayout catContainer, listContainer;
    private TextView tvTotalQty, tvSubtotal, tvDiscount, tvGrand;
    private Button btnSave;

    private List<FoodCategory> categories = new ArrayList<>();
    private HashMap<Integer, Integer> cartQty = new HashMap<>(); // FoodId -> qty
    private HashMap<Integer, Food> foodIndex = new HashMap<>();  // FoodId -> Food

    private final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private int selectedTableId = -1;
    private double discountRate = 0.0; // ví dụ 10% thì = 0.1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        spnTable      = findViewById(R.id.spnTable);
        catContainer  = findViewById(R.id.catContainer);
        listContainer = findViewById(R.id.listContainer);
        tvTotalQty    = findViewById(R.id.tvTotalQty);
        tvSubtotal    = findViewById(R.id.tvSubtotal);
        tvDiscount    = findViewById(R.id.tvDiscount);
        tvGrand       = findViewById(R.id.tvGrandTotal);
        btnSave       = findViewById(R.id.btnSave);

        // nhận tableId
        selectedTableId = getIntent().getIntExtra("tableId", -1);

        setupTableSpinner();
        loadCategories();
        if (!categories.isEmpty()) {
            buildCategoryChips(categories);
            loadFoodsByCategory(categories.get(0).Food_Category_Id);
        }

        btnSave.setOnClickListener(v -> {
            if (selectedTableId <= 0) {
                Toast.makeText(this, "Thiếu bàn!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cartQty.isEmpty()) {
                Toast.makeText(this, "Chưa chọn món!", Toast.LENGTH_SHORT).show();
                return;
            }

            // TÍNH TỔNG & CHIẾT KHẤU
            long sub = 0;
            for (Integer foodId : cartQty.keySet()) {
                int q = cartQty.get(foodId);
                Food f = foodIndex.containsKey(foodId) ? foodIndex.get(foodId)
                        : MyDatabase.getINSTANCE(this).getFoodDAO().getById(foodId);
                if (f != null) sub += (long) f.Price * q;
            }
            long discount = Math.round(sub * discountRate);
            long grand = sub - discount;

            // 1) Tạo bill (Status = 0: chưa thanh toán)
            Bill bill = new Bill();
            bill.Table_Id = selectedTableId;
            bill.Date_Checkin= System.currentTimeMillis();
            bill.Status = String.valueOf(0);
            bill.Discount = (int) Math.round(discountRate * 100);
            bill.Total_Price = Math.toIntExact(grand);
            long billId = MyDatabase.getINSTANCE(this).getBillDAO().insert(bill);

            // 2) Insert các dòng BillInfor
            for (Integer foodId : cartQty.keySet()) {
                int q = cartQty.get(foodId);
                Food f = foodIndex.containsKey(foodId) ? foodIndex.get(foodId)
                        : MyDatabase.getINSTANCE(this).getFoodDAO().getById(foodId);
                if (f == null) continue;

                BillInfor bi = new BillInfor();
                bi.Bill_Id = (int) billId;      // hoặc long nếu entity là long
                bi.Food_Id = foodId;
                bi.Count = q;
                MyDatabase.getINSTANCE(this).getBillInforDAO().insert(bi);
            }

            // 3) Cập nhật trạng thái bàn = bận
            MyDatabase.getINSTANCE(this).getTableDAO().updateStatus(selectedTableId, 1);

            // 4) Quay về UserHome + kết thúc
            Toast.makeText(this, "Đã lưu hóa đơn!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private void setupTableSpinner() {
        // Tạo danh sách "Bàn số X"
        List<String> items = new ArrayList<>();
        int tableCount = 20; // hoặc lấy từ DB
        for (int i = 1; i <= tableCount; i++) items.add("Bàn số " + i);

        ArrayAdapter<String> adp = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, items);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTable.setAdapter(adp);

        if (selectedTableId > 0 && selectedTableId <= tableCount) {
            spnTable.setSelection(selectedTableId - 1, true);
        }
    }

    private void loadCategories() {
        categories = MyDatabase.getINSTANCE(this).getFoodCategoryDAO().getAll();
    }

    private void buildCategoryChips(List<FoodCategory> cats) {
        catContainer.removeAllViews();

        for (FoodCategory c : cats) {
            // Tạo Button “chip” bằng code
            Button chip = new Button(this);
            chip.setText(c.Food_Category_Name);

            // Style đơn giản giống @style/ChipCategory
            chip.setAllCaps(false);
            chip.setTextColor(0xFF4E4E4E);
            chip.setBackgroundResource(R.drawable.bg_chip_inactive);
            int padH = dp(14), padV = dp(8);
            chip.setPadding(padH, padV, padH, padV);

            // Margin end 8dp
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.setMarginEnd(dp(8));
            chip.setLayoutParams(lp);

            chip.setOnClickListener(v -> loadFoodsByCategory(c.Food_Category_Id));
            catContainer.addView(chip);
        }
    }


    private void loadFoodsByCategory(int catId) {
        listContainer.removeAllViews();
        foodIndex.clear();

        List<Food> foods = MyDatabase.getINSTANCE(this).getFoodDAO().getByCategory(catId);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Food f : foods) {
            View row = inflater.inflate(R.layout.item_order_row, listContainer, false);

            TextView tvName  = row.findViewById(R.id.tvFoodName);
            TextView tvPrice = row.findViewById(R.id.tvFoodPrice);
            TextView tvQty   = row.findViewById(R.id.tvQty);
            Button btnMinus  = row.findViewById(R.id.btnMinus);
            Button btnPlus   = row.findViewById(R.id.btnPlus);

            tvName.setText(f.Food_Name);
            tvPrice.setText(VND.format(f.Price));

            int initQty = cartQty.getOrDefault(f.Food_Id, 0);
            tvQty.setText(String.valueOf(initQty));

            // Lưu index để tính tiền nhanh
            foodIndex.put(f.Food_Id, f);

            btnPlus.setOnClickListener(v -> {
                int q = cartQty.getOrDefault(f.Food_Id, 0) + 1;
                cartQty.put(f.Food_Id, q);
                tvQty.setText(String.valueOf(q));
                updateTotals();
            });

            btnMinus.setOnClickListener(v -> {
                int q = cartQty.getOrDefault(f.Food_Id, 0);
                if (q > 0) q--;
                if (q == 0) cartQty.remove(f.Food_Id); else cartQty.put(f.Food_Id, q);
                tvQty.setText(String.valueOf(q));
                updateTotals();
            });

            listContainer.addView(row);
        }

        updateTotals();
    }

    private void updateTotals() {
        int totalQty = 0;
        long sub = 0;

        for (Integer foodId : cartQty.keySet()) {
            int q = cartQty.getOrDefault(foodId, 0);
            totalQty += q;

            // Ưu tiên lấy từ cache foodIndex, nếu chưa có thì get từ DB
            Food f = foodIndex.containsKey(foodId)
                    ? foodIndex.get(foodId)
                    : MyDatabase.getINSTANCE(this).getFoodDAO().getById(foodId);

            if (f != null) sub += (long) f.Price * q;
        }

        long discount = Math.round(sub * discountRate);
        long grand    = sub - discount;

        tvTotalQty.setText("Tổng số lượng: " + totalQty);
        tvSubtotal.setText("Tiền hàng: " + VND.format(sub));
        tvDiscount.setText("Chiết khấu: " + Math.round(discountRate * 100) + "%");
        tvGrand.setText("Thành tiền: " + VND.format(grand));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
