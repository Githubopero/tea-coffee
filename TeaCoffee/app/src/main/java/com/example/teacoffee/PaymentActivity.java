package com.example.teacoffee;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacoffee.myDB.Bill;
import com.example.teacoffee.myDB.BillInfor;
import com.example.teacoffee.myDB.Food;
import com.example.teacoffee.myDB.MyDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    private LinearLayout listContainer, qrContainer;
    private TextView tvTable, tvTotalQty, tvSubtotal, tvDiscount, tvGrand;

    private int tableId = -1;
    private Bill currentBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // View refs
        listContainer = findViewById(R.id.listContainer);
        tvTable       = findViewById(R.id.tvTable);
        tvTotalQty    = findViewById(R.id.tvTotalQty);
        tvSubtotal    = findViewById(R.id.tvSubtotal);
        tvDiscount    = findViewById(R.id.tvDiscount);
        tvGrand       = findViewById(R.id.tvGrand);

        tableId = getIntent().getIntExtra("tableId", -1);
        if (tableId <= 0) {
            Toast.makeText(this, "Thiếu thông tin bàn", Toast.LENGTH_SHORT).show();
            finish(); return;
        }
        tvTable.setText("Bàn số " + tableId);

        loadBillAndRender();

        // Tiền mặt
        findViewById(R.id.btnCash).setOnClickListener(v -> {
            if (currentBill == null) { Toast.makeText(this, "Không có bill mở", Toast.LENGTH_SHORT).show(); return; }
            confirmAndPay("Xác nhận thanh toán bằng tiền mặt?");
        });

        // Chuyển khoản: mở popup QR giữa màn hình
        findViewById(R.id.btnTransfer).setOnClickListener(v -> {
            showQRDialog(); // ✅ gọi hàm hiển thị dialog QR mới
        });




    }

    /** Nạp bill mở của bàn và vẽ danh sách món + tổng kết */
    private void loadBillAndRender() {
        MyDatabase db = MyDatabase.getINSTANCE(this);
        currentBill = db.getBillDAO().getOpenByTable(tableId, "0");
        if (currentBill == null) {
            currentBill = db.getBillDAO().getOpenByTable(tableId, "Open"); // fallback
        }
        listContainer.removeAllViews();

        if (currentBill == null) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Không có hóa đơn mở")
                    .setMessage("Bàn " + tableId + " hiện chưa có hóa đơn đang mở.")
                    .setPositiveButton("Về màn chính", (d, w) -> finish())
                    .setNegativeButton("Mở Order", (d, w) -> {
                        Intent i = new Intent(this, OrderActivity.class);
                        i.putExtra("tableId", tableId);
                        startActivity(i);
                        finish();
                    })
                    .show();
            return;
        }

        List<BillInfor> lines = db.getBillInforDAO().getByBillId(currentBill.Bill_Id);
        LayoutInflater inflater = LayoutInflater.from(this);

        int totalQty = 0;
        long subtotal = 0;

        // header
        addRow(inflater, "Tên món", "Đơn giá", "Số lượng", true);

        for (BillInfor li : lines) {
            Food f = db.getFoodDAO().getById(li.Food_Id);
            long price = (f == null ? 0 : f.Price);
            subtotal += price * li.Count;
            totalQty += li.Count;
            addRow(inflater, (f==null?("Món #"+li.Food_Id):f.Food_Name), VND.format(price), String.valueOf(li.Count), false);
        }

        // tổng kết
        tvTotalQty.setText("Tổng số lượng: " + totalQty);
        tvSubtotal.setText("Tiền hàng: " + VND.format(subtotal));

        int percent = currentBill.Discount == null ? 0 : currentBill.Discount;
        long grand = currentBill.Total_Price == null ? subtotal : currentBill.Total_Price;
        long discountMoney = Math.max(0, subtotal - grand);

        tvDiscount.setText("Chiết khấu: " + percent + "% (" + VND.format(discountMoney) + ")");
        tvGrand.setText("Tổng tiền thanh toán: " + VND.format(grand));
    }

    private void addRow(LayoutInflater inflater, String c1, String c2, String c3, boolean header) {
        TextView row = new TextView(this);
        row.setText((header ? "" : "• ") + c1 + "   " + c2 + "   " + c3);
        row.setTextSize(header ? 15f : 14f);
        row.setPadding(8, header ? 12 : 6, 8, header ? 12 : 6);
        listContainer.addView(row);
    }

    private void confirmAndPay(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage(message)
                .setPositiveButton("Có", (DialogInterface d, int w) -> doPay())
                .setNegativeButton("Hủy", null)
                .show();
    }

    /** Ghi DB: chốt bill, trả bàn về rảnh */
    private void doPay() {
        if (currentBill == null) return;
        MyDatabase db = MyDatabase.getINSTANCE(this);

        currentBill.Status = "1"; // Closed
        currentBill.Date_Checkout = System.currentTimeMillis();
        db.getBillDAO().update(currentBill);

        db.getTableDAO().updateStatus(0, tableId); // trả bàn về rảnh

        Toast.makeText(this, "Đã thanh toán xong cho bàn " + tableId, Toast.LENGTH_SHORT).show();

        // QUAY VỀ UserHomeActivity THAY VÌ chỉ finish()
        Intent back = new Intent(this, UserHomeActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(back);
        finish();
    }
    /** Hiển thị popup QR ở giữa màn hình */
    private void showQRDialog() {
        View qrView = getLayoutInflater().inflate(R.layout.dialog_qr_payment, null);

        new AlertDialog.Builder(this)
                .setView(qrView)
                .setCancelable(true)
                .create()
                .show();

        Button btnDone = qrView.findViewById(R.id.btnDoneQR);
        btnDone.setOnClickListener(x -> {
            confirmAndPay("Xác nhận đã nhận tiền chuyển khoản?");
        });
    }


}
