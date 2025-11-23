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

import androidx.appcompat.app.AlertDialog;
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
    private Button btnSave, btnCancel;
    private int currentCatId = 0;
    private HashMap<Integer, Double> catDiscountRate = new HashMap<>(); // CategoryId -> rate

    private Integer currentOpenBillId = null; // null: chưa có bill mở cho bàn hiện tại

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
        btnCancel = findViewById(R.id.btnCancel);


        // nhận tableId
        selectedTableId = getIntent().getIntExtra("tableId", -1);

        setupTableSpinner();
        loadCategories();
        // đảm bảo currentCatId có giá trị
        if (!categories.isEmpty()) {
            currentCatId = categories.get(0).Food_Category_Id;
        }
        // nạp giỏ theo bàn được nhận từ Intent
        refreshCartForTable();


        if (!categories.isEmpty()) {
            buildCategoryChips(categories);
            loadFoodsByCategory(categories.get(0).Food_Category_Id);
        }
        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Hủy hóa đơn")
                    .setMessage("Bạn có chắc muốn hủy, không lưu hóa đơn cho bàn này?")
                    .setPositiveButton("Hủy", (dialog, which) -> {
                        // KHÔNG ghi gì xuống DB, bàn giữ nguyên như lúc mở màn hình
                        Toast.makeText(this, "Đã hủy, không lưu hóa đơn", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });


        btnSave.setOnClickListener(v -> {
            if (selectedTableId <= 0) { Toast.makeText(this, "Thiếu bàn!", Toast.LENGTH_SHORT).show(); return; }
            if (cartQty.isEmpty())     { Toast.makeText(this, "Chưa chọn món!", Toast.LENGTH_SHORT).show(); return; }

            Totals t = computeTotals(); // <-- dùng hàm mới
            var db = MyDatabase.getINSTANCE(this);

            // 1) Lấy (hoặc tạo) bill mở của bàn hiện tại
            Bill open = db.getBillDAO().getOpenByTable(selectedTableId, "0");
            int billId;
            if (open == null) {
                Bill bill = new Bill();
                bill.Table_Id = selectedTableId;
                bill.Date_Checkin = System.currentTimeMillis();
                bill.Status = "0"; // open
                bill.Discount = t.effPercent;                 // lưu % hiệu dụng
                bill.Total_Price = Math.toIntExact(t.grand);  // thành tiền sau CK
                billId = (int) db.getBillDAO().insert(bill);
                db.getTableDAO().updateStatus(1, selectedTableId);
            } else {
                open.Discount = t.effPercent;
                open.Total_Price = Math.toIntExact(t.grand);
                db.getBillDAO().update(open);
                billId = open.Bill_Id;
            }
            currentOpenBillId = billId;

            // 2) Upsert BillInfor theo giỏ
            for (Integer foodId : cartQty.keySet()) {
                int q = cartQty.get(foodId);
                BillInfor exist = db.getBillInforDAO().findByBillAndFood(billId, foodId);
                if (exist == null) {
                    BillInfor bi = new BillInfor();
                    bi.Bill_Id = billId;
                    bi.Food_Id = foodId;
                    bi.Count = q;
                    db.getBillInforDAO().insert(bi);
                } else {
                    exist.Count = q;
                    db.getBillInforDAO().update(exist);
                }
            }

            // 3) Xóa các món về 0
            List<BillInfor> lines = db.getBillInforDAO().getByBillId(billId);
            for (BillInfor bi : lines) {
                int q = cartQty.getOrDefault(bi.Food_Id, 0);
                if (q <= 0) db.getBillInforDAO().delete(bi);
            }

            Toast.makeText(this, "Đã lưu hóa đơn cho bàn " + selectedTableId, Toast.LENGTH_SHORT).show();
            finish();
        });


        activateTabsForOrder();
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
        spnTable.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedTableId = position + 1;   // vì danh sách "Bàn số 1..20"
                refreshCartForTable();            // nạp lại giỏ theo bàn đang chọn
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void loadCategories() {
        categories = MyDatabase.getINSTANCE(this).getFoodCategoryDAO().getAll();
        // map % chiết khấu theo tên danh mục
        catDiscountRate.clear();
        for (FoodCategory c : categories) {
            String name = (c.Food_Category_Name == null ? "" : c.Food_Category_Name)
                    .toLowerCase(java.util.Locale.ROOT);
            double r = 0.0;
            if (name.contains("cà phê") || name.contains("ca phe") || name.contains("coffee")) {
                r = 0.055; // 5.5%
            } else if (name.contains("sinh tố") || name.contains("sinh to") || name.contains("smoothie")) {
                r = 0.10;  // 10%
            }else {
                r= 0.10;
            }
            // có thể thêm: else if (name.contains("trà") || name.contains("tea")) r = 0.0;
            catDiscountRate.put(c.Food_Category_Id, r);
        }
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
        currentCatId = catId;   // nhớ category đang xem

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
    private static class Totals {
        long sub;        // tổng tiền hàng
        long discount;   // tổng tiền CK
        long grand;      // thành tiền
        int  effPercent; // % hiệu dụng = discount/sub (làm tròn)
        int  totalQty;   // tổng số lượng
    }

    private Totals computeTotals() {
        Totals t = new Totals();
        for (Integer foodId : cartQty.keySet()) {
            int q = cartQty.getOrDefault(foodId, 0);
            if (q <= 0) continue;

            Food f = foodIndex.containsKey(foodId)
                    ? foodIndex.get(foodId)
                    : MyDatabase.getINSTANCE(this).getFoodDAO().getById(foodId);
            if (f == null) continue;

            long line = (long) f.Price * q;
            t.sub += line;
            t.totalQty += q;
//            Tiền hàng = S
//            Tiền cà phê = C
//            Tiền sinh tố = M
//            Chiết khấu = D = 0.055*C + 0.10*M
//            % hiệu dụng hiển thị ≈ (D / S) * 100
            double rate = catDiscountRate.getOrDefault(f.Food_Category_Id, 0.0);
            t.discount += Math.round(line * rate);
        }
        t.grand = t.sub - t.discount;
        t.effPercent = (t.sub > 0) ? (int) Math.round(t.discount * 100.0 / t.sub) : 0;
        return t;
    }

    private void updateTotals() {
        Totals t = computeTotals();
        tvTotalQty.setText("Tổng số lượng: " + t.totalQty);
        tvSubtotal.setText("Tiền hàng: " + VND.format(t.sub));
        // hiển thị cả tiền CK và % hiệu dụng để rõ khi nhiều danh mục
        tvDiscount.setText(
                "Chiết khấu: " + VND.format(t.discount)
                        + " (≈ " + String.format(Locale.getDefault(), "%.2f", (t.discount * 100.0 / Math.max(1, t.sub))) + "%)"
        );

        tvGrand.setText("Thành tiền: " + VND.format(t.grand));
    }


    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
    private void activateTabsForOrder(){
        View tabReserve = findViewById(R.id.tabReserve);
        View tabOrder   = findViewById(R.id.tabOrder);
        View tabPayment = findViewById(R.id.tabPayment);

        tabReserve.setBackgroundResource(R.drawable.bg_chip_inactive);
        tabOrder.setBackgroundResource(R.drawable.bg_chip_active);
        tabPayment.setBackgroundResource(R.drawable.bg_chip_inactive);

        ((android.widget.ImageView)findViewById(R.id.ivReserve)).setColorFilter(android.graphics.Color.parseColor("#4E4E4E"));
        ((android.widget.TextView)findViewById(R.id.tvReserve)).setTextColor(android.graphics.Color.parseColor("#4E4E4E"));

        ((android.widget.ImageView)findViewById(R.id.ivOrder)).setColorFilter(android.graphics.Color.WHITE);
        ((android.widget.TextView)findViewById(R.id.tvOrder)).setTextColor(android.graphics.Color.WHITE);

        ((android.widget.ImageView)findViewById(R.id.ivPayment)).setColorFilter(android.graphics.Color.parseColor("#4E4E4E"));
        ((android.widget.TextView)findViewById(R.id.tvPayment)).setTextColor(android.graphics.Color.parseColor("#4E4E4E"));
    }
    private void refreshCartForTable() {
        cartQty.clear();
        currentOpenBillId = null;

        var db = MyDatabase.getINSTANCE(this);
        Bill open = db.getBillDAO().getOpenByTable(selectedTableId, "0"); // bill chưa thanh toán

        if (open != null) {
            currentOpenBillId = open.Bill_Id;
            // lấy tất cả dòng chi tiết của bill
            List<BillInfor> lines = db.getBillInforDAO().getByBillId(open.Bill_Id);
            for (BillInfor bi : lines) {
                cartQty.put(bi.Food_Id, bi.Count); // đưa lên UI
            }
        }

        // vẽ lại list theo category hiện tại (nếu chưa có thì lấy category đầu tiên)
        if (currentCatId == 0 && !categories.isEmpty()) {
            currentCatId = categories.get(0).Food_Category_Id;
        }
        loadFoodsByCategory(currentCatId); // vì tvQty đọc từ cartQty nên sẽ hiện đúng số lượng
    }


}
